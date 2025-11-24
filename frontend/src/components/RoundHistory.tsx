import type { RoundResult } from '../types/game.types';

interface RoundHistoryProps {
  history: RoundResult[];
}

export function RoundHistory({ history }: RoundHistoryProps) {
  if (!history || history.length === 0) {
    return (
      <div id="history-container">
        <p className="empty-message">No rounds played yet</p>
      </div>
    );
  }

  // Show most recent rounds first
  const recentRounds = [...history].reverse().slice(0, 10);

  return (
    <div id="history-container">
      {recentRounds.map((round) => (
        <div
          key={round.roundNumber}
          className={`history-item ${round.wasWar ? 'war' : ''}`}
        >
          <div className="history-round">
            Round {round.roundNumber}
            {round.wasWar && <span className="war-badge">WAR!</span>}
          </div>
          <div className="history-winner">Winner: {round.winnerName}</div>
        </div>
      ))}
    </div>
  );
}

