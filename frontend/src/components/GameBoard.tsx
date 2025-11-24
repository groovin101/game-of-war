import type { GameState } from '../types/game.types';
import { PlayerCard } from './PlayerCard';

interface GameBoardProps {
  gameState: GameState;
  autoRevealWar: boolean;
}

export function GameBoard({ gameState, autoRevealWar }: GameBoardProps) {
  return (
    <div className="game-info">
      <div className="game-stats">
        <div className="stat">
          <span className="stat-label">Round:</span>
          <span className="stat-value">{gameState.roundNumber}</span>
        </div>
        <div className="stat">
          <span className="stat-label">Status:</span>
          <span className="stat-value">{gameState.status}</span>
        </div>
      </div>
      
      <div id="players-container">
        {gameState.players.map((player) => (
          <PlayerCard
            key={player.name}
            player={player}
            gameState={gameState}
            autoRevealWar={autoRevealWar}
          />
        ))}
      </div>
    </div>
  );
}

