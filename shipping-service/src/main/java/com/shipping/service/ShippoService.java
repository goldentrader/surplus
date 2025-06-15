package com.shipping.service;

import com.shipping.model.Location;
import com.shippo.Shippo;
import com.shippo.exception.APIConnectionException;
import com.shippo.exception.APIException;
import com.shippo.exception.AuthenticationException;
import com.shippo.exception.InvalidRequestException;
import com.shippo.model.Address;
import com.shippo.model.Rate;
import com.shippo.model.Shipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ShippoService {

    @Value("${shippo.api.key}")
    private String shippoApiKey;

    public ShippoService() {
        Shippo.apiKey = shippoApiKey;
    }

    public List<Rate> getShippingRates(Location fromLocation, Location toLocation, double weight, String weightUnit) {
        try {
            // Créer l'adresse d'expédition
            Map<String, Object> fromAddressMap = new HashMap<>();
            fromAddressMap.put("name", "Store Name");
            fromAddressMap.put("street1", fromLocation.getAddress());
            fromAddressMap.put("city", fromLocation.getCity());
            fromAddressMap.put("state", "");
            fromAddressMap.put("zip", fromLocation.getPostalCode());
            fromAddressMap.put("country", fromLocation.getCountry());
            fromAddressMap.put("phone", "");
            fromAddressMap.put("email", "");

            // Créer l'adresse de livraison
            Map<String, Object> toAddressMap = new HashMap<>();
            toAddressMap.put("name", "Customer Name");
            toAddressMap.put("street1", toLocation.getAddress());
            toAddressMap.put("city", toLocation.getCity());
            toAddressMap.put("state", "");
            toAddressMap.put("zip", toLocation.getPostalCode());
            toAddressMap.put("country", toLocation.getCountry());
            toAddressMap.put("phone", "");
            toAddressMap.put("email", "");

            // Créer les dimensions du colis
            Map<String, Object> parcelMap = new HashMap<>();
            parcelMap.put("length", "5");
            parcelMap.put("width", "5");
            parcelMap.put("height", "5");
            parcelMap.put("distance_unit", "in");
            parcelMap.put("weight", String.valueOf(weight));
            parcelMap.put("mass_unit", weightUnit);

            // Créer la demande d'expédition
            Map<String, Object> shipmentMap = new HashMap<>();
            shipmentMap.put("address_from", fromAddressMap);
            shipmentMap.put("address_to", toAddressMap);
            shipmentMap.put("parcels", parcelMap);
            shipmentMap.put("async", false);

            // Obtenir les tarifs d'expédition
            Shipment shipment = Shipment.create(shipmentMap);
            return shipment.getRates();

        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException e) {
            log.error("Error getting shipping rates from Shippo", e);
            throw new RuntimeException("Failed to get shipping rates", e);
        }
    }

    public String createShippingLabel(String rateId) {
        try {
            // Créer l'étiquette d'expédition
            Map<String, Object> transactionMap = new HashMap<>();
            transactionMap.put("rate", rateId);
            transactionMap.put("label_file_type", "PDF");
            transactionMap.put("async", false);

            // Retourner l'URL de l'étiquette
            return (String) com.shippo.model.Transaction.create(transactionMap).getLabelUrl();
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | APIException e) {
            log.error("Error creating shipping label with Shippo", e);
            throw new RuntimeException("Failed to create shipping label", e);
        }
    }
} 