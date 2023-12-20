# Projet de Classification d'Images - MLP pour MNIST

Ce projet vise à réaliser une classification d'images MNIST en utilisant un Multi-Layer Perceptron (MLP). L'objectif principal est de reconnaître les chiffres manuscrits présents dans la base de données MNIST.

## Prérequis

- Java installé sur votre machine
- Les données d'entraînement et de test au format MNIST dans le répertoire `donnees/`
- Les bibliothèques et dépendances nécessaires au projet

## Utilisation

### Chargement des données

Assurez-vous d'avoir les données MNIST d'entraînement et de test dans le format requis (`train-images-idx3-ubyte.gz`, `train-labels-idx1-ubyte.gz`, `t10k-images-idx3-ubyte.gz`, `t10k-labels-idx1-ubyte.gz`) et placez-les dans le dossier `donnees/`.

### Exécution du code

Exécutez le fichier `ClassificationImage.java` pour entraîner le MLP et tester la classification.

### Analyse des résultats

À la fin de l'exécution, le programme affichera le taux d'erreur et de réussite pour la classification des imagettes de test.

## Fonctionnalités principales

- **Entraînement du MLP**
  - Utilisation d'une architecture à couches multiples définie dans le tableau `nbNeurones`
  - Choix du nombre de passages d'entraînement avec `nbPassage`
  - Utilisation de la fonction d'activation Sigmoid pour le réseau ou TanH

- **Tests et évaluation**
  - Méthode `testReseau()` pour évaluer les performances du réseau sur les données de test

## Remarques supplémentaires

- Les images MNIST sont converties en tableaux de pixels normalisés pour l'entraînement du MLP.
- Les étiquettes des imagettes sont transformées en vecteurs correspondant aux réponses attendues du réseau.
