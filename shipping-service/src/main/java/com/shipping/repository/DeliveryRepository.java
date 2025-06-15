package com.shipping.repository;

import com.shipping.model.Delivery;
import com.shipping.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByStatus(DeliveryStatus status);
    List<Delivery> findByDeliveryPersonId(Long deliveryPersonId);
    List<Delivery> findByOrderId(Long orderId);
} 