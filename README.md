# Chess Game

- A local 2-player chess application featuring a Graphical User Interface (GUI), complete move validation, and check/checkmate detection.
- Designed with future extensibility in mind (e.g., PGN file loading, custom chess bot, or remote multiplayer).

## Features

- **Full Chess Ruleset:** Supports all standard chess moves, including castling (kingside/queenside), pawn promotion, and *en passant*.
- **Game State Detection:** Real-time evaluation of check, checkmate, and stalemate after every move to ensure accurate game-ending conditions.
- **Move History & Undo/Redo:** Real-time logging of all played moves with instant Undo and Redo navigation buttons.
- **Captured Pieces Tracker:** Dynamic side-panel display using FlowPane layouts with custom piece styling and Unicode symbols for both White and Black.
- **Visual Move Indicators:** Clear UI highlights showing all legal target squares when selecting a piece.
- **Integrated Chess Clock:** Dual countdown timers for timed competitive play.
- **Polished UI & Controls:** Modern dark aesthetic featuring board glow effects, intuitive turn indicators, and quick-action buttons to reset or close the game.

## Tech Stack & Architecture

- **Language:** Java
- **UI Framework:** JavaFX
- **Build Tool:** Gradle
- **Version Control:** Git, GitHub

## Roadmap

- [x] Project setup (Gradle + JavaFX, GitHub Repo)
- [x] Board GUI: 8x8 grid rendering with starting piece layouts
- [x] Click Interaction: Piece selection and field targeting
- [x] Basic move validation rules per piece type
- [x] Piece capturing logic
- [x] Turn switching and UI status updates
- [x] Check detection
- [x] Checkmate & Stalemate detection (Game Over logic)
- [x] Castling implementation (Kingside / Queenside)
- [x] Pawn promotion implementation
- [x] *En passant* implementation
- [X] Small move History with Undo- and Redo- Buttons
- [X] Move History implemented in the UI
- [X] Buttons for reseting the round and closing the application
- [X] Indicators showing all possible moves
- [X] Chess clock implementation
- [X] Captured pieces lists
- [X] Basic Main Menu with game settings
- [X] Board rotate animation after each move
- [ ] *Optional / Planned:* PGN file parser, custom chess AI bot, remote multiplayer support

## Installation & Quick Start

1. Download the latest `Schachspiel_v1.0.zip` from the **Releases** section on GitHub.
2. Extract the ZIP archive to a folder of your choice on your computer.
3. Open the extracted folder and navigate to the `bin` subfolder.
4. Launch the game by double-clicking the `Schachspiel.bat` file.

> **Note for Windows Users:**
> Since this application is a private, unsigned project, Windows SmartScreen may display a security warning (*"Windows protected your PC"*).
>
> 1. Click on **"More info"**.
> 2. Click the **"Run anyway"** button.
> 3. *Note:* A console window will open alongside the game interface. Please keep this console window open in the background while playing, as closing it will terminate the game.

## Demo

![Game Start](DemoBilder/Spielstart.png)
![Checkmate by Black](DemoBilder/schwarzMatt.png)
![Pawn Promotion by White](DemoBilder/PromotionW.png)