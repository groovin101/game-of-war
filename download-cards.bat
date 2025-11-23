@echo off
echo Downloading playing card images...
echo.

set SUITS=S H D C
set RANKS=A 2 3 4 5 6 7 8 9 J Q K

REM Download regular cards (A, 2-9, J, Q, K)
for %%s in (%SUITS%) do (
    for %%r in (%RANKS%) do (
        echo Downloading %%r%%s.png
        curl -s -o src\main\resources\static\cards\%%r%%s.png https://deckofcardsapi.com/static/img/%%r%%s.png
    )
)

REM Download 10 cards (API uses "0" for rank 10)
for %%s in (%SUITS%) do (
    echo Downloading 10%%s.png
    curl -s -o src\main\resources\static\cards\10%%s.png https://deckofcardsapi.com/static/img/0%%s.png
)

echo.
echo Done! All 52 cards downloaded to src\main\resources\static\cards\
pause

