package service;

import enums.CommandType;
import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;
import model.Checker;
import model.Dice;
import model.DoublingCube;
import model.Player;
import util.Command;
import util.CommandParser;
import util.CommonConstants;
import util.DiceCommand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameService {
    private final MatchManager matchManager;
    private BoardService boardService;
    private Player currentPlayer;
    private final Dice dice = new Dice();
    private final DoublingCube doublingCube;
    private final CommandParser commandParser = new CommandParser();

    private boolean isDiceSet = false;
    private int presetRoll1;
    private int presetRoll2;

    public void setPresetDiceRolls(int roll1, int roll2) {
        isDiceSet = true;
        presetRoll1 = roll1;
        presetRoll2 = roll2;
    }

    public GameService(MatchManager matchManager) {
        this.matchManager = matchManager;
        this.doublingCube = new DoublingCube();
    }

    public void setUpGame() {
        boardService = new BoardService(matchManager.getPlayer1(), matchManager.getPlayer2());
        determineStartingPlayer();
    }


    public void executeCommand(Command command) {
        if (command instanceof DiceCommand diceCommand) {
            setPresetDiceRolls(diceCommand.getRoll1(), diceCommand.getRoll2());
            System.out.printf("Dice rolls set to: %d and %d.%n", diceCommand.getRoll1(), diceCommand.getRoll2());
        } else {
            // Handle other command types
            switch (command.getType()) {
                case ROLL -> {
                    executeRollAndPlay(currentPlayer);
                    displayGameState();
                    toggleCurrentPlayer();
                }
                case PIP -> displayPipCounts();
                case END_MATCH -> handleEndMatch();
                default -> System.out.println("Unknown command. Please try again.");
            }
        }
    }




    public void updateScore() {
        Player winner = currentPlayer;
        Player loser = getOpponentPlayer();

        int points = calculatePoints(winner, loser);

        System.out.printf("%s wins the game and scores %d point(s)!%n", winner.getName(), points);
        matchManager.incrementScore(winner, points);
        doublingCube.reset();
    }

    private int calculatePoints(Player winner, Player loser) {
        int basePoints = doublingCube.getValue();
        if (isBackgammon(loser)) {
            System.out.printf("Backgammon! %s scores triple points!%n", winner.getName());
            return basePoints * CommonConstants.BACKGAMMON;
        } else if (isGammon(loser)) {
            System.out.printf("Gammon! %s scores double points!%n", winner.getName());
            return basePoints * CommonConstants.GAMMON;
        }
        return basePoints;
    }


    private boolean isGammon(Player player) {
        // The opponent has not borne off any checkers
        return boardService.getBoard().getBearOffForPlayer(player).isEmpty();
    }

    private boolean isBackgammon(Player player) {
        // The opponent has not borne off any checkers
        // AND has checkers on the bar or in the winner's home board
        if (!isGammon(player)) {
            return false;
        }

        boolean hasCheckersOnBar = !boardService.getBoard().getBarForPlayer(player).isEmpty();
        boolean hasCheckersInWinnerHomeBoard = hasCheckersInHomeBoard(player, currentPlayer);

        return hasCheckersOnBar || hasCheckersInWinnerHomeBoard;
    }

    private boolean hasCheckersInHomeBoard(Player player, Player winner) {
        int homeStart, homeEnd;

        if (winner.equals(matchManager.getPlayer1())) {
            homeStart = 19;
            homeEnd = 24;
        } else {
            homeStart = 1;
            homeEnd = 6;
        }

        for (int position = homeStart; position <= homeEnd; position++) {
            List<Checker> checkers = boardService.getBoard().getPositions().getOrDefault(position, new ArrayList<>());
            for (Checker checker : checkers) {
                if (checker.getOwner().equals(player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isGameOver() {
        if (hasPlayerWon(matchManager.getPlayer1())) {
            System.out.println("Congratulations " + matchManager.getPlayer1().getName() + ", you have won the game!");
            return true;
        } else if (hasPlayerWon(matchManager.getPlayer2())) {
            System.out.println("Congratulations " + matchManager.getPlayer2().getName() + ", you have won the game!");
            return true;
        }
        return false;
    }

    public void displayGameState() {
        System.out.println("\nMatch State:");
        System.out.println(matchManager.getPlayer1().getName() + " Score: " + matchManager.getPlayer1Score());
        System.out.println(matchManager.getPlayer2().getName() + " Score: " + matchManager.getPlayer2Score());
        System.out.println("Match Length: " + matchManager.getMatchLength());

        System.out.println("\nCurrent Game State:");
        boardService.displayBoard();
        System.out.println("Doubling Cube Value: " + doublingCube.getValue());
        if (doublingCube.getOwner() == null) {
            System.out.println("The cube is in the middle (no owner).");
        } else {
            System.out.println("Doubling cube owned by: " + doublingCube.getOwner().getName());
        }
        System.out.println("It's " + currentPlayer.getName() + "'s turn.");
        System.out.println(currentPlayer.getName() + "'s pip count: " + calculatePipCount(currentPlayer));
    }

    private void executeRollAndPlay(Player player) {
        int roll1, roll2;

        if (isDiceSet) {
            roll1 = presetRoll1;
            roll2 = presetRoll2;
            isDiceSet = false; // Reset the flag after use
        } else {
            roll1 = dice.roll();
            roll2 = dice.roll();
        }

        System.out.println(player.getName() + " rolled " + roll1 + " and " + roll2);
        playRoll(player, roll1, roll2);
    }


    private void displayPipCounts() {
        System.out.println(matchManager.getPlayer1().getName() + "'s pip count: " + calculatePipCount(matchManager.getPlayer1()));
        System.out.println(matchManager.getPlayer2().getName() + "'s pip count: " + calculatePipCount(matchManager.getPlayer2()));
    }

    private void determineStartingPlayer() {
        int rollPlayer1, rollPlayer2;
        System.out.println("Rolling dice to determine who goes first...");
        do {
            rollPlayer1 = dice.roll();
            rollPlayer2 = dice.roll();
            System.out.println(matchManager.getPlayer1().getName() + " rolled: " + rollPlayer1);
            System.out.println(matchManager.getPlayer2().getName() + " rolled: " + rollPlayer2);

            if (rollPlayer1 > rollPlayer2) {
                currentPlayer = matchManager.getPlayer1();
                System.out.println(currentPlayer.getName() + " goes first!");
            } else if (rollPlayer2 > rollPlayer1) {
                currentPlayer = matchManager.getPlayer2();
                System.out.println(currentPlayer.getName() + " goes first!");
            } else {
                System.out.println("It's a tie! Rolling again...");
            }
        } while (rollPlayer1 == rollPlayer2);
    }

    private void toggleCurrentPlayer() {
        currentPlayer = currentPlayer.equals(matchManager.getPlayer1()) ? matchManager.getPlayer2() : matchManager.getPlayer1();
    }

    private boolean hasPlayerWon(Player player) {
        return boardService.getBearOffForPlayer(player).size() + boardService.getPositions().values().stream()
                .flatMap(List::stream)
                .filter(checker -> checker.getOwner().equals(player))
                .count() == 0;
    }

    private int calculatePipCount(Player player) {
        int pipCount = 0;
        int direction = (player == matchManager.getPlayer1()) ? 1 : -1;
        for (int position : boardService.getBoard().getPositions().keySet()) {
            List<Checker> checkers = boardService.getBoard().getPositions().get(position);
            for (Checker checker : checkers) {
                if (checker.getOwner().equals(player)) {
                    pipCount += Math.abs(position - (direction == 1 ? 25 : 0));
                }
            }
        }
        return pipCount;
    }

    private void playRoll(Player player, int roll1, int roll2) {
        List<Integer> rolls = new ArrayList<>();
        if (roll1 == roll2) {
            for (int i = 0; i < 4; i++) rolls.add(roll1);
        } else {
            rolls.add(roll1);
            rolls.add(roll2);
        }

        while (!rolls.isEmpty()) {
            List<String> options = generateMoveOptions(player, rolls);
            if (options.isEmpty()) {
                System.out.println("No legal moves available for the current rolls. Turn passes to the next player.");
                break;
            }

            System.out.println("Available move options for current roll:");
            char optionLetter = 'A';
            for (String option : options) {
                System.out.println(optionLetter + ") " + option);
                optionLetter++;
            }

            char selectedOption = getUserSelection(options.size());
            boolean successfulMove = executeSelectedOption(selectedOption, options, rolls);

            if (!successfulMove) {
                System.out.println("Invalid selection. Please try again.");
                continue;
            }
            displayGameState();
        }
    }

    private List<String> generateMoveOptions(Player player, List<Integer> rolls) {
        // Check if the player has checkers on the bar
        List<Checker> barCheckers = boardService.getBoard().getBarForPlayer(player);
        if (!barCheckers.isEmpty()) {
            return deDuplicateMoves(generateBarEntryMoves(player, rolls)); // Only allow bar re-entry moves
        }
        return deDuplicateMoves(boardService.getBoard().getLegalMoves(player, rolls)); // Regular move generation otherwise
    }

    private List<String> deDuplicateMoves(List<String> allMoves) {
        return new ArrayList<>(new HashSet<>(allMoves));
    }

    // Helper method to generate moves for re-entering checkers from the bar
    private List<String> generateBarEntryMoves(Player player, List<Integer> rolls) {
        List<String> barEntryMoves = new ArrayList<>();
        int homeStart = (player == matchManager.getPlayer1()) ? 1 : 19;
        int homeEnd = (player == matchManager.getPlayer1()) ? 6 : 24;

        for (int roll : rolls) {
            int targetPosition = (player == matchManager.getPlayer1()) ? roll : (25 - roll);
            if (targetPosition >= homeStart && targetPosition <= homeEnd) {
                if (boardService.getBoard().canEnterFromBar(player, targetPosition)) {
                    barEntryMoves.add("BAR -> " + targetPosition);
                }
            }
        }
        return barEntryMoves;
    }

    private void handleEndMatch() {
        Player highestScorer = matchManager.getPlayer1Score() > matchManager.getPlayer2Score()
                ? matchManager.getPlayer1()
                : matchManager.getPlayer2();
        matchManager.incrementScore(highestScorer, CommonConstants.SINGLE);
        System.out.println("Match ended early. " + highestScorer.getName() + " is awarded 1 point!");
    }
    private char getUserSelection(int numOptions) {
        while (true) {
            try {
                System.out.print("Enter the option letter: ");
                String input = commandParser.getUserInput();

                // Ensure input is a single letter and falls within valid range
                if (input.length() == 1) {
                    char selectedOption = Character.toUpperCase(input.charAt(0));
                    if (selectedOption >= 'A' && selectedOption < ('A' + numOptions)) {
                        return selectedOption;
                    }
                }

                System.out.println("Invalid input. Please select a letter from A to " + (char) ('A' + numOptions - 1) + ".");
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
    }

    private boolean executeSelectedOption(char selectedOption, List<String> options, List<Integer> rolls) {
        int optionIndex = selectedOption - 'A';
        if (optionIndex >= 0 && optionIndex < options.size()) {
            String chosenMove = options.get(optionIndex);
            System.out.println("You chose: " + chosenMove);

            int toPosition;
            int rollUsed;

            if (chosenMove.startsWith("BAR")) {
                // Handling move from the bar
                toPosition = Integer.parseInt(chosenMove.split(" -> ")[1].trim());
                boardService.getBoard().enterFromBar(currentPlayer, toPosition);

                // Calculate rollUsed directly from the destination position for bar entries
                rollUsed = Math.abs((currentPlayer == matchManager.getPlayer1() ? 0 : 25) - toPosition);
            } else if(chosenMove.endsWith("OFF")) {
                // Bear-off handling
                int fromPosition = Integer.parseInt(chosenMove.split(" -> ")[0].trim());
                boardService.getBoard().bearOffChecker(currentPlayer, fromPosition);

                // Calculate rollUsed for bear-off moves
                rollUsed = Math.abs((currentPlayer == matchManager.getPlayer1() ? 0 : 25) - fromPosition);
            } else {
                // Regular move handling
                String[] moveParts = chosenMove.split(" -> ");
                int fromPosition = Integer.parseInt(moveParts[0].trim());
                toPosition = Integer.parseInt(moveParts[1].trim());
                boardService.getBoard().makeMove(currentPlayer, fromPosition, toPosition);

                // Calculate rollUsed for regular moves
                rollUsed = Math.abs(toPosition - fromPosition);
            }

            // Remove the used roll from rolls list
            rolls.remove(Integer.valueOf(rollUsed));

            return true;
        }
        return false;
    }

    public void offerDouble() throws InvalidMoveException {
        if (doublingCube.getOwner() == currentPlayer) {
            throw new InvalidMoveException("You already own the cube and cannot offer a double.");
        }
        if (matchManager.isDoublingOffered()) {
            throw new InvalidMoveException("A double has already been offered.");
        }

        matchManager.setDoublingOffered(true);
        matchManager.setPlayerToRespond(getOpponentPlayer());
        System.out.printf("%s offers to double the stakes to %d.%n", currentPlayer.getName(), doublingCube.getValue() * 2);

        Player opponent = getOpponentPlayer();

        while (true) {
            currentPlayer = opponent;
            System.out.printf("%s, do you accept the double? (ACCEPT/REFUSE): ", opponent.getName());
            String input = commandParser.getUserInput();

            try {
                Command command = commandParser.parseCommand(input);

                switch (command.getType()) {
                    case ACCEPT:
                        acceptDouble(); // Trigger accept logic
                        return; // Exit the method after successful handling
                    case REFUSE:
                        refuseDouble(); // Trigger refuse logic
                        return; // Exit the method after successful handling
                    default:
                        System.out.println("Invalid command. Please type 'ACCEPT' or 'REFUSE'.");
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void acceptDouble() throws InvalidMoveException {
        if (!matchManager.isDoublingOffered()) {
            throw new InvalidMoveException("No double has been offered.");
        }
        if (!currentPlayer.equals(matchManager.getPlayerToRespond())) {
            throw new InvalidMoveException("It's not your turn to respond to the double.");
        }

        doublingCube.doubleValue(currentPlayer);
        matchManager.setDoublingOffered(false);
        matchManager.setPlayerToRespond(null);
        System.out.println(currentPlayer.getName() + " accepts the double. The stakes are now " + doublingCube.getValue() + ".");
    }

    public void refuseDouble() throws InvalidMoveException {
        if (!matchManager.isDoublingOffered()) {
            throw new InvalidMoveException("No double has been offered.");
        }
        if (!currentPlayer.equals(matchManager.getPlayerToRespond())) {
            throw new InvalidMoveException("It's not your turn to respond to the double.");
        }

        Player winner = getOpponentPlayer();
        int points = doublingCube.getValue();
        matchManager.incrementScore(winner, points);

        System.out.println(currentPlayer.getName() + " refuses the double. " + winner.getName() + " wins " + points + " point(s).");

        // Reset for next game
        doublingCube.reset();
        matchManager.setDoublingOffered(false);
        matchManager.setPlayerToRespond(null);
        matchManager.setGameOver(true);
    }

    private Player getOpponentPlayer() {
        return currentPlayer.equals(matchManager.getPlayer1()) ? matchManager.getPlayer2() : matchManager.getPlayer1();
    }

}