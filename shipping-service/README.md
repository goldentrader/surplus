# Service de Shipping (Logistics Context)

Ce service est un microservice responsable de la gestion de la logistique physique des commandes, notamment la création et le suivi des livraisons, le calcul des frais d'expédition et l'intégration avec une API de transporteur (Shippo).

## Fonctionnalités Principales

*   **Création Automatique de Livraison** : Une livraison est automatiquement créée et mise en statut `PENDING` après qu'une commande a été payée (déclenchée par l'événement `OrderPaidEvent`).
*   **Calcul des Frais de Livraison** : Utilise l'API de Shippo pour obtenir les tarifs d'expédition en fonction des adresses de départ (magasin) et de destination (client), ainsi que le poids du colis.
*   **Création d'Étiquettes d'Expédition** : Permet de générer des étiquettes d'expédition via l'API de Shippo (cette fonctionnalité est implémentée dans `ShippoService`).
*   **Suivi des Statuts de Livraison** : Permet de mettre à jour et de consulter le statut des livraisons.
*   **Optimisation de Route** : Contient un service (`RouteOptimizationService`) pour la gestion des itinéraires, bien que l'implémentation actuelle soit basique (calcul de distance).

## Architecture et Composants

Le service est structuré selon les principes du Domain-Driven Design (DDD) pour le contexte de la logistique.

### Entités Principales (Package `com.shipping.model`)

*   `Delivery` : Représente une livraison. Contient des informations telles que l'ID de la commande, l'ID du livreur (si assigné), le statut, les heures de ramassage/livraison, et la route.
*   `Route` : Définit un itinéraire avec une adresse de départ, une adresse de fin et des points de passage (waypoints).
*   `Location` : Un objet de valeur (`Value Object`) encapsulant les détails d'une localisation (latitude, longitude, adresse, ville, code postal, pays).
*   `DeliveryStatus` : Une énumération pour les statuts possibles d'une livraison (`PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`).

### Événements (Package `com.shipping.event`)

*   `OrderPaidEvent` : Événement qui sera émis par le service de paiement (ou un autre service) une fois qu'une commande est payée. Il contient les informations nécessaires pour créer une livraison.

### Services de Domaine (Package `com.shipping.service`)

*   `ShippingService` : Le service principal qui orchestre la création des livraisons à partir des événements de commande payée et interagit avec les autres services.
*   `DeliveryAssignmentService` : Service pour l'assignation des livreurs et la mise à jour des statuts (actuellement, la logique d'assignation manuelle est moins prioritaire, mais le service est en place).
*   `RouteOptimizationService` : Service pour l'optimisation des itinéraires et le calcul des distances.
*   `ShippoService` : Service d'intégration avec l'API externe Shippo pour les calculs de tarifs et la génération d'étiquettes.

### Repositories (Package `com.shipping.repository`)

*   `DeliveryRepository` : Interface de persistance des données pour l'entité `Delivery`, utilisant Spring Data JPA.

### Contrôleurs REST (Package `com.shipping.controller`)

*   `ShippingController` : Expose les endpoints REST pour interagir avec le service de shipping.

## Points d'API

Le `ShippingController` expose les endpoints suivants :

*   `POST /api/shipping/order-paid` :
    *   **Description** : Reçoit un événement `OrderPaidEvent` pour déclencher la création automatique d'une livraison.
    *   **Corps de la requête** : `OrderPaidEvent` (JSON)
    *   **Réponse** : `Delivery` (JSON) de la livraison créée.

*   `GET /api/shipping/{deliveryId}` :
    *   **Description** : Récupère les détails d'une livraison spécifique par son ID.
    *   **Paramètres de chemin** : `deliveryId` (Long)
    *   **Réponse** : `Delivery` (JSON)

*   `PUT /api/shipping/{deliveryId}/status` :
    *   **Description** : Met à jour le statut d'une livraison.
    *   **Paramètres de chemin** : `deliveryId` (Long)
    *   **Paramètres de requête** : `status` (`DeliveryStatus` - PENDING, IN_PROGRESS, COMPLETED, CANCELLED)
    *   **Réponse** : `Delivery` (JSON) de la livraison mise à jour.

## Prérequis

*   **Java 17 ou plus récent**
*   **Maven**
*   **PostgreSQL** : Une base de données nommée `shipping_db` doit être créée. Les identifiants de connexion sont configurés dans `src/main/resources/application.yml`.
*   **Clé API Shippo** : Obtenez une clé API depuis votre compte [Shippo](https://goshippo.com/). Cette clé doit être définie dans la variable d'environnement `SHIPPO_API_KEY`.
    *   Pour Windows (PowerShell) : `$env:SHIPPO_API_KEY="votre_cle_api_shippo"`
    *   Pour Linux/macOS (Bash/Zsh) : `export SHIPPO_API_KEY="votre_cle_api_shippo"`

## Comment Exécuter

1.  **Naviguez vers le répertoire du service** :
    ```bash
    cd shipping-service
    ```
2.  **Définissez votre clé API Shippo** (si ce n'est pas déjà fait) :
    *   Pour Windows (PowerShell) :
        ```powershell
        $env:SHIPPO_API_KEY="votre_cle_api_shippo"
        ```
    *   Pour Linux/macOS (Bash/Zsh) :
        ```bash
        export SHIPPO_API_KEY="votre_cle_api_shippo"
        ```
3.  **Compilez et exécutez le service** :
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

Le service démarrera sur le port 8084 par défaut. 