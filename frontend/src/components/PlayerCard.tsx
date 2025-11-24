import type { Player, GameState } from '../types/game.types';
import { WarReveal } from './WarReveal';

interface PlayerCardProps {
  player: Player;
  gameState: GameState;
  autoRevealWar: boolean;
}

export function PlayerCard({ player, gameState, autoRevealWar }: PlayerCardProps) {
  const isEliminated = player.cardCount === 0;
  
  // Check if this player is the game winner
  const isGameWinner = gameState.gameOver && gameState.winner?.name === player.name;
  
  // Check if this player won the most recent round
  const lastRound = gameState.roundHistory.length > 0 
    ? gameState.roundHistory[gameState.roundHistory.length - 1]
    : null;
  const isRoundWinner = lastRound?.winnerName === player.name;
  
  const isWinner = isGameWinner || isRoundWinner;

  const roundWinnerClass = isRoundWinner && !gameState.gameOver ? 'round-winner' : '';
  
  return (
    <div className={`player-card ${isEliminated ? 'eliminated' : ''} ${isWinner ? 'winner' : ''} ${roundWinnerClass}`}>
      <div className="player-name">{player.name}</div>
      <div className="player-card-count">Cards: {player.cardCount}</div>
      <WarReveal player={player} autoReveal={autoRevealWar} />
    </div>
  );
}

