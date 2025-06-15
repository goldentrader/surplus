package com.shipping.service;

import com.shipping.model.Delivery;
import com.shipping.model.DeliveryStatus;
import com.shipping.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentService {
    
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery assignDeliveryPerson(Long deliveryId, Long deliveryPersonId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new RuntimeException("Delivery not found"));
        
        delivery.setDeliveryPersonId(deliveryPersonId);
        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        
        return deliveryRepository.save(delivery);
    }

    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new RuntimeException("Delivery not found"));
        
        delivery.setStatus(newStatus);
        return deliveryRepository.save(delivery);
    }
} 