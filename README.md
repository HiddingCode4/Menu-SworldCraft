# Sworldcraft Menu — NeoForge 26.3

Menu principal personnalisé pour le modpack Sworldcraft.

## Installer

1. Utiliser Minecraft **26.3**, **NeoForge 26.3.0.102 exactement**, et Java 25.
2. Copier `sworldcraft_menu-neoforge-26.3-1.1.0.jar` dans le dossier `mods` de l’instance **cliente**.
3. Lancer le jeu : le menu apparaît automatiquement.

Aucune dépendance à Waystone, FancyMenu ou à une bibliothèque supplémentaire.
Le mod est uniquement côté client : ne pas l’ajouter au serveur dédié.
Ne pas combiner avec un autre mod qui remplace le menu principal.

## Contenu

- Décor illustré Minecraft avec monolithe cyan, logo PNG transparent.
- Boutons Solo, Multijoueur, Mods, Options, Quitter.
- Langue (L) et accessibilité (A), navigation clavier et narration des boutons.
- Bordures cyan au survol et au focus clavier ; son standard des boutons.
- Particules légères calculées selon le temps, sans vidéo ni shader requis.
- Les particules sont désactivées avec l’option d’accessibilité de réduction des éclairs.
- Textes Solo, Multijoueur, Options et Quitter traduits par Minecraft.
- Le fond est une image fixe : l’eau et le monolithe ne sont pas des objets 3D animés.

Le visuel reprend le style de la maquette, avec des éléments reconstruits séparément.
Les actions Solo et Multijoueur sont récupérées du menu standard pour conserver
les restrictions du compte, le mode démo et l’avertissement multijoueur.
Le menu pause et les autres écrans de Minecraft ne sont pas remplacés.

## Ouvrir dans VS Code et compiler (Windows)

Extraire le dossier complet avant de l’ouvrir dans VS Code. Installer un **JDK 25**
(par exemple Temurin 25) ; un simple Java Runtime n’est pas suffisant.
Dans le terminal PowerShell situé dans le dossier contenant `build.gradle` :

```powershell
.\gradlew.bat build
```

Le JAR à installer se trouve dans `build\libs\`.
La première compilation télécharge Gradle, NeoForge et les dépendances.
Ne pas copier le ZIP ni les sources dans `mods`.

Pour lancer une instance de développement :

```powershell
.\gradlew.bat runClient
```

Sous Linux / macOS : `chmod +x gradlew`, puis `./gradlew build` ou `./gradlew runClient`.

## Personnaliser

Ressources : `src/main/resources/assets/sworldcraft_menu/textures/gui/`

- `background.png` : décor (1672 × 941).
- `logo.png` : logo transparent (2172 × 724).

Les textures sont remplaçables par un pack de ressources sans modifier le code.
Conserver les proportions indiquées ; l’affichage couvre l’écran sans étirer le décor.
Les boutons sont dessinés dans `RunicButton.java` ; leur disposition et leurs actions
sont dans `RunicTitleScreen.java`.

## Compatibilité

Minecraft 26.3 uniquement, chargeur NeoForge. Pas Fabric, Forge ni une autre
version de Minecraft. Les mods du modpack restent des JAR séparés dans `mods`.

## Crédits

Projet Sworldcraft / Hidding. Illustrations créées avec un outil de génération d’images.
Structure Gradle issue du MDK NeoForge (voir `TEMPLATE_LICENSE.txt`).
