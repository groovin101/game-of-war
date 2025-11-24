import type { GameState, CreateGameRequest } from '../types/game.types';

const API_BASE_URL = '/api/games';

export class GameApiError extends Error {
  constructor(message: string, status?: number) {
    super(message);
    this.name = 'GameApiError';
    this.status = status;
  }
  public status?: number;
}

export async function createGame(request: CreateGameRequest): Promise<GameState> {
  const response = await fetch(API_BASE_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    throw new GameApiError('Failed to create game', response.status);
  }

  return response.json();
}

export async function playRound(gameId: string): Promise<GameState> {
  const response = await fetch(`${API_BASE_URL}/${gameId}/play-round`, {
    method: 'POST',
  });

  if (!response.ok) {
    throw new GameApiError('Failed to play round', response.status);
  }

  return response.json();
}

export async function deleteGame(gameId: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/${gameId}`, {
    method: 'DELETE',
  });

  if (!response.ok) {
    throw new GameApiError('Failed to delete game', response.status);
  }
}

