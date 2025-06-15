package com.shipping.controller;

import com.shipping.event.OrderPaidEvent;
import com.shipping.model.Delivery;
import com.shipping.model.DeliveryStatus;
import com.shipping.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/order-paid")
    public ResponseEntity<Delivery> handleOrderPaid(@RequestBody OrderPaidEvent orderPaidEvent) {
        Delivery delivery = shippingService.createDeliveryFromOrder(orderPaidEvent);
        return ResponseEntity.ok(delivery);
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(shippingService.getDelivery(deliveryId));
    }

    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable Long deliveryId,
            @RequestParam DeliveryStatus status) {
        return ResponseEntity.ok(shippingService.updateDeliveryStatus(deliveryId, status));
    }
} 