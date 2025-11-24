import type { Card } from '../types/game.types';
import { getCardImagePath } from '../utils/cardUtils';

interface PlayingCardProps {
  card: Card;
  isSignificant?: boolean;
}

export function PlayingCard({ card, isSignificant = false }: PlayingCardProps) {
  return (
    <div className={`card ${isSignificant ? 'significant' : ''}`}>
      <img
        src={getCardImagePath(card)}
        alt={card.display}
        className="playing-card"
        loading="lazy"
      />
    </div>
  );
}

