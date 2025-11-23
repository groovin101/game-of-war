// API Configuration
const API_BASE_URL = window.location.origin + '/api/games';

// State
let currentGameId = null;
let autoPlayInterval = null;

// DOM Elements
const setupSection = document.getElementById('setup-section');
const gameSection = document.getElementById('game-section');
const startGameBtn = document.getElementById('start-game-btn');
const playRoundBtn = document.getElementById('play-round-btn');
const autoPlayBtn = document.getElementById('auto-play-btn');
const newGameBtn = document.getElementById('new-game-btn');
const numPlayersInput = document.getElementById('num-players');
const numSuitsInput = document.getElementById('num-suits');
const numRanksInput = document.getElementById('num-ranks');
const roundNumberSpan = document.getElementById('round-number');
const gameStatusSpan = document.getElementById('game-status');
const playersContainer = document.getElementById('players-container');
const historyContainer = document.getElementById('history-container');
const winnerModal = document.getElementById('winner-modal');
const winnerMessage = document.getElementById('winner-message');
const closeModalBtn = document.getElementById('close-modal-btn');

// Event Listeners
startGameBtn.addEventListener('click', createNewGame);
playRoundBtn.addEventListener('click', playRound);
autoPlayBtn.addEventListener('click', toggleAutoPlay);
newGameBtn.addEventListener('click', resetToSetup);
closeModalBtn.addEventListener('click', resetToSetup);

// Create a new game
async function createNewGame() {
    const numberOfPlayers = parseInt(numPlayersInput.value);
    const numberOfSuits = parseInt(numSuitsInput.value);
    const numberOfRanks = parseInt(numRanksInput.value);

    if (numberOfPlayers < 2 || numberOfPlayers > 10) {
        alert('Number of players must be between 2 and 10');
        return;
    }

    try {
        startGameBtn.disabled = true;
        startGameBtn.textContent = 'Creating Game...';

        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                numberOfPlayers,
                numberOfSuits,
                numberOfRanks
            })
        });

        if (!response.ok) {
            throw new Error('Failed to create game');
        }

        const gameState = await response.json();
        currentGameId = gameState.gameId;

        setupSection.classList.add('hidden');
        gameSection.classList.remove('hidden');
        
        updateGameDisplay(gameState);
    } catch (error) {
        console.error('Error creating game:', error);
        alert('Failed to create game. Please try again.');
    } finally {
        startGameBtn.disabled = false;
        startGameBtn.textContent = 'Start New Game';
    }
}

// Play a single round
async function playRound() {
    if (!currentGameId) return;

    try {
        playRoundBtn.disabled = true;

        const response = await fetch(`${API_BASE_URL}/${currentGameId}/play-round`, {
            method: 'POST'
        });

        if (!response.ok) {
            throw new Error('Failed to play round');
        }

        const gameState = await response.json();
        updateGameDisplay(gameState);

        if (gameState.gameOver) {
            showWinnerModal(gameState);
            stopAutoPlay();
        }
    } catch (error) {
        console.error('Error playing round:', error);
        alert('Failed to play round. Please try again.');
    } finally {
        playRoundBtn.disabled = false;
    }
}

// Toggle auto-play
function toggleAutoPlay() {
    if (autoPlayInterval) {
        stopAutoPlay();
    } else {
        startAutoPlay();
    }
}

function startAutoPlay() {
    autoPlayBtn.textContent = 'Stop Auto Play';
    autoPlayBtn.classList.remove('btn-secondary');
    autoPlayBtn.classList.add('btn-primary');
    
    autoPlayInterval = setInterval(async () => {
        await playRound();
    }, 1500); // Play a round every 1.5 seconds
}

function stopAutoPlay() {
    if (autoPlayInterval) {
        clearInterval(autoPlayInterval);
        autoPlayInterval = null;
        autoPlayBtn.textContent = 'Auto Play';
        autoPlayBtn.classList.remove('btn-primary');
        autoPlayBtn.classList.add('btn-secondary');
    }
}

// Update the game display
function updateGameDisplay(gameState) {
    // Update status
    roundNumberSpan.textContent = gameState.roundNumber;
    gameStatusSpan.textContent = gameState.status;

    // Update players
    playersContainer.innerHTML = '';
    gameState.players.forEach(player => {
        const playerCard = createPlayerCard(player, gameState);
        playersContainer.appendChild(playerCard);
    });

    // Update history
    updateHistory(gameState.roundHistory);

    // Disable play button if game is over
    if (gameState.gameOver) {
        playRoundBtn.disabled = true;
        autoPlayBtn.disabled = true;
    }
}

