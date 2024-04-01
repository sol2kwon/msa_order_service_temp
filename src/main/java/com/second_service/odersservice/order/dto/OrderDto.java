package com.second_service.odersservice.order.dto;

import com.second_service.odersservice.order.vo.ResponseOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDto implements Serializable {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String orderId;
    private String userId;

    private List<ResponseOrder> catalogs;



}
