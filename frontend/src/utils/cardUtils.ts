import type { Card } from '../types/game.types';

// Map backend rank names to card file names
const rankMap: Record<string, string> = {
  'ACE': 'A',
  'KING': 'K',
  'QUEEN': 'Q',
  'JACK': 'J',
  'TEN': '10',
  'NINE': '9',
  'EIGHT': '8',
  'SEVEN': '7',
  'SIX': '6',
  'FIVE': '5',
  'FOUR': '4',
  'THREE': '3',
  'TWO': '2'
};

// Map backend suit names to single character codes
const suitMap: Record<string, string> = {
  'HEART': 'H',
  'DIAMOND': 'D',
  'CLUB': 'C',
  'SPADE': 'S'
};

export function getCardImagePath(card: Card): string {
  const rank = rankMap[card.rank] || card.rank;
  const suit = suitMap[card.suit] || card.suit.charAt(0).toUpperCase();
  return `/cards/${rank}${suit}.png`;
}

export function formatCardDisplay(card: Card): string {
  return card.display;
}

