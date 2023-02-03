package com.bajaks.RisWebshopApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReportItem {
    private String username;
    private String address;
    private Date orderTime;
    private Float total;

    private String isDelivered;

    private Date deliveryTime;
}
