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

import static util.CommonConstants.*;

/**
 * Provides core game logic for Backgammon, including turn management, game state updates,
 * score calculations, and handling of commands such as rolling dice and doubling stakes.
 */
public class GameService {
    private final MatchManager matchManager;
    private final DoublingManager doublingManager;
    private BoardService boardService;
    private Player currentPlayer;
    private final Dice dice = new Dice();
    private final CommandParser commandParser = new CommandParser();
    private boolean gameOver;

    private boolean isDiceSet = false;
    private int presetRoll1;
    private int presetRoll2;

    private boolean isFileInputMode = false;
    private BufferedReader fileBufferedReader;

    /**
     * Constructs a GameService instance with the specified match manager.
     *
     * @param matchManager the manager handling the match state.
     */
    public GameService(MatchManager matchManager) {
        this.matchManager = matchManager;
        this.doublingManager = new DoublingManager();
    }

    /**
     * Initializes the game state, sets up the board, and determines the starting player.
     */
    public void setUpGame() {
        gameOver = false;
        boardService = new BoardService(matchManager.getPlayer1(), matchManager.getPlayer2());
        determineStartingPlayer();
    }

    /**
     * Presets the dice rolls to the given values
     * @param roll1 first roll value
     * @param roll2 second roll value
     */
    public void setPresetDiceRolls(int roll1, int roll2) {
        isDiceSet = true;
        presetRoll1 = roll1;
        presetRoll2 = roll2;
    }

    /**
     * Executes a command issued by the player, including rolling dice, offering doubles, or setting dice rolls.
     *
     * @param command the command to execute.
     */
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

