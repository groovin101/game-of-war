# Quick Start Guide - Game of War Web Interface

## 🚀 Get Started in 3 Steps

### Step 1: Verify Java Installation
Make sure you have Java 21 installed:

```bash
java -version
```

You should see something like `openjdk version "21..."` or similar.

### Step 2: Start the Web Server

**Windows:**
```bash
.\start-web.bat
```

**macOS/Linux:**
```bash
chmod +x start-web.sh
./start-web.sh
```

**Or use Gradle directly:**
```bash
.\gradlew.bat bootRun    # Windows
./gradlew bootRun        # macOS/Linux
```

### Step 3: Open Your Browser

Navigate to: **http://localhost:5000**

The web interface will load automatically!

---

## 🎮 How to Play

1. **Configure the game:**
   - Number of Players (2-10)
   - Number of Suits (1-8)
   - Number of Ranks (2-20)

2. **Start the game:**
   - Click "Start New Game"

3. **Play rounds:**
   - Click "Play Round" to play one round at a time
   - Or click "Auto Play" to watch the game unfold automatically

4. **Watch the action:**
   - See cards being played in real-time
   - Track player card counts
   - View round history
   - Get notified when WAR happens! 🎴

---

## 🔧 Troubleshooting

### Port 5000 Already in Use

If you get an error about port 5000 being in use, you can change the port:

1. Edit `src/main/resources/application.properties`
2. Change `server.port=5000` to another port (e.g., `server.port=8080`)
3. Restart the server

### Gradle Build Fails

Try cleaning and rebuilding:

```bash
.\gradlew.bat clean build    # Windows
./gradlew clean build        # macOS/Linux
```

### Java Version Error

This application requires Java 21. If you have an older version:

1. Download Java 21 from [Adoptium](https://adoptium.net/)
2. Install it
3. Set `JAVA_HOME` to point to your Java 21 installation
4. Restart your terminal and try again

### Browser Shows "Cannot Connect"

1. Make sure the server is running (check the terminal for errors)
2. Try accessing `http://localhost:5000/actuator/health` - you should see `{"status":"UP"}`
3. If that works, clear your browser cache and try again

---

## 📚 Next Steps

- **REST API Documentation:** See [README.md](README.md#rest-api) for API endpoints
- **Deploy to AWS:** See [deploy-aws.md](deploy-aws.md) for deployment instructions
- **Command-Line Mode:** See [README.md](README.md#running-the-command-line-game) for CLI usage
- **Customize the UI:** Edit files in `src/main/resources/static/`

---

## 💡 Tips

- **Auto Play Speed:** The auto-play feature runs at 1.5 seconds per round. Edit `app.js` to change the speed.
- **Game Configuration:** Different configurations create wildly different game lengths!
  - Fewer ranks = shorter games
  - More players = longer games
  - Standard deck (2 players, 4 suits, 13 ranks) typically runs 100-1000 rounds
- **Watching WAR:** Pay attention to the round history - WAR battles are marked with a special badge!

---

## 🎯 Features

- ✅ Beautiful, responsive UI
- ✅ Real-time game updates
- ✅ Auto-play mode
- ✅ Round history tracking
- ✅ Visual card display with suit symbols (♠ ♥ ♦ ♣)
- ✅ Player elimination tracking
- ✅ Draw detection
- ✅ Mobile-friendly design

---

Enjoy playing Game of War! 🎴🎉

