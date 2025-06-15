package com.shipping.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long orderId;
    private Long deliveryPersonId;
    
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Route route;
} 