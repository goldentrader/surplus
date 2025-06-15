package com.shipping.service;

import com.shipping.event.OrderPaidEvent;
import com.shipping.model.Delivery;
import com.shipping.model.DeliveryStatus;
import com.shipping.model.Location;
import com.shipping.model.Route;
import com.shipping.repository.DeliveryRepository;
import com.shippo.model.Rate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingService {
    
    private final DeliveryRepository deliveryRepository;
    private final RouteOptimizationService routeOptimizationService;
    private final ShippoService shippoService;

    @Transactional
    public Delivery createDeliveryFromOrder(OrderPaidEvent orderPaidEvent) {
        // Créer la location de livraison
        Location deliveryLocation = new Location();
        deliveryLocation.setAddress(orderPaidEvent.getCustomerAddress());
        deliveryLocation.setLatitude(orderPaidEvent.getCustomerLatitude());
        deliveryLocation.setLongitude(orderPaidEvent.getCustomerLongitude());
        deliveryLocation.setCity(orderPaidEvent.getCustomerCity());
        deliveryLocation.setPostalCode(orderPaidEvent.getCustomerPostalCode());
        deliveryLocation.setCountry(orderPaidEvent.getCustomerCountry());

        // Obtenir les tarifs d'expédition via Shippo
        Location storeLocation = getStoreLocation(); // À implémenter selon votre logique
        List<Rate> shippingRates = shippoService.getShippingRates(
            storeLocation,
            deliveryLocation,
            1.0, // poids en kg
            "kg"
        );

        // Créer la route
        Route route = routeOptimizationService.optimizeRoute(
            storeLocation,
            deliveryLocation,
            null
        );

        // Créer la livraison
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderPaidEvent.getOrderId());
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setRoute(route);

        return deliveryRepository.save(delivery);
    }

    @Transactional(readOnly = true)
    public Delivery getDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new RuntimeException("Delivery not found"));
        
        delivery.setStatus(newStatus);
        return deliveryRepository.save(delivery);
    }

    private Location getStoreLocation() {
        // À implémenter selon votre logique
        Location storeLocation = new Location();
        storeLocation.setAddress("123 Store Street");
        storeLocation.setCity("Store City");
        storeLocation.setPostalCode("12345");
        storeLocation.setCountry("FR");
        storeLocation.setLatitude(48.8566);
        storeLocation.setLongitude(2.3522);
        return storeLocation;
    }
} 