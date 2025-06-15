package com.shipping.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Location {
    private Double latitude;
    private Double longitude;
    private String address;
    private String city;
    private String postalCode;
    private String country;
} 