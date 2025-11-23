# 🎯 Next Steps - Getting Your Web Interface Running

## Immediate Actions (Do This Now!)

### 1️⃣ Start the Application

Open a terminal in the project directory and run:

**Windows:**
```bash
.\gradlew.bat bootRun
```

**macOS/Linux:**
```bash
./gradlew bootRun
```

**What happens:**
- Gradle downloads Spring Boot dependencies (first time only, ~2-3 minutes)
- Application compiles
- Server starts on port 5000
- You'll see: `Started GameOfWarApplication in X.XXX seconds`

### 2️⃣ Open Your Browser

Navigate to:
```
http://localhost:5000
```

### 3️⃣ Play the Game!

You should see a beautiful web interface with:
- Game setup form
- Player configuration options
- Start button

**Try it:**
1. Keep default settings (2 players, 4 suits, 13 ranks)
2. Click "Start New Game"
3. Click "Play Round" a few times
4. Try "Auto Play" to watch the game unfold

---

## Quick Troubleshooting

### "Command not found: gradlew"

The Gradle wrapper is already in your project. Make sure you're in the project root directory:

```bash
cd C:\Users\fuzza\dev\game-of-war
```

### "Port 5000 is already in use"

Something else is using port 5000. Either:

**Option A - Use a different port:**
1. Edit `src/main/resources/application.properties`
2. Change `server.port=5000` to `server.port=8080`
3. Restart the server
4. Visit `http://localhost:8080`

**Option B - Find and stop the process using port 5000:**
```bash
# Windows PowerShell
netstat -ano | findstr :5000
taskkill /PID <PID> /F

# Linux/macOS
lsof -ti:5000 | xargs kill
```

### "Java version error"

Make sure you have Java 21 installed:

```bash
java -version
```

Should show `openjdk version "21"` or similar.

If not, download from: https://adoptium.net/

---

## Understanding What You Built

### Files You Can Customize

**Frontend (No Java knowledge needed!):**
- `src/main/resources/static/index.html` - Page structure
- `src/main/resources/static/styles.css` - Visual styling
- `src/main/resources/static/app.js` - Game logic and behavior

**Backend (Java):**
- `src/main/java/com/groovin101/gow/web/controller/GameController.java` - API endpoints
- `src/main/java/com/groovin101/gow/web/service/GameService.java` - Business logic

**Configuration:**
- `src/main/resources/application.properties` - Server settings

---

## Quick Customizations

### Change the Title
Edit `src/main/resources/static/index.html`:
```html
<h1>🎴 Game of War</h1>
<!-- Change to: -->
<h1>🎴 My Custom War Game!</h1>
```

### Change Colors
Edit `src/main/resources/static/styles.css`:
```css
:root {
    --primary-color: #2563eb;  /* Change this blue */
    --success-color: #10b981;  /* Change this green */
}
```

### Change Auto-Play Speed
Edit `src/main/resources/static/app.js`:
```javascript
autoPlayInterval = setInterval(async () => {
    await playRound();
}, 1500); // Change 1500 to 500 for faster, 3000 for slower
```

Refresh your browser to see changes (no need to restart server for frontend changes).

---

## Testing the API Directly

You can interact with the game via API calls:

### Create a Game
```bash
curl -X POST http://localhost:5000/api/games ^
  -H "Content-Type: application/json" ^
  -d "{\"numberOfPlayers\": 2, \"numberOfSuits\": 4, \"numberOfRanks\": 13}"
```

You'll get back JSON with a `gameId`.

### Play a Round
```bash
curl -X POST http://localhost:5000/api/games/{GAME_ID_HERE}/play-round
```

### Get Game State
```bash
curl http://localhost:5000/api/games/{GAME_ID_HERE}
```

---

## GitHub Actions

### ✅ CI (Automatic) - `.github/workflows/ci.yml`
Builds and tests on every push. No setup required.

### 🚀 AWS Deploy (Manual) - `.github/workflows/aws-deploy.yml`
Only runs when manually triggered. Requires setup. See **[GITHUB-ACTIONS.md](GITHUB-ACTIONS.md)**.

