package com.shipping.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Location startLocation;

    @Embedded
    private Location endLocation;

    @ElementCollection
    private List<Location> waypoints;
} 