// Create a player card element
function createPlayerCard(player, gameState) {
    const card = document.createElement('div');
    card.className = 'player-card';

    if (player.cardCount === 0) {
        card.classList.add('eliminated');
    }

    // Highlight if this player won the overall game
    if (gameState.gameOver && gameState.winner && gameState.winner.name === player.name) {
        card.classList.add('winner');
    }

    // Highlight if this player won the last round
    if (gameState.roundHistory && gameState.roundHistory.length > 0) {
        const lastRound = gameState.roundHistory[gameState.roundHistory.length - 1];
        if (lastRound.winnerName === player.name) {
            card.classList.add('round-winner');
        }
    }

    const nameDiv = document.createElement('div');
    nameDiv.className = 'player-name';
    nameDiv.textContent = player.name;

    const countDiv = document.createElement('div');
    countDiv.className = 'player-card-count';
    countDiv.textContent = `Cards: ${player.cardCount}`;

    const cardsDiv = document.createElement('div');
    cardsDiv.className = 'cards-played';

    if (player.cardsPlayedThisRound && player.cardsPlayedThisRound.length > 0) {
        player.cardsPlayedThisRound.forEach(playedCard => {
            const cardElement = document.createElement('div');
            cardElement.className = 'card';
            
            // Check if this is the significant card
            if (player.significantCard && 
                playedCard.rank === player.significantCard.rank && 
                playedCard.suit === player.significantCard.suit) {
                cardElement.classList.add('significant');
            }
            
            cardElement.innerHTML = formatCard(playedCard);
            cardsDiv.appendChild(cardElement);
        });
    }

    card.appendChild(nameDiv);
    card.appendChild(countDiv);
    card.appendChild(cardsDiv);

    return card;
}

// Format a card for display as an image
function formatCard(card) {
    // Map backend rank names to card file names
    const rankMap = {
        'ACE': 'A', 'KING': 'K', 'QUEEN': 'Q', 'JACK': 'J',
        'TEN': '10', 'NINE': '9', 'EIGHT': '8', 'SEVEN': '7',
        'SIX': '6', 'FIVE': '5', 'FOUR': '4', 'THREE': '3', 'TWO': '2'
    };
    
    // Map backend suit names to card image codes
    const suitMap = {
        'HEART': 'H',
        'DIAMOND': 'D',
        'CLUB': 'C',
        'SPADE': 'S'
    };
    
    const rank = rankMap[card.rank] || card.rank;
    const suitCode = suitMap[card.suit] || card.suit.charAt(0).toUpperCase();
    
    // Deck of Cards API format: rank + suit code (e.g., "AS" for Ace of Spades)
    const cardUrl = `https://deckofcardsapi.com/static/img/${rank}${suitCode}.png`;
    
    console.log('Card:', card.rank, card.suit, '-> URL:', cardUrl);
    
    return `<img src="${cardUrl}" alt="${card.display}" class="playing-card" loading="lazy">`;
}

// Update round history
function updateHistory(roundHistory) {
    if (!roundHistory || roundHistory.length === 0) {
        historyContainer.innerHTML = '<p class="empty-message">No rounds played yet</p>';
        return;
    }

    historyContainer.innerHTML = '';
    
    // Show most recent rounds first
    const recentRounds = roundHistory.slice().reverse().slice(0, 10);
    
    recentRounds.forEach(round => {
        const historyItem = document.createElement('div');
        historyItem.className = 'history-item';
        
        if (round.wasWar) {
            historyItem.classList.add('war');
        }

        const roundDiv = document.createElement('div');
        roundDiv.className = 'history-round';
        roundDiv.textContent = `Round ${round.roundNumber}`;
        
        if (round.wasWar) {
            const warBadge = document.createElement('span');
            warBadge.className = 'war-badge';
            warBadge.textContent = 'WAR!';
            roundDiv.appendChild(warBadge);
        }

        const winnerDiv = document.createElement('div');
        winnerDiv.className = 'history-winner';
        winnerDiv.textContent = `Winner: ${round.winnerName}`;

        historyItem.appendChild(roundDiv);
        historyItem.appendChild(winnerDiv);
        historyContainer.appendChild(historyItem);
    });
}

// Show winner modal
function showWinnerModal(gameState) {
    if (gameState.isDraw) {
        winnerMessage.textContent = 'The game ended in a draw!';
    } else if (gameState.winner) {
        winnerMessage.textContent = `${gameState.winner.name} wins the game!`;
    } else {
        winnerMessage.textContent = 'Game Over!';
    }
    
    winnerModal.classList.remove('hidden');
}

// Reset to setup screen
function resetToSetup() {
    stopAutoPlay();
    currentGameId = null;
    
    gameSection.classList.add('hidden');
    setupSection.classList.remove('hidden');
    winnerModal.classList.add('hidden');
    
    playRoundBtn.disabled = false;
    autoPlayBtn.disabled = false;
    
    playersContainer.innerHTML = '';
    historyContainer.innerHTML = '<p class="empty-message">No rounds played yet</p>';
    roundNumberSpan.textContent = '0';
    gameStatusSpan.textContent = 'Ready';
}

// Initialize the app
console.log('Game of War - Web Interface Loaded');