    /**
     * Processes a file containing a series of commands for testing or automation.
     *
     * @param filename the file containing commands.
     */
    private void processFileInput(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            fileBufferedReader = reader;
            isFileInputMode = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
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
            isFileInputMode = false;
            fileBufferedReader = null;
        }
    }

    /**
     * Updates the scores based on the game result, including calculations for single, gammon, or backgammon scores.
     */
    public void updateScore() {
        Player winner = determineWinner();
        Player loser = (winner == matchManager.getPlayer1()) ? matchManager.getPlayer2() : matchManager.getPlayer1();

        int basePoints = doublingManager.getDoublingCube().getValue();
        int score = calculateScore(winner, loser, basePoints);

        matchManager.incrementScore(winner, score);
    }

    /**
     * Determines the winner of the game based on checkers borne off or pip counts.
     *
     * @return the player who won the game, or null if it's a draw.
     */
    private Player determineWinner() {
        boolean player1AllOff = isAllCheckersBorneOff(matchManager.getPlayer1());
        boolean player2AllOff = isAllCheckersBorneOff(matchManager.getPlayer2());

        if (player1AllOff && !player2AllOff) {
            return matchManager.getPlayer1();
        } else if (player2AllOff && !player1AllOff) {
            return matchManager.getPlayer2();
        } else if (!player1AllOff && !player2AllOff) {
            // If no one has borne off all checkers, determine by pip count
            int p1PipCount = calculatePipCount(matchManager.getPlayer1());
            int p2PipCount = calculatePipCount(matchManager.getPlayer2());

            if (p1PipCount < p2PipCount) {
                return matchManager.getPlayer1();
            } else if (p2PipCount < p1PipCount) {
                return matchManager.getPlayer2();
            } else {
                return null;
            }
        }

        return null;
    }

    private int calculateScore(Player winner, Player loser, int basePoints) {
        if (winner == null) {
            return 0;
        }

        boolean winnerAllOff = isAllCheckersBorneOff(winner);

        if (winnerAllOff) {
            if (isGammon(winner, loser)) {
                System.out.println(GAMMON_MESSAGE);
                return basePoints * GAMMON;
            } else if (isBackgammon(winner, loser)) {
                System.out.println(BACKGAMMON_MESSAGE);
                return basePoints * BACKGAMMON;
            } else {

                return basePoints * SINGLE;
            }
        } else {
            return basePoints;
        }
    }

    /**
     * Checks if a player has borne off all their checkers.
     *
     * @param player the player being checked.
     * @return true if the player has borne off all 15 checkers, false otherwise.
     */
    private boolean isAllCheckersBorneOff(Player player) {
        int borneOffCount = boardService.getBearOffForPlayer(player).size();
        return borneOffCount == 15;
    }

    /**
     * Determines if the game result is a Gammon.
     * A Gammon occurs when the loser has not borne off any checkers
     * and does not have checkers on the bar or in the winner's home board.
     *
     * @param winner the player who won the game.
     * @param loser  the player who lost the game.
     * @return true if the result is a Gammon, false otherwise.
     */
    private boolean isGammon(Player winner, Player loser) {
        int loserBorneOffCount = boardService.getBearOffForPlayer(loser).size();
        return loserBorneOffCount == 0 && !hasCheckersOnBarOrInWinnerHomeBoard(loser, winner);
    }


    /**
     * Determines if the game result is a Backgammon.
     * A Backgammon occurs when the loser has checkers on the bar
     * or in the winner's home board.
     *
     * @param winner the player who won the game.
     * @param loser  the player who lost the game.
     * @return true if the result is a Backgammon, false otherwise.
     */
    private boolean isBackgammon(Player winner, Player loser) {
        return hasCheckersOnBarOrInWinnerHomeBoard(loser, winner);
    }

    /**
     * Checks if a player has checkers either on the bar or in the winner's home board.
     *
     * @param player the player being checked.
     * @param winner the player who won the game.
     * @return true if the player has checkers on the bar or in the winner's home board, false otherwise.
     */
    private boolean hasCheckersOnBarOrInWinnerHomeBoard(Player player, Player winner) {
        boolean hasCheckersOnBar = !boardService.getBoard().getBarForPlayer(player).isEmpty();
        boolean hasCheckersInWinnerHomeBoard = hasCheckersInHomeBoard(player, winner);
        return hasCheckersOnBar || hasCheckersInWinnerHomeBoard;
    }


    /**
     * Checks if a player has any checkers in the winner's home board.
     *
     * @param player the player being checked.
     * @param winner the player who won the game.
     * @return true if the player has checkers in the winner's home board, false otherwise.
     */
    private boolean hasCheckersInHomeBoard(Player player, Player winner) {
        int homeStart, homeEnd;

        if (winner.equals(matchManager.getPlayer1())) {
            homeStart = PLAYER_1_HOME_START;
            homeEnd = PLAYER_1_HOME_END;
        } else {
            homeStart = PLAYER_2_HOME_START;
            homeEnd = PLAYER_2_HOME_END;
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

    /**
     * Checks if the game is over based on the current state or game conditions.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver || isGameOverCondition();
    }

    /**
     * Determines if the game-ending conditions have been met.
     *
     * @return true if either player has borne off all their checkers, false otherwise.
     */
    private boolean isGameOverCondition() {
        return isAllCheckersBorneOff(matchManager.getPlayer1()) || isAllCheckersBorneOff(matchManager.getPlayer2());
    }

    /**
     * Sets the game-over state.
     *
     * @param gameOver true to mark the game as over, false otherwise.
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Displays the current state of the game, including the board, scores, and doubling cube state.
     */
    public void displayGameState() {
        System.out.println(HORIZONTAL_DIVIDER);
        System.out.println("\nMatch State:");
        System.out.println(matchManager.getPlayer1().getName() + " Score: " + matchManager.getPlayer1Score());
        System.out.println(matchManager.getPlayer2().getName() + " Score: " + matchManager.getPlayer2Score());
        System.out.println("Match Length: " + matchManager.getMatchLength());

        System.out.println(HORIZONTAL_DIVIDER);
        System.out.println("Game Number: " + matchManager.getCurrentGameNumber());
        displayDoublingCube();

        System.out.println("\nCurrent Game State:");
        boardService.displayBoard();

        System.out.println("It's " + currentPlayer.getName() + "'s turn.");
        System.out.println(currentPlayer.getName() + "'s pip count: " + calculatePipCount(currentPlayer));
    }

    /**
     * Displays the current state of the doubling cube, including its value and owner.
     */
    private void displayDoublingCube(){
        System.out.println(HORIZONTAL_DIVIDER);
        System.out.println("Doubling Cube Value: " + doublingManager.getDoublingCube().getValue());
        if (doublingManager.getDoublingCube().getOwner() == null) {
            System.out.println(DOUBLING_CUBE_NO_OWNER_MESSAGE);
        } else {
            System.out.println("Doubling cube owned by: " + doublingManager.getDoublingCube().getOwner().getName());
        }
        System.out.println(HORIZONTAL_DIVIDER);
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
        System.out.println(HORIZONTAL_DIVIDER);

        System.out.println(PIP_COMMAND_MESSAGE);
        System.out.println(matchManager.getPlayer1().getName() + "'s pip count: " + calculatePipCount(matchManager.getPlayer1()));
        System.out.println(matchManager.getPlayer2().getName() + "'s pip count: " + calculatePipCount(matchManager.getPlayer2()));
    }

    /**
     * Determines the starting player by rolling dice.
     */
    private void determineStartingPlayer() {
        System.out.println(ROLL_TO_START_MESSAGE);
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

            System.out.println(ROLL_TIE_MESSAGE);
        }
    }

    /**
     * Toggles the current player between Player 1 and Player 2.
     */
    private void toggleCurrentPlayer() {
        currentPlayer = currentPlayer.equals(matchManager.getPlayer1()) ? matchManager.getPlayer2() : matchManager.getPlayer1();
    }

    /**
     * Calculates the pip count for the specified player.
     *
     * @param player the player whose pip count is calculated.
     * @return the total pip count for the player.
     */
    private int calculatePipCount(Player player) {
        int pipCount = 0;
        int direction = (player == matchManager.getPlayer1()) ? PLAYER_1_MOVE_DIRECTION : PLAYER_2_MOVE_DIRECTION;
        for (int position : boardService.getBoard().getPositions().keySet()) {
            List<Checker> checkers = boardService.getBoard().getPositions().get(position);
            for (Checker checker : checkers) {
                if (checker.getOwner().equals(player)) {
                    pipCount += Math.abs(position - (direction == PLAYER_1_MOVE_DIRECTION ? PLAYER_1_BEAR_OFF_POSITION : PLAYER_2_BEAR_OFF_POSITION));
                }
            }
        }
        return pipCount;
    }

    /**
     * Processes the player's roll and allows them to make moves based on the rolled values.
     *
     * @param player the player whose turn it is.
     * @param roll1  the value of the first die.
     * @param roll2  the value of the second die.
     */
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

    /**
     * Displays the list of available move options for the current player.
     *
     * @param options the list of move options.
     */
    private void displayMoveOptions(List<String> options) {
        System.out.println("Available move options:");
        char optionLetter = 'A';
        for (String option : options) {
            System.out.println(optionLetter + ") " + option);
            optionLetter++;
        }
    }

    /**
     * Generates a list of dice rolls based on the rolled values.
     * If the dice show the same value, four identical rolls are generated.
     *
     * @param roll1 the value of the first die.
     * @param roll2 the value of the second die.
     * @return a list of dice rolls.
     */
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

    /**
     * Generates a list of all legal move options for the current player based on the dice rolls.
     *
     * @param player the player whose move options are being generated.
     * @param rolls  the list of available dice rolls.
     * @return a list of legal move options as strings.
     */
    private List<String> generateMoveOptions(Player player, List<Integer> rolls) {
        List<Checker> barCheckers = boardService.getBoard().getBarForPlayer(player);
        if (!barCheckers.isEmpty()) {
            return deDuplicateMoves(generateBarEntryMoves(player, rolls));
        }
        return deDuplicateMoves(boardService.getBoard().getLegalMoves(player, rolls));
    }

    /**
     * Removes duplicate move options from the provided list.
     *
     * @param allMoves the list of all moves, potentially containing duplicates.
     * @return a list of unique move options.
     */
    private List<String> deDuplicateMoves(List<String> allMoves) {
        return new ArrayList<>(new HashSet<>(allMoves));
    }

    /**
     * Generates a list of legal moves for entering checkers from the bar onto the board.
     *
     * @param player the player attempting to move checkers from the bar.
     * @param rolls  the list of available dice rolls.
     * @return a list of bar entry moves as strings.
     */
    private List<String> generateBarEntryMoves(Player player, List<Integer> rolls) {
        List<String> barEntryMoves = new ArrayList<>();
        int homeStart = (player == matchManager.getPlayer1()) ? PLAYER_2_HOME_START : PLAYER_1_HOME_START;
        int homeEnd = (player == matchManager.getPlayer1()) ? PLAYER_2_HOME_END : PLAYER_1_HOME_END;

        for (int roll : rolls) {
            int targetPosition = (player == matchManager.getPlayer1()) ? roll : (PLAYER_1_BEAR_OFF_POSITION - roll);
            if (targetPosition >= homeStart && targetPosition <= homeEnd) {
                if (boardService.getBoard().canEnterFromBar(player, targetPosition)) {
                    barEntryMoves.add("BAR -> " + targetPosition);
                }
            }
        }
        return barEntryMoves;
    }


    /**
     * Prompts the user to select an option from a list of available moves.
     *
     * @param numOptions the number of available options.
     * @return the selected option as a character.
     */
    private char getUserSelection(int numOptions) {
        while (true) {
            try {
                System.out.print("Enter the option letter: ");
                String input = commandParser.getUserInput();

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

    /**
     * Executes the move option selected by the user.
     *
     * @param selectedOption the selected option letter.
     * @param options        the list of available options.
     * @param rolls          the list of remaining dice rolls.
     * @return true if the move was successfully executed, false otherwise.
     */
    private boolean executeSelectedOption(char selectedOption, List<String> options, List<Integer> rolls) {
        int optionIndex = selectedOption - 'A';
        if (optionIndex >= 0 && optionIndex < options.size()) {
            String chosenMove = options.get(optionIndex);
            System.out.println("You chose: " + chosenMove);

            int toPosition;
            int rollUsed;

            if (chosenMove.startsWith(BAR)) {
                toPosition = Integer.parseInt(chosenMove.split(" -> ")[1].trim());
                boardService.getBoard().enterFromBar(currentPlayer, toPosition);

                rollUsed = Math.abs((currentPlayer == matchManager.getPlayer1() ? PLAYER_2_BEAR_OFF_POSITION : PLAYER_1_BEAR_OFF_POSITION) - toPosition);
            } else if(chosenMove.endsWith(OFF)) {
                int fromPosition = Integer.parseInt(chosenMove.split(" -> ")[0].trim());
                boardService.getBoard().bearOffChecker(currentPlayer, fromPosition);

                rollUsed = rolls.stream()
                        .filter(roll -> roll >= Math.abs((currentPlayer == matchManager.getPlayer1()? PLAYER_1_BEAR_OFF_POSITION: PLAYER_2_BEAR_OFF_POSITION) - fromPosition)).findFirst()
                        .orElseThrow(() -> new IllegalStateException(NO_VALID_ROLLS_MESSAGE));
            } else {
                String[] moveParts = chosenMove.split(" -> ");
                int fromPosition = Integer.parseInt(moveParts[0].trim());
                toPosition = Integer.parseInt(moveParts[1].trim());
                boardService.getBoard().makeMove(currentPlayer, fromPosition, toPosition);

                rollUsed = Math.abs(toPosition - fromPosition);
            }

            rolls.remove(Integer.valueOf(rollUsed));

            return true;
        }
        return false;
    }

    /**
     * Handles the offering of the doubling cube, allowing the opponent to accept or refuse.
     *
     * @throws InvalidCommandException if the doubling offer is invalid.
     */
    public void offerDouble() throws InvalidCommandException {
        if (null != doublingManager.getDoublingCube().getOwner() && doublingManager.getDoublingCube().getOwner() != currentPlayer) {
            throw new InvalidCommandException("You cannot offer the doubling cube!");
        }

        System.out.println(HORIZONTAL_DIVIDER);
        System.out.printf("%s offers to double the stakes to %d.%n", currentPlayer.getName(), doublingManager.getDoublingCube().getValue() * 2);

        Player opponent = getOpponentPlayer();
        doublingManager.setPlayerToRespond(opponent);
        toggleCurrentPlayer();

        handleDoublingResponse(opponent);
    }

    /**
     * Handles the response to a doubling cube offer, allowing the opponent to accept or refuse.
     *
     * @param opponent the opponent responding to the doubling offer.
     */
    private void handleDoublingResponse(Player opponent) {
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


    /**
     * Handles the player's acceptance of a doubling offer, doubling the cube value.
     *
     * @throws InvalidCommandException if it's not the player's turn to respond.
     */
    public void acceptDouble() throws InvalidCommandException {
        if (!currentPlayer.equals(doublingManager.getPlayerToRespond())) {
            throw new InvalidCommandException(DOUBLING_CUBE_ERROR_MESSAGE);
        }

        doublingManager.getDoublingCube().doubleValue(currentPlayer);
        doublingManager.setPlayerToRespond(null);
        System.out.println(currentPlayer.getName() + " accepts the double. The stakes are now " + doublingManager.getDoublingCube().getValue() + ".");
    }

    /**
     * Handles the player's refusal of a doubling offer, ending the game and awarding points to the opponent.
     *
     * @throws InvalidCommandException if it's not the player's turn to respond.
     */
    public void refuseDouble() throws InvalidCommandException {
        if (!currentPlayer.equals(doublingManager.getPlayerToRespond())) {
            throw new InvalidCommandException(DOUBLING_CUBE_ERROR_MESSAGE);
        }

        Player winner = getOpponentPlayer();
        int points = doublingManager.getDoublingCube().getValue();
        matchManager.incrementScore(winner, points);

        System.out.println(currentPlayer.getName() + " refuses the double. ");

        doublingManager.getDoublingCube().reset();
        doublingManager.setPlayerToRespond(null);
        setGameOver(true);
    }

    /**
     * Retrieves the opponent of the current player.
     *
     * @return the opponent player.
     */
    private Player getOpponentPlayer() {
        return currentPlayer.equals(matchManager.getPlayer1()) ? matchManager.getPlayer2() : matchManager.getPlayer1();
    }

}