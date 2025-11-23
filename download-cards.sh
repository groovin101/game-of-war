#!/bin/bash

echo "Downloading playing card images..."
echo ""

SUITS="S H D C"
RANKS="A 2 3 4 5 6 7 8 9 J Q K"

# Download regular cards (A, 2-9, J, Q, K)
for suit in $SUITS; do
    for rank in $RANKS; do
        echo "Downloading ${rank}${suit}.png"
        curl -s -o "src/main/resources/static/cards/${rank}${suit}.png" "https://deckofcardsapi.com/static/img/${rank}${suit}.png"
    done
done

# Download 10 cards (API uses "0" for rank 10)
for suit in $SUITS; do
    echo "Downloading 10${suit}.png"
    curl -s -o "src/main/resources/static/cards/10${suit}.png" "https://deckofcardsapi.com/static/img/0${suit}.png"
done

echo ""
echo "Done! All 52 cards downloaded to src/main/resources/static/cards/"

