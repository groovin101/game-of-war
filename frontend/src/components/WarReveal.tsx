import { useState, useEffect } from 'react';
import type { Player } from '../types/game.types';
import { PlayingCard } from './PlayingCard';

interface WarRevealProps {
  player: Player;
  autoReveal?: boolean;
}

export function WarReveal({ player, autoReveal = false }: WarRevealProps) {
  const [revealed, setRevealed] = useState(false);

  // Auto-reveal war cards when autoReveal prop is true
  useEffect(() => {
    if (autoReveal && player.cardsPlayedThisRound && player.cardsPlayedThisRound.length > 1) {
      // Slight delay for visual effect
      const timer = setTimeout(() => {
        setRevealed(true);
      }, 300);
      return () => clearTimeout(timer);
    } else {
      // Reset revealed state when autoReveal becomes false or cards change (new round)
      setRevealed(false);
    }
  }, [autoReveal, player.cardsPlayedThisRound]);

  // Don't render anything if no cards have been played yet
  if (!player.cardsPlayedThisRound || player.cardsPlayedThisRound.length === 0) {
    return null;
  }

  // Not a war scenario, just show the single card
  if (player.cardsPlayedThisRound.length === 1) {
    const card = player.cardsPlayedThisRound[0];

    const isSignificant = player.significantCard !== null &&
      card.rank === player.significantCard.rank &&
      card.suit === player.significantCard.suit;

    return (
      <div className="cards-played">
        <PlayingCard card={card} isSignificant={isSignificant} />
        <div className="card-placeholder"></div>
        <div className="card-placeholder"></div>
        <div className="card-placeholder"></div>
      </div>
    );
  }

  // War scenario - show first card and WAR! reveal button
  const firstCard = player.cardsPlayedThisRound[0];
  const remainingCards = player.cardsPlayedThisRound.slice(1);

  return (
    <div className="cards-played war-container">
      <PlayingCard card={firstCard} />
      
      {!revealed && (
        <button 
          className="war-reveal-btn"
          onClick={() => setRevealed(true)}
          aria-label="Reveal war cards"
        >
          ⚔️ WAR!
        </button>
      )}
      
      {revealed && remainingCards.map((card, index) => {
        const isSignificant = player.significantCard !== null &&
          card.rank === player.significantCard.rank &&
          card.suit === player.significantCard.suit;
        
        return (
          <PlayingCard 
            key={`${card.rank}-${card.suit}-${index}`}
            card={card}
            isSignificant={isSignificant}
          />
        );
      })}
    </div>
  );
}

