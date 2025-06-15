package com.shipping.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidEvent {
    private Long orderId;
    private String customerAddress;
    private Double customerLatitude;
    private Double customerLongitude;
    private String customerCity;
    private String customerPostalCode;
    private String customerCountry;
} 