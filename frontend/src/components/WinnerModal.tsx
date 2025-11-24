import type { GameState } from '../types/game.types';

interface WinnerModalProps {
  gameState: GameState;
  onClose: () => void;
}

export function WinnerModal({ gameState, onClose }: WinnerModalProps) {
  if (!gameState.gameOver) {
    return null;
  }

  let message: string;
  if (gameState.isDraw) {
    message = 'The game ended in a draw!';
  } else if (gameState.winner) {
    message = `${gameState.winner.name} wins the game!`;
  } else {
    message = 'Game Over!';
  }

  return (
    <div id="winner-modal" className="modal">
      <div className="modal-content">
        <h2>🎉 Game Over! 🎉</h2>
        <p id="winner-message">{message}</p>
        <button className="btn btn-primary" onClick={onClose}>
          New Game
        </button>
      </div>
    </div>
  );
}

