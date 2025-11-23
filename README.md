# Game of War

A configurable Java 21 implementation of the classic card game **War**. The project models cards, players, decks, and dealer logic, making it easy to simulate battles and wars with multiple players, custom deck sizes, and automated resolution rules.

Now featuring a **modern web interface** built with Spring Boot!

---

## Features
- 🎮 **Interactive Web UI** - Play the game in your browser with a beautiful, responsive interface
- 🖥️ **Command-Line Interface** - Traditional CLI gameplay for terminal enthusiasts
- ⚙️ **Highly Configurable** - Customize number of players, suits, and ranks (default: 2 players, 4 suits, 13 ranks)
- 🚀 **REST API** - Full RESTful API for game management and automation
- ☁️ **AWS-Ready** - Easy deployment to AWS Elastic Beanstalk, ECS, or App Runner
- 🐳 **Docker Support** - Containerized deployment with multi-stage builds
- ✅ **Comprehensive Tests** - JUnit 4 and Mockito test suite
- 🔧 **Gradle Build** - Modern build system with wrapper scripts

---

## 🚀 Quick Start - Run Locally

### Prerequisites
- **Java 21** installed ([Download here](https://adoptium.net/))
- No other dependencies needed - Gradle wrapper is included!

Verify your Java version:
```bash
java -version
# Should show "openjdk version 21" or similar
```

### Run the Web Application

**Windows:**
```bash
.\gradlew.bat bootRun
```

**macOS/Linux:**
```bash
./gradlew bootRun
```

**Then open your browser to:**
```
http://localhost:5000
```

That's it! 🎉 The web interface will load with an interactive game board.

> **First run will take 1-2 minutes** while Gradle downloads dependencies. Subsequent runs start in seconds.

### Want to deploy to AWS?
See **[deploy-aws.md](deploy-aws.md)** for complete AWS deployment instructions (Elastic Beanstalk, ECS, App Runner, and more).

### GitHub Actions
Automated CI (`.github/workflows/ci.yml`) builds and tests on every push. See **[GITHUB-ACTIONS.md](GITHUB-ACTIONS.md)** for:
- CI setup (automatic, no config needed)
- AWS deployment via `aws-deploy.yml` (manual only, requires setup)
- Required secrets and cost estimates

### Troubleshooting

**Port 5000 already in use?**

Change the port in `src/main/resources/application.properties`:
```properties
server.port=8080
```
Then visit `http://localhost:8080` instead.

**Build failing?**
```bash
# Clean and rebuild
.\gradlew.bat clean build
```

**Need help?** See [QUICKSTART.md](QUICKSTART.md) for detailed troubleshooting.

---

## Alternative: Command-Line Interface

Want the classic CLI experience? See the [Running the Command-Line Game](#running-the-command-line-game) section below.

---

## 🛠️ Development Commands

### Common Gradle Tasks

| Task | Windows | macOS / Linux | Description |
|------|---------|---------------|-------------|
| **🚀 Run Web App** | `.\gradlew.bat bootRun` | `./gradlew bootRun` | **Start web server on port 5000** |
| Build | `.\gradlew.bat build` | `./gradlew build` | Compile, test, and create JAR |
| Test | `.\gradlew.bat test` | `./gradlew test` | Run all tests |
| Clean | `.\gradlew.bat clean` | `./gradlew clean` | Remove build artifacts |
| Build JAR | `.\gradlew.bat build -x test` | `./gradlew build -x test` | Build without running tests |
| CLI Fat JAR | `.\gradlew.bat fatJar` | `./gradlew fatJar` | Create standalone CLI JAR |

### Run Production JAR

After building, you can run the production JAR:
```bash
.\gradlew.bat build
java -jar build/libs/gameOfWar-1.0-SNAPSHOT.jar
```

Then visit `http://localhost:5000`

---

## Web Interface

The web interface provides a modern, interactive way to play Game of War.

### Features
- 🎨 Beautiful, responsive design that works on desktop and mobile
- ⚡ Real-time game updates
- 🎴 Visual playing card images (real card graphics!)
- 📊 Round history tracking
- 🤖 Auto-play mode for automatic game progression
- ⚙️ Configurable game parameters (players, suits, ranks)

### Starting the Web Server

```bash
# Development mode (with hot reload)
.\gradlew.bat bootRun    # Windows
./gradlew bootRun        # macOS/Linux

# Production mode (executable JAR)
.\gradlew.bat build
java -jar build/libs/gameOfWar-1.0-SNAPSHOT.jar
```

The server starts on **port 5000** by default. Visit `http://localhost:5000` in your browser.

### REST API

The application exposes a RESTful API for game management:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/games` | POST | Create a new game |
| `/api/games` | GET | List all active games |
| `/api/games/{id}` | GET | Get game state |
| `/api/games/{id}/play-round` | POST | Play a single round |
| `/api/games/{id}` | DELETE | Delete a game |
| `/actuator/health` | GET | Health check endpoint |

#### Example API Usage

```bash
# Create a new game
curl -X POST http://localhost:5000/api/games \
  -H "Content-Type: application/json" \
  -d '{"numberOfPlayers": 2, "numberOfSuits": 4, "numberOfRanks": 13}'

# Play a round
curl -X POST http://localhost:5000/api/games/{gameId}/play-round

# Get game state
curl http://localhost:5000/api/games/{gameId}
```

---

## Running the Command-Line Game

The traditional command-line interface is still available. After building, you can run the game from compiled classes or from the fat JAR.

### From Compiled Classes
```bash
java -cp build/classes/java/main com.groovin101.gow.War [numberOfPlayers numberOfSuits numberOfRanks [-e]]
```
Example:
```bash
java -cp build/classes/java/main com.groovin101.gow.War 3 4 13 -e
```

### From the CLI Fat JAR
First, build the CLI-specific JAR:
```bash
.\gradlew.bat fatJar    # Windows
./gradlew fatJar        # macOS/Linux
```

Then run it:
```bash
java -jar build/libs/gameOfWar-cli-all.jar [numberOfPlayers numberOfSuits numberOfRanks [-e]]
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
 ├── main/
 │    ├── java/com/groovin101/gow/
 │    │    ├── model/             # Card, Deck, Player, Dealer, etc.
 │    │    ├── exception/         # Custom exception hierarchy
 │    │    ├── web/
 │    │    │    ├── controller/  # REST API controllers
 │    │    │    ├── service/     # Game service layer
 │    │    │    └── dto/         # Data transfer objects
 │    │    ├── War.java          # CLI game orchestrator
 │    │    └── GameOfWarApplication.java  # Spring Boot entry point
 │    └── resources/
 │         ├── static/           # Web frontend (HTML, CSS, JS)
 │         └── application.properties
 └── test/java/com/groovin101/gow/
      ├── model/        # Unit tests for domain objects
      ├── test/utils/   # Shared test fixtures (BaseTest)
      └── WarTest.java  # Game-level behavior tests
```

---

## ☁️ Deployment

### GitHub Actions CI/CD

✅ **CI (`.github/workflows/ci.yml`)** - Automatic builds and tests (no setup)

🚀 **AWS Deploy (`.github/workflows/aws-deploy.yml`)** - Manual deployment (requires setup)

📖 See **[GITHUB-ACTIONS.md](GITHUB-ACTIONS.md)** for full instructions

### Deploy to AWS

This application is **production-ready** and can be deployed to AWS in multiple ways.

📖 **See [deploy-aws.md](deploy-aws.md) for complete deployment instructions**, including:
- AWS Elastic Beanstalk (recommended - easiest option)
- AWS ECS with Docker (containerized deployment)
- AWS App Runner (fully managed containers)
- Cost estimates and comparisons

### Docker Deployment

Run locally with Docker:
```bash
docker build -t game-of-war .
docker run -p 5000:5000 game-of-war
```

Then visit `http://localhost:5000`

### Health Check Endpoint

The application exposes a health check endpoint for AWS load balancers and monitoring:
```
GET http://localhost:5000/actuator/health
```

---

## Technology Stack

- **Language:** Java 21
- **Web Framework:** Spring Boot 3.4.0
- **Build Tool:** Gradle 9.2.1
- **Frontend:** HTML5, CSS3, Vanilla JavaScript
- **Testing:** JUnit 4.13.2, Mockito 5.11.0
- **Deployment:** Docker, AWS Elastic Beanstalk, GitHub Actions

---

## Architecture

### Game Flow
1. **CLI Mode:** Direct instantiation of `War` class with command-line args
2. **Web Mode:** REST API → `GameService` → `War` instance → JSON response

### Design Patterns
- **Service Layer Pattern:** `GameService` manages game instances
- **DTO Pattern:** Clean API contracts with data transfer objects
- **Repository Pattern:** In-memory game storage with `ConcurrentHashMap`
- **Rule Chain Pattern:** Extensible game rules (defined, ready for implementation)

---