---

## Deploying to AWS (Optional)

Once you're happy with the local version, deploy to AWS:

### Quick Deploy to Elastic Beanstalk

1. **Build the application:**
   ```bash
   .\gradlew.bat build
   ```

2. **Install AWS EB CLI:**
   ```bash
   pip install awsebcli
   ```

3. **Initialize and deploy:**
   ```bash
   eb init -p "Corretto 21" game-of-war --region us-east-1
   eb create game-of-war-env
   ```

4. **Open in browser:**
   ```bash
   eb open
   ```

Your game is now live on the internet! 🌐

See `deploy-aws.md` for detailed deployment instructions and other AWS options.

---

## Common Use Cases

### 1. Show it to Friends/Colleagues

Start the server and share: `http://your-ip-address:5000`

To find your IP:
```bash
# Windows
ipconfig

# macOS/Linux
ifconfig
```

Make sure your firewall allows incoming connections on port 5000.

### 2. Run Multiple Games

The API supports multiple simultaneous games. Each call to `/api/games` creates a new game with a unique ID.

### 3. Integrate with Other Apps

Use the REST API to integrate Game of War into other applications:
- Mobile apps
- Discord bots
- Slack integrations
- Analytics dashboards

---

## Development Workflow

### Making Changes

1. **Frontend changes** (HTML/CSS/JS):
   - Edit files in `src/main/resources/static/`
   - Refresh browser (no restart needed)

2. **Backend changes** (Java):
   - Edit files in `src/main/java/`
   - Stop server (Ctrl+C)
   - Run `.\gradlew.bat bootRun` again

3. **Test everything:**
   ```bash
   .\gradlew.bat test
   ```

### Version Control

Don't forget to commit your changes:

```bash
git add .
git commit -m "Added web interface for Game of War"
git push origin web-interface
```

---

## Learning More

### Documentation Files

- **README.md** - Complete project documentation
- **QUICKSTART.md** - Quick start guide for end users
- **WEB-INTERFACE-SUMMARY.md** - Technical summary of what was built
- **deploy-aws.md** - AWS deployment options and instructions

### Spring Boot Resources

- Official Guide: https://spring.io/guides/gs/spring-boot/
- REST API Guide: https://spring.io/guides/gs/rest-service/
- Actuator Guide: https://spring.io/guides/gs/actuator-service/

### JavaScript/Frontend

- Modern JavaScript: https://javascript.info/
- Fetch API: https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
- CSS Grid/Flexbox: https://css-tricks.com/snippets/css/complete-guide-grid/

---

## Getting Help

If you run into issues:

1. **Check the console/terminal** for error messages
2. **Check browser console** (F12 → Console tab) for frontend errors
3. **Verify Java version**: `java -version` (should be 21)
4. **Try clean build**: `.\gradlew.bat clean build`
5. **Check port availability**: `netstat -ano | findstr :5000`

Common fixes:
- Restart the server
- Clear browser cache
- Check firewall settings
- Verify all files were created correctly

---

## What You've Accomplished 🎉

You now have:

✅ A modern web application for Game of War  
✅ Beautiful, responsive UI that works on any device  
✅ Full REST API for game management  
✅ Production-ready Spring Boot application  
✅ Docker containerization support  
✅ AWS deployment configuration  
✅ CI/CD pipeline with GitHub Actions  
✅ Health check endpoints for load balancers  
✅ Comprehensive documentation  

**All in ~2000 lines of production code!**

---

## Celebrate! 🎊

You've successfully transformed a command-line card game into a modern web application. This is a significant achievement that demonstrates:

- Full-stack development (frontend + backend)
- Enterprise Java (Spring Boot)
- REST API design
- Modern web UI
- Cloud deployment readiness
- DevOps practices (Docker, CI/CD)

**Now go play some cards!** 🎴

Visit: **http://localhost:5000**

---

Happy coding! 🚀

