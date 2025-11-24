import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from './App';
import type { GameState } from './types/game.types';

// Mock the game API
vi.mock('./services/gameApi', () => ({
  createGame: vi.fn(),
  playRound: vi.fn(),
  deleteGame: vi.fn(),
  GameApiError: class GameApiError extends Error {
    constructor(message: string, public status?: number) {
      super(message);
      this.name = 'GameApiError';
    }
  },
}));

import * as gameApi from './services/gameApi';

describe('App - Auto-play speed controls', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.useFakeTimers();
  });

  afterEach(() => {
    vi.runOnlyPendingTimers();
    vi.useRealTimers();
  });

  const mockGameState: GameState = {
    gameId: 'test-game-123',
    roundNumber: 1,
    status: 'In Progress',
    players: [
      {
        name: 'Player 1',
        cardCount: 26,
        cardsPlayedThisRound: [],
        significantCard: null,
      },
      {
        name: 'Player 2',
        cardCount: 26,
        cardsPlayedThisRound: [],
        significantCard: null,
      },
    ],
    roundHistory: [],
    gameOver: false,
    winner: null,
    isDraw: false,
  };

  it('should allow changing auto-play speed during active auto-play', async () => {
    const user = userEvent.setup({ delay: null });

    // Mock createGame to return initial game state
    vi.mocked(gameApi.createGame).mockResolvedValue(mockGameState);

    // Mock playRound to return updated states
    let roundNumber = 1;
    vi.mocked(gameApi.playRound).mockImplementation(async () => {
      roundNumber++;
      return {
        ...mockGameState,
        roundNumber,
        roundHistory: [
          {
            roundNumber,
            winnerName: 'Player 1',
            wasWar: false,
          },
        ],
      };
    });

    render(<App />);

    // Start a game
    const startButton = screen.getByRole('button', { name: /start new game/i });
    await user.click(startButton);
    await waitFor(() => expect(gameApi.createGame).toHaveBeenCalled());

    // Start auto-play
    const autoPlayButton = screen.getByRole('button', { name: /auto play/i });
    await user.click(autoPlayButton);

    // Verify auto-play started
    expect(autoPlayButton).toHaveTextContent(/stop auto play/i);

    // Find the speed selector
    const speedSelector = screen.getByRole('combobox', { name: /speed:/i });
    expect(speedSelector).toBeInTheDocument();
    expect(speedSelector).not.toBeDisabled();

    // Change speed to "slow" during auto-play
    await user.selectOptions(speedSelector, 'slow');
    expect(speedSelector).toHaveValue('slow');

    // Change speed to "ludicrous"
    await user.selectOptions(speedSelector, 'ludicrous');
    expect(speedSelector).toHaveValue('ludicrous');

    // Verify auto-play is still running
    expect(autoPlayButton).toHaveTextContent(/stop auto play/i);
  });

  it('should stop auto-play when game ends', async () => {
    const user = userEvent.setup({ delay: null });

    // Mock createGame to return initial game state
    vi.mocked(gameApi.createGame).mockResolvedValue(mockGameState);

    // Mock playRound to return game over state on second call
    let callCount = 0;
    vi.mocked(gameApi.playRound).mockImplementation(async () => {
      callCount++;
      if (callCount === 1) {
        return {
          ...mockGameState,
          roundNumber: 2,
          roundHistory: [
            {
              roundNumber: 2,
              winnerName: 'Player 1',
              wasWar: false,
            },
          ],
        };
      } else {
        return {
          ...mockGameState,
          roundNumber: 3,
          status: 'Game Over',
          gameOver: true,
          winner: mockGameState.players[0],
          players: [
            { ...mockGameState.players[0], cardCount: 52 },
            { ...mockGameState.players[1], cardCount: 0 },
          ],
          roundHistory: [
            {
              roundNumber: 2,
              winnerName: 'Player 1',
              wasWar: false,
            },
            {
              roundNumber: 3,
              winnerName: 'Player 1',
              wasWar: false,
            },
          ],
        };
      }
    });

    render(<App />);

    // Start a game
    const startButton = screen.getByRole('button', { name: /start new game/i });
    await user.click(startButton);
    await waitFor(() => expect(gameApi.createGame).toHaveBeenCalled());

    // Start auto-play
    const autoPlayButton = screen.getByRole('button', { name: /auto play/i });
    await user.click(autoPlayButton);

    // Verify auto-play started
    expect(autoPlayButton).toHaveTextContent(/stop auto play/i);

    // Advance timers to trigger rounds
    await vi.advanceTimersByTimeAsync(100); // Initial scheduling delay
    await waitFor(() => expect(gameApi.playRound).toHaveBeenCalledTimes(1));

    // Advance timers for the delay between rounds (medium speed = 1000ms)
    await vi.advanceTimersByTimeAsync(1100);
    await waitFor(() => expect(gameApi.playRound).toHaveBeenCalledTimes(2));

    // Game should be over now, auto-play should stop
    await waitFor(() => {
      const button = screen.getByRole('button', { name: /auto play/i });
      expect(button).toHaveTextContent(/auto play/i); // Back to "Auto Play" not "Stop Auto Play"
      expect(button).toBeDisabled(); // Disabled because game is over
    });

    // Winner modal should appear
    await waitFor(() => {
      expect(screen.getByText(/game over/i)).toBeInTheDocument();
    });
  });
});

