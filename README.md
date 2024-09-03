# Plateforme de Tailleur en Ligne

<div align="center">
  <img align="center" src="./maquettes/logo/1.png" alt="Logo" width="200"/>
  &nbsp;
  &nbsp;
  &nbsp;
  &nbsp;
  <img align="center" src="https://enaa.ma/images/icon/enaa-logo-white.svg" alt="Logo" width="200"/>
  &nbsp;
  &nbsp;
  &nbsp;
  &nbsp;
  <img align="center" src="https://atomrace.com/blog/wp-content/uploads/2018/05/spring-boot-logo.png" alt="Logo" width="200"/>
  &nbsp;
  &nbsp;
  &nbsp;
  &nbsp;
  <img align="center" src="https://angular.dev/assets/images/press-kit/angular_wordmark_gradient.png" alt="Logo" width="200"/>
</div>

## Description du Projet

La **Plateforme de Tailleur en Ligne** est une application web conçue pour moderniser le secteur de la couture traditionnelle marocaine. Elle permet aux tailleurs locaux de vendre leurs vêtements traditionnels directement aux consommateurs, offrant ainsi une solution pour étendre leur marché et améliorer la visibilité de leurs produits.

## Contexte du Projet

Ce projet est réalisé dans le cadre de la formation chez ENAA, comme Projet Fil Rouge. L'objectif est de démontrer une maîtrise complète des compétences acquises au cours de la formation, en développant une application fonctionnelle, sécurisée et opérationnelle.

## Cahier des Charges

### 1. Contexte et Objectifs

**Contexte :**
- Moderniser la couture traditionnelle marocaine.
- Permettre aux tailleurs locaux de vendre leurs produits en ligne.
- Augmenter la visibilité des vêtements traditionnels.

**Objectifs :**
- Concevoir une plateforme en ligne pour la vente de vêtements traditionnels marocains.
- Fournir des outils de gestion des stocks, des commandes et de personnalisation des produits.
- Offrir une expérience d'achat en ligne sécurisée et conviviale.

### 2. Fonctionnalités Clés

- **Catalogue de Produits :** 
  - Large gamme de vêtements traditionnels.
  - Descriptions détaillées et vérification de l'authenticité.
  
- **Profils de Tailleurs :**
  - Pages dédiées pour chaque tailleur.
  - Gestion des stocks et des commandes en ligne.
  
- **Système de Commande :**
  - Panier d'achat et processus de commande.
  - Personnalisation des produits (tailles, tissus, couleurs).
  
- **Support Multi-langue et Livraison :**
  - Support multilingue pour un public international.
  - Options de livraison flexibles.
  
- **Support Évaluation et Feedback :**
  - Système de notation et commentaires.
  - Feedback pour améliorer la qualité des produits et services.
  
- **Programme de Fidélité :**
  - Points de fidélité, remises, offres spéciales.
  - Notifications personnalisées pour les clients réguliers.
  
- **Visualisation en Temps Réel :**
  - Outil de visualisation 3D.
  - Option d'avatar personnalisable.

### 3. Technologies Requises

- **Frontend :** Angular
- **Backend :** Spring Boot
- **Base de données :** MySQL

### 4. Planification du Projet

- **Configuration :** Mise en place de Jira/Trello pour la gestion des tâches.
- **Conception :** Diagrammes UML et maquettes front-end.
- **Développement :** Spring Boot pour le backend et Angular pour le frontend.
- **Sécurité :** Implémentation de Spring Security.
- **Tests :** Tests unitaires avec JUnit et Mockito.
- **Qualité du Code :** Analyse statique avec SonarQube.
- **Conteneurisation :** Docker pour le déploiement.
- **Intégration Continue :** Pipelines Jenkins.

### 5. Livrables

- **Documentation du Projet :** PDF, PPT, ou Canva.
- **Planification des Tâches :** Lien vers Jira/Trello.
- **Design Final :** Lien vers Figma/Adobe XD.
- **Repository GitHub :** 
  - README
  - Diagrammes UML (classes, use case, séquence)
  - Code source

### 6. Critères de Performance

- **Qualité du Code :** Clarté, performance, et documentation.
- **Performance Fonctionnelle :** Fiabilité et conformité.
- **Convivialité de l'Utilisateur :** Accessibilité et interface utilisateur.
- **Contrôle de Version Git :** Pratiques de versionnage cohérentes.
- **Compréhension des Concepts :** Application des concepts de formation.
- **Documentation :** Clarté et précision.

## Objectifs du Projet

1. **Élargir le Marché des Tailleurs :** Offrir une plateforme de vente en ligne.
2. **Augmenter la Visibilité des Produits :** Promouvoir les vêtements traditionnels.
3. **Simplifier la Gestion des Commandes :** Outils pour gestion des stocks et commandes.
4. **Promouvoir la Couture Traditionnelle :** Améliorer la visibilité internationale.

## État Actuel du Projet

Je suis actuellement dans la phase de **Backend**. En ce moment, je travaille précisément sur le **microservices**.

## Structure du projet

```bash
social-management-service/
│
├── src/
│   ├── main/
│   │   ├── java/com/socialmanagementservice/
│   │   │   ├── controller/
│   │   │   │   ├── FollowController.java
│   │   │   │   ├── WishlistController.java
│   │   │   │   └── FavoritesController.java
│   │   │   ├── service/
│   │   │   │   ├── FollowService.java
│   │   │   │   ├── WishlistService.java
│   │   │   │   └── FavoritesService.java
│   │   │   ├── model/
│   │   │   │   ├── Follow.java
│   │   │   │   ├── Wishlist.java
│   │   │   │   └── Favorites.java
│   │   │   ├── repository/
│   │   │   │   ├── FollowRepository.java
│   │   │   │   ├── WishlistRepository.java
│   │   │   │   └── FavoritesRepository.java
│   │   │   ├── dto/
│   │   │   │   ├── FollowDto.java
│   │   │   │   ├── WishlistDto.java
│   │   │   │   └── FavoritesDto.java
│   │   │   └── SocialManagementServiceApplication.java
│   └── test/
│       └── java/com/socialmanagementservice/
│           └── SocialManagementServiceApplicationTests.java
├── pom.xml
└── README.md
```

## Contact

Pour toute question ou demande d'informations supplémentaires, veuillez contacter : [yassineoularbi4@gmail.com]

