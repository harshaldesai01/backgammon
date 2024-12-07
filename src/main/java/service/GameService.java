package service;

import enums.CommandType;
import exceptions.InvalidCommandException;
import model.Checker;
import model.Dice;
import model.Player;
import util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameService {
    private final MatchManager matchManager;
    private final DoublingManager doublingManager;
    private BoardService boardService;
    private Player currentPlayer;
    private final Dice dice = new Dice();
    private final CommandParser commandParser = new CommandParser();

    private boolean isDiceSet = false;
    private int presetRoll1;
    private int presetRoll2;

    private boolean isFileInputMode = false;
    private BufferedReader fileBufferedReader;


    public void setPresetDiceRolls(int roll1, int roll2) {
        isDiceSet = true;
        presetRoll1 = roll1;
        presetRoll2 = roll2;
    }

    public GameService(MatchManager matchManager) {
        this.matchManager = matchManager;
        this.doublingManager = new DoublingManager();
    }

    public void setUpGame() {
        boardService = new BoardService(matchManager.getPlayer1(), matchManager.getPlayer2());
        determineStartingPlayer();
    }


    public void executeCommand(Command command) {
        if (command instanceof TestCommand testCommand) {
            processFileInput(testCommand.getFilename());
        }
        else if (command instanceof DiceCommand diceCommand) {
            setPresetDiceRolls(diceCommand.getRoll1(), diceCommand.getRoll2());
            System.out.printf("Dice rolls set to: %d and %d.%n", diceCommand.getRoll1(), diceCommand.getRoll2());
        } else {
            switch (command.getType()) {
                case ROLL -> {
                    executeRollAndPlay(currentPlayer);
                    displayGameState();
                    toggleCurrentPlayer();
                }
                case PIP -> displayPipCounts();
                default -> System.out.println("Unknown command. Please try again.");
            }
        }
    }

    private void processFileInput(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            fileBufferedReader = reader;
            isFileInputMode = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                try {
                    Command command = commandParser.parseCommand(line.trim());
                    executeCommand(command);
                } catch (InvalidCommandException e) {
                    System.out.printf("Invalid command in file '%s': %s%n", filename, e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.printf("Error reading file '%s': %s%n", filename, e.getMessage());
        } finally {
            isFileInputMode = false; // Ensure manual input resumes after file processing
            fileBufferedReader = null;
        }
    }

    public void updateScore() {
        // Determine the winner or if the game is a draw
        Player winner = null;
        Player loser = null;

        if (hasPlayerWon(matchManager.getPlayer1())) {
            winner = matchManager.getPlayer1();
            loser = matchManager.getPlayer2();
        } else if (hasPlayerWon(matchManager.getPlayer2())) {
            winner = matchManager.getPlayer2();
            loser = matchManager.getPlayer1();
        }

        // Handle scoring based on outcome
        if (winner != null) {
            int points = calculatePoints(winner, loser);
            matchManager.incrementScore(winner, points);
        } else {
            System.out.println("The game ends in a draw. No points awarded.");
        }

        // Reset the doubling cube for the next game
        doublingManager.getDoublingCube().reset();
    }

    private int calculatePoints(Player winner, Player loser) {
        int basePoints = doublingManager.getDoublingCube().getValue();

        if (loser != null && isBackgammon(loser)) {
            System.out.printf("Backgammon! %s scores triple points!%n", winner.getName());
            return basePoints * CommonConstants.BACKGAMMON;
        } else if (loser != null && isGammon(loser)) {
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
            return true;
        } else return hasPlayerWon(matchManager.getPlayer2());
    }

    public void displayGameState() {
        System.out.println("\nMatch State:");
        System.out.println(matchManager.getPlayer1().getName() + " Score: " + matchManager.getPlayer1Score());
        System.out.println(matchManager.getPlayer2().getName() + " Score: " + matchManager.getPlayer2Score());
        System.out.println("Match Length: " + matchManager.getMatchLength());

        System.out.println("\nCurrent Game State:");
        boardService.displayBoard();
        System.out.println("Doubling Cube Value: " + doublingManager.getDoublingCube().getValue());
        if (doublingManager.getDoublingCube().getOwner() == null) {
            System.out.println("The cube is in the middle (no owner).");
        } else {
            System.out.println("Doubling cube owned by: " + doublingManager.getDoublingCube().getOwner().getName());
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
        System.out.println("Rolling dice to determine who goes first...");
        while (true) {
            int rollPlayer1 = dice.roll();
            int rollPlayer2 = dice.roll();

            System.out.println(matchManager.getPlayer1().getName() + " rolled: " + rollPlayer1);
            System.out.println(matchManager.getPlayer2().getName() + " rolled: " + rollPlayer2);

            if (rollPlayer1 != rollPlayer2) {
                currentPlayer = rollPlayer1 > rollPlayer2 ? matchManager.getPlayer1() : matchManager.getPlayer2();
                System.out.println(currentPlayer.getName() + " goes first!");
                break;
            }

            System.out.println("It's a tie! Rolling again...");
        }
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
        List<Integer> rolls = generateRolls(roll1, roll2);

        while (!rolls.isEmpty()) {
            List<String> options = generateMoveOptions(player, rolls);
            if (options.isEmpty()) {
                System.out.println(CommonConstants.NO_LEGAL_MOVES_MESSAGE);
                break;
            }

            displayMoveOptions(options);

            char selectedOption;
            if (isFileInputMode) {
                try {
                    String fileInput = fileBufferedReader.readLine();
                    if (fileInput != null && !fileInput.trim().isEmpty()) {
                        selectedOption = fileInput.trim().toUpperCase().charAt(0);
                    } else {
                        throw new IOException("End of file or invalid input in test command.");
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from test file. Switching back to manual input.");
                    isFileInputMode = false;
                    selectedOption = getUserSelection(options.size());
                }
            } else {
                selectedOption = getUserSelection(options.size());
            }

            boolean successfulMove = executeSelectedOption(selectedOption, options, rolls);

            if (!successfulMove) {
                System.out.println(CommonConstants.INVALID_SELECTION_MESSAGE);
                continue;
            }
            displayGameState();
        }
    }

    private void displayMoveOptions(List<String> options) {
        System.out.println("Available move options:");
        char optionLetter = 'A';
        for (String option : options) {
            System.out.println(optionLetter + ") " + option);
            optionLetter++;
        }
    }

    private List<Integer> generateRolls(int roll1, int roll2) {
        List<Integer> rolls = new ArrayList<>();
        if (roll1 == roll2) {
            for (int i = 0; i < 4; i++) rolls.add(roll1);
        } else {
            rolls.add(roll1);
            rolls.add(roll2);
        }
        return rolls;
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

    public void offerDouble() throws InvalidCommandException {
        if (doublingManager.getDoublingCube().getOwner() != currentPlayer) {
            throw new InvalidCommandException("You cannot offer the doubling cube!");
        }

        Player opponent = getOpponentPlayer();

        doublingManager.setPlayerToRespond(opponent);
        System.out.printf("%s offers to double the stakes to %d.%n", currentPlayer.getName(), doublingManager.getDoublingCube().getValue() * 2);

        handlingDoublingResponse(opponent);
    }

    private void handlingDoublingResponse(Player opponent) {
        while (true) {
            System.out.printf("%s, do you accept the double? (ACCEPT/REFUSE): ", opponent.getName());
            try {
                Command command = commandParser.parseCommand(commandParser.getUserInput());
                if (command.getType() == CommandType.ACCEPT) {
                    acceptDouble();
                    return;
                } else if (command.getType() == CommandType.REFUSE) {
                    refuseDouble();
                    return;
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Invalid input. Please type 'ACCEPT' or 'REFUSE'.");
        }
    }


    public void acceptDouble() throws InvalidCommandException {
        if (!currentPlayer.equals(doublingManager.getPlayerToRespond())) {
            throw new InvalidCommandException("It's not your turn to respond to the double.");
        }

        doublingManager.getDoublingCube().doubleValue(currentPlayer);
        doublingManager.setPlayerToRespond(null);
        System.out.println(currentPlayer.getName() + " accepts the double. The stakes are now " + doublingManager.getDoublingCube().getValue() + ".");
    }

    public void refuseDouble() throws InvalidCommandException {
        if (!currentPlayer.equals(doublingManager.getPlayerToRespond())) {
            throw new InvalidCommandException("It's not your turn to respond to the double.");
        }

        Player winner = getOpponentPlayer();
        int points = doublingManager.getDoublingCube().getValue();
        matchManager.incrementScore(winner, points);

        System.out.println(currentPlayer.getName() + " refuses the double. " + winner.getName() + " wins " + points + " point(s).");

        // Reset for next game
        doublingManager.getDoublingCube().reset();
        doublingManager.setPlayerToRespond(null);
        matchManager.setGameOver(true);
    }

    private Player getOpponentPlayer() {
        return currentPlayer.equals(matchManager.getPlayer1()) ? matchManager.getPlayer2() : matchManager.getPlayer1();
    }

}