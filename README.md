# Game of War

A configurable Java 21 implementation of the classic card game **War**. The project models cards, players, decks, and dealer logic, making it easy to simulate battles and wars with multiple players, custom deck sizes, and automated resolution rules.

---

## Features
- Configurable number of players, suits, and ranks (default: 2 players, 4 suits, 13 ranks).
- Custom exception hierarchy for argument validation and game initialization failures.
- Detailed simulation of battle and war rounds, including tie handling and draw detection.
- Comprehensive test suite powered by JUnit 4 and Mockito.
- Gradle-based build with wrapper scripts for reproducible tooling.

---

## Requirements
- **Java 21** (ensure `JAVA_HOME` points to a JDK 21 installation)
- **Gradle Wrapper** (bundled in repo) – no standalone Gradle install required

To verify your Java version:
```bash
java -version
```

---

## Getting Started

Clone the repository and move into the project directory:
```bash
git clone <repo-url>
cd game-of-war
```

### Common Gradle Tasks
| Task | Windows | macOS / Linux | Description |
|------|---------|---------------|-------------|
| Build | `.\gradlew.bat build` | `./gradlew build` | Compiles code, runs tests, produces JAR |
| Test | `.\gradlew.bat test` | `./gradlew test` | Runs the full JUnit suite |
| Clean | `.\gradlew.bat clean` | `./gradlew clean` | Removes `build/` artifacts |
| Skip tests | `.\gradlew.bat build -x test` | `./gradlew build -x test` | Build without executing tests |
| Fat JAR | `.\gradlew.bat fatJar` | `./gradlew fatJar` | Creates runnable `build/libs/gameOfWar-all.jar` |

> Tests are configured to run on every build unless you explicitly exclude them (e.g., using `-x test`).

---

## Running the Game

After building, you can run the game straight from compiled classes or from the fat JAR.

### From Compiled Classes
```bash
java -cp build/classes/java/main com.groovin101.gow.War [numberOfPlayers numberOfSuits numberOfRanks [-e]]
```
Example:
```bash
java -cp build/classes/java/main com.groovin101.gow.War 3 4 13 -e
```

### From the Fat JAR
```bash
java -jar build/libs/gameOfWar-all.jar [numberOfPlayers numberOfSuits numberOfRanks [-e]]
```

- `-e` enables verbose exception output (otherwise user-friendly messages are printed).
- If no arguments are given, the game defaults to `2 players / 4 suits / 13 ranks`.

### Command-Line Arguments
The launcher accepts up to four arguments in this order:

| Argument | Required | Type | Allowed Range | Notes |
|----------|----------|------|---------------|-------|
| `numberOfPlayers` | yes | integer | `2` – *deck size* | Must not exceed total cards (suits × ranks). |
| `numberOfSuits`   | yes | integer | `1` – `4` | Determines suit variety in the deck. |
| `numberOfRanks`   | yes | integer | `1` – `13` | Determines rank variety (Ace high). |
| `-e`              | optional | flag | N/A | Enables verbose exception reporting. Must appear after the numeric args. |

Examples:
```bash
# Standard 2-player war with full deck, verbose errors
java -jar build/libs/gameOfWar-all.jar 2 4 13 -e

# Three players, two suits, five ranks (30-card deck)
java -cp build/classes/java/main com.groovin101.gow.War 3 2 5
```

Validation rules implemented in `InputArguments` ensure:
- All numeric parameters are valid integers (spaces or surrounding quotes are tolerated).
- Suits/ranks stay within the configured bounds.
- Player count never exceeds the total number of cards available.
- `--help`, `--usage`, or `-usage` prints the table above without starting a game.
- Legacy positional syntax (`players suits ranks`) is still supported but optional.

---

## Gameplay Rules

This project follows the traditional War card game mechanics with a few configurable twists:

1. **Setup**
   - A deck is created using the specified number of suits × ranks (defaults to a standard 52-card French deck).
   - Cards are shuffled and dealt evenly to all players; any remainder stays in the dealer queue.

2. **Battle Phase**
   - Each player reveals the top card of their deck (`battle()` in code).
   - The highest ranked card wins the round and collects all cards played.
   - Ranks ascend from Two → Ace; suits are irrelevant for ranking.

3. **War Phase (Tie Handling)**
   - If the highest cards tie and at least one player still has cards, a *war* is triggered.
   - In a war, each tied player plays three additional cards (`war()`), with the third card acting as the new “significant” card.
   - If another tie occurs, wars continue recursively until a single winner emerges or all tied players run out of cards.

4. **Spoils Distribution**
   - All cards played during the battle/war are shuffled and appended to the winner’s deck bottom (`divySpoilsToWinner`).
   - Winners are tracked so the game can report the last hand and final victor.

5. **Player Elimination & Draws**
   - Players with zero cards are removed from active play.
   - If no player can continue (i.e., everyone is out of cards during a war), the game is flagged as a draw.

6. **Victory Conditions**
   - The game ends when one player holds every card in the configured deck or a draw is declared.
   - The CLI prints the winner (or draw) and the number of rounds played.

> Tip: You can enable verbose output in `War.play()` to observe per-round logging, or hook into the `Player`/`Deck` classes to build custom visualizations.

---

## Project Layout
```
src/
 ├── main/java/com/groovin101/gow/
 │    ├── model/        # Card, Deck, Player, Dealer, etc.
 │    ├── exception/    # Custom exception hierarchy
 │    └── War.java      # Main game orchestrator
 └── test/java/com/groovin101/gow/
      ├── model/        # Unit tests for domain objects
      ├── test/utils/   # Shared test fixtures (BaseTest)
      └── WarTest.java  # Game-level behavior tests
```
