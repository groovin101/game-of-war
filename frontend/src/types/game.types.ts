// Type definitions mirroring backend DTOs

export interface Card {
  rank: string;
  suit: string;
  display: string;
}

export interface Player {
  name: string;
  cardCount: number;
  cardsPlayedThisRound: Card[];
  significantCard: Card | null;
}

export interface RoundResult {
  roundNumber: number;
  winnerName: string;
  wasWar: boolean;
}

export interface GameState {
  gameId: string;
  roundNumber: number;
  status: string;
  players: Player[];
  roundHistory: RoundResult[];
  gameOver: boolean;
  winner: Player | null;
  isDraw: boolean;
}

export interface CreateGameRequest {
  numberOfPlayers: number;
  numberOfSuits: number;
  numberOfRanks: number;
}

