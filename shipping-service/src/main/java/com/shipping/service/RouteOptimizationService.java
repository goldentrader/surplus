package com.shipping.service;

import com.shipping.model.Location;
import com.shipping.model.Route;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteOptimizationService {

    public Route optimizeRoute(Location startLocation, Location endLocation, List<Location> waypoints) {
        // TODO: Implémenter l'algorithme d'optimisation de route
        // Pour l'instant, nous retournons simplement une route non optimisée
        Route route = new Route();
        route.setStartLocation(startLocation);
        route.setEndLocation(endLocation);
        route.setWaypoints(waypoints);
        return route;
    }

    public double calculateDistance(Location point1, Location point2) {
        // Implémentation de la formule de Haversine pour calculer la distance entre deux points
        double R = 6371; // Rayon de la Terre en km
        double lat1 = Math.toRadians(point1.getLatitude());
        double lat2 = Math.toRadians(point2.getLatitude());
        double deltaLat = Math.toRadians(point2.getLatitude() - point1.getLatitude());
        double deltaLon = Math.toRadians(point2.getLongitude() - point1.getLongitude());

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }
} 