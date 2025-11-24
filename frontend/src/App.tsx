import { useState, useRef, useCallback, useEffect } from 'react';
import type { GameState } from './types/game.types';
import { createGame, playRound as apiPlayRound, GameApiError } from './services/gameApi';
import { GameSetup } from './components/GameSetup';
import { GameBoard } from './components/GameBoard';
import { RoundHistory } from './components/RoundHistory';
import { WinnerModal } from './components/WinnerModal';
import './App.css';

type AutoPlaySpeed = 'slow' | 'medium' | 'ludicrous';

function App() {
  const [gameState, setGameState] = useState<GameState | null>(null);
  const [isCreating, setIsCreating] = useState(false);
  const [isPlaying, setIsPlaying] = useState(false);
  const [isAutoPlaying, setIsAutoPlaying] = useState(false);
  const [autoPlaySpeed, setAutoPlaySpeed] = useState<AutoPlaySpeed>('medium');
  const [autoRevealWar, setAutoRevealWar] = useState(false);
  
  // Use refs to access latest values in auto-play callbacks
  const autoPlaySpeedRef = useRef<AutoPlaySpeed>(autoPlaySpeed);
  const autoPlayTimeoutRef = useRef<number | null>(null);
  const isAutoPlayingRef = useRef<boolean>(false);
  
  // Keep refs in sync with state
  useEffect(() => {
    autoPlaySpeedRef.current = autoPlaySpeed;
  }, [autoPlaySpeed]);

  useEffect(() => {
    isAutoPlayingRef.current = isAutoPlaying;
  }, [isAutoPlaying]);

  // Cleanup on unmount
  useEffect(() => {
    return () => {
      if (autoPlayTimeoutRef.current) {
        clearTimeout(autoPlayTimeoutRef.current);
      }
    };
  }, []);

  const handleCreateGame = useCallback(async (players: number, suits: number, ranks: number) => {
    setIsCreating(true);
    try {
      const newGameState = await createGame({
        numberOfPlayers: players,
        numberOfSuits: suits,
        numberOfRanks: ranks,
      });
      setGameState(newGameState);
    } catch (error) {
      console.error('Error creating game:', error);
      alert('Failed to create game. Please try again.');
    } finally {
      setIsCreating(false);
    }
  }, []);

  const getSpeedDelay = useCallback((speed: AutoPlaySpeed, isWar: boolean): number => {
    const baseDelays = {
      slow: 2000,
      medium: 1000,
      ludicrous: 200,
    };
    
    const warDelays = {
      slow: 5000,
      medium: 3700,
      ludicrous: 2500,
    };
    
    return isWar ? warDelays[speed] : baseDelays[speed];
  }, []);

  const playRoundWithDelay = useCallback(async (currentGameState: GameState): Promise<GameState> => {
    if (!currentGameState?.gameId) {
      throw new Error('No active game');
    }

    const newState = await apiPlayRound(currentGameState.gameId);
    setGameState(newState);

    // Check if this round was a war
    const wasWar = newState.roundHistory.length > 0 && 
                   newState.roundHistory[newState.roundHistory.length - 1].wasWar;

    if (wasWar) {
      // Trigger auto-reveal for war cards
      setAutoRevealWar(true);
      
      // Wait for war animation with longer delay
      const currentSpeed = autoPlaySpeedRef.current;
      const warDelay = getSpeedDelay(currentSpeed, true);
      
      await new Promise(resolve => setTimeout(resolve, warDelay));
      
      // Turn off auto-reveal for next round
      setAutoRevealWar(false);
    } else {
      // Normal round delay
      const currentSpeed = autoPlaySpeedRef.current;
      const normalDelay = getSpeedDelay(currentSpeed, false);
      await new Promise(resolve => setTimeout(resolve, normalDelay));
    }

    return newState;
  }, [getSpeedDelay]);

  const scheduleNextRound = useCallback((currentGameState: GameState) => {
    // Check if auto-play is still active
    if (!isAutoPlayingRef.current) {
      return;
    }

    if (currentGameState.gameOver) {
      setIsAutoPlaying(false);
      setAutoRevealWar(false);
      return;
    }

    autoPlayTimeoutRef.current = setTimeout(async () => {
      // Double-check auto-play is still active before playing
      if (!isAutoPlayingRef.current) {
        return;
      }

      try {
        const newState = await playRoundWithDelay(currentGameState);
        scheduleNextRound(newState);
      } catch (error) {
        console.error('Error in auto-play:', error);
        setIsAutoPlaying(false);
        setAutoRevealWar(false);
        
        if (error instanceof GameApiError && error.status && error.status >= 500) {
          alert('Server error during auto-play. The server may have shut down. Please start a new game.');
        } else {
          alert('Failed to play round during auto-play. Stopping auto-play.');
        }
      }
    }, 100);
  }, [playRoundWithDelay]);

  const handlePlayRound = useCallback(async () => {
    if (!gameState?.gameId || isPlaying) return;

    setIsPlaying(true);
    try {
      const newState = await apiPlayRound(gameState.gameId);
      setGameState(newState);
    } catch (error) {
      console.error('Error playing round:', error);
      alert('Failed to play round. Please try again.');
    } finally {
      setIsPlaying(false);
    }
  }, [gameState, isPlaying]);

  const handleToggleAutoPlay = useCallback(() => {
    if (isAutoPlaying) {
      // Stop auto-play
      isAutoPlayingRef.current = false;
      if (autoPlayTimeoutRef.current) {
        clearTimeout(autoPlayTimeoutRef.current);
        autoPlayTimeoutRef.current = null;
      }
      setIsAutoPlaying(false);
      setAutoRevealWar(false);
    } else {
      // Start auto-play
      if (!gameState || gameState.gameOver) return;
      
      isAutoPlayingRef.current = true;
      setIsAutoPlaying(true);
      scheduleNextRound(gameState);
    }
  }, [isAutoPlaying, gameState, scheduleNextRound]);

  const handleNewGame = useCallback(() => {
    isAutoPlayingRef.current = false;
    if (autoPlayTimeoutRef.current) {
      clearTimeout(autoPlayTimeoutRef.current);
      autoPlayTimeoutRef.current = null;
    }
    setGameState(null);
    setIsAutoPlaying(false);
    setAutoRevealWar(false);
  }, []);

  if (!gameState) {
    return (
      <div className="container">
        <header>
          <h1>🎴 Game of War 🎴</h1>
        </header>
        <main>
          <GameSetup onStartGame={handleCreateGame} isCreating={isCreating} />
        </main>
      </div>
    );
  }

  return (
    <div className="container">
      <header>
        <h1>🎴 Game of War 🎴</h1>
      </header>
      <main>
        <div id="game-section" className="section">
          <div className="game-controls">
            <button
              className="btn btn-primary"
              onClick={handlePlayRound}
              disabled={isPlaying || isAutoPlaying || gameState.gameOver}
            >
              Play Round
            </button>
            <button
              className={`btn ${isAutoPlaying ? 'btn-primary' : 'btn-secondary'}`}
              onClick={handleToggleAutoPlay}
              disabled={gameState.gameOver}
            >
              {isAutoPlaying ? 'Stop Auto Play' : 'Auto Play'}
            </button>
            <div className="auto-play-speed">
              <label htmlFor="speed-select">Speed:</label>
              <select
                id="speed-select"
                value={autoPlaySpeed}
                onChange={(e) => setAutoPlaySpeed(e.target.value as AutoPlaySpeed)}
              >
                <option value="slow">Slow</option>
                <option value="medium">Medium</option>
                <option value="ludicrous">Ludicrous</option>
              </select>
            </div>
            <button
              className="btn btn-secondary"
              onClick={handleNewGame}
            >
              New Game
            </button>
          </div>

          <GameBoard gameState={gameState} autoRevealWar={autoRevealWar} />

          <div className="history-section">
            <h3>Round History</h3>
            <RoundHistory history={gameState.roundHistory} />
          </div>
        </div>

        {gameState.gameOver && <WinnerModal gameState={gameState} onClose={handleNewGame} />}
      </main>
    </div>
  );
}

export default App;
