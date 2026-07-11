# Schachspiel:

- Lokales 2-Spieler-Schachspiel mit grafischer Oberfläche, vollständiger Zugvalidierung und Schach-/Schachmatt-Erkennung.
- Später erweiterbar, um das Laden von Partien aus dem PGN-Format, einem eigenen Schach-Bot und/oder einem Remote Multiplayer.

# Roadmap:

- [X] Projekt aufsetzen (Gradle + JavaFX, GitHub Repo)
- [X] Brett-GUI: 8x8-Raster mit Figuren in Startposition
- [X] Klick-Logik: Figur + Zielfeld auswählen, Figur bewegt sich (noch ohne Regeln)
- [X] Grundlegende Zugregeln pro Figurentyp
- [X] Schlagen von Figuren
- [X] Spielerwechsel + Anzeige
- [X] Schach-Erkennung
- [X] Schachmatt- und Patt- Erkennung + Spielende
- [X] Rochade implementieren
- [ ] Später: Promotion
- [ ] Optional: Laden von Spielen aus dem PGN-Format, eigener Schach-Bot, Remote Multiplayer


# Wichtige Bibliotheken:

- JavaFX: Für die grafische Oberfläche und die Felderlogik