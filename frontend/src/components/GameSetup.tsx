import { useState } from 'react';

interface GameSetupProps {
  onStartGame: (players: number, suits: number, ranks: number) => void;
  isCreating: boolean;
}

export function GameSetup({ onStartGame, isCreating }: GameSetupProps) {
  const [numberOfPlayers, setNumberOfPlayers] = useState(2);
  const [numberOfSuits, setNumberOfSuits] = useState(4);
  const [numberOfRanks, setNumberOfRanks] = useState(13);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (numberOfPlayers < 2 || numberOfPlayers > 10) {
      alert('Number of players must be between 2 and 10');
      return;
    }
    
    onStartGame(numberOfPlayers, numberOfSuits, numberOfRanks);
  };

  return (
    <div id="setup-section" className="section">
      <h2>Game Setup</h2>
      <form onSubmit={handleSubmit} className="setup-form">
        <div className="form-group">
          <label htmlFor="num-players">Number of Players (2-10):</label>
          <input
            type="number"
            id="num-players"
            min="2"
            max="10"
            value={numberOfPlayers}
            onChange={(e) => setNumberOfPlayers(parseInt(e.target.value))}
            disabled={isCreating}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="num-suits">Number of Suits:</label>
          <input
            type="number"
            id="num-suits"
            min="1"
            max="10"
            value={numberOfSuits}
            onChange={(e) => setNumberOfSuits(parseInt(e.target.value))}
            disabled={isCreating}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="num-ranks">Number of Ranks:</label>
          <input
            type="number"
            id="num-ranks"
            min="2"
            max="20"
            value={numberOfRanks}
            onChange={(e) => setNumberOfRanks(parseInt(e.target.value))}
            disabled={isCreating}
          />
        </div>
        
        <button type="submit" className="btn btn-primary" disabled={isCreating}>
          {isCreating ? 'Creating Game...' : 'Start New Game'}
        </button>
      </form>
    </div>
  );
}

