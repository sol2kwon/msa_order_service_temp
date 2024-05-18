package com.second_service.odersservice.order.controller;

import com.second_service.odersservice.order.dto.OrderDto;
import com.second_service.odersservice.order.messagequeue.KafkaProducer;
import com.second_service.odersservice.order.repository.OrderEntity;
import com.second_service.odersservice.order.service.OrderService;
import com.second_service.odersservice.order.vo.RequestOrder;
import com.second_service.odersservice.order.vo.ResponseOrder;
import jakarta.ws.rs.POST;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {
    Environment env;
    OrderService orderService;

    KafkaProducer kafkaProducer;
    @Autowired
    public OrderController(OrderService orderService, Environment env,  KafkaProducer kafkaProducer){
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder requestOrder){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createdOrder = orderService.createOrder(orderDto);

        kafkaProducer.send("example-catalog-topic",orderDto);

        ResponseOrder responseOrder = mapper.map(createdOrder,ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId){
        Iterable<OrderEntity> orderList =  orderService.getOrdersByUserId(userId);
        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach( d -> {
            result.add(new ModelMapper().map(d,ResponseOrder.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
