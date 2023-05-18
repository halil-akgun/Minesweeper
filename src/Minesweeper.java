import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Minesweeper {

    private String[][] gameBoard;
    private String[][] playerBoard;

    private int rowLength;
    private int columnLength;

    private int amountOfMines;

    private int markAsMine = 0;

    private final Scanner scanner = new Scanner(System.in);


    public void start() {
        System.out.println("\n\n================  Welcome to Minesweeper  ================\n");

        boolean isValid;
        do {
            isValid = true;
            try {
                System.out.print("Enter number of row: ");
                rowLength = scanner.nextInt();
                System.out.print("Enter number of column: ");
                columnLength = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                isValid = false;
                scanner.nextLine();
            }
        } while (!isValid);


        createBoards();
        placeMines();

        String select;
        while (true) {

//            print(gameBoard);
            print(playerBoard);
            System.out.println("Total mines: " + amountOfMines + "\t Marked as mines: " + markAsMine);
            System.out.println();

            System.out.print("Press X to mark the mine or any key to guess: ");
            select = scanner.next();
            Map<String, Integer> guessOfPlayer = guessOfPlayer();

            if (select.equalsIgnoreCase("x")) {
                if (playerBoard[guessOfPlayer.get("row")][guessOfPlayer.get("column")].equals(" ")) {
                    playerBoard[guessOfPlayer.get("row")][guessOfPlayer.get("column")] = "X";
                    markAsMine++;
                }
            } else {
                if (isThereMine(guessOfPlayer)) {
                    playerBoard[guessOfPlayer.get("row")][guessOfPlayer.get("column")] = "*";
                    System.out.println("\n\nYou stepped on a mine.");
                    System.out.println("\nGame Board:");
                    print(gameBoard);
                    System.out.println("\nYour Guesses:");
                    print(playerBoard);
                    break;
                }
                updatePlayerBoard(guessOfPlayer);
            }

            if (isWin()) {
                System.out.println("\n\n******************************************n*********");
                System.out.println("************  CONGRATULATIONS! YOU WON  ************");
                System.out.println("********n*******************************************");
                System.out.println("\nGame Board:");
                print(gameBoard);
                System.out.println("\nYour Guesses:");
                print(playerBoard);
                break;
            }

        }

    }

    public void createBoards() {
        gameBoard = new String[rowLength][columnLength];
        playerBoard = new String[rowLength][columnLength];

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                gameBoard[i][j] = " ";
                playerBoard[i][j] = " ";
            }
        }
    }

    public void placeMines() {
        amountOfMines = rowLength * columnLength / 4;
        int count = amountOfMines;
        int rowIndex;
        int columnIndex;

        while (count > 0) {
            rowIndex = (int) (Math.random() * (rowLength));
            columnIndex = (int) (Math.random() * (columnLength));

            if (gameBoard[rowIndex][columnIndex].equals(" ")) {
                gameBoard[rowIndex][columnIndex] = "X";
                count--;
            }
        }

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                map.put("row", i);
                map.put("column", j);
                if (!isThereMine(map)) {
                    String num = String.valueOf(numberOfMinesAround(map));
                    gameBoard[map.get("row")][map.get("column")] = num;
                }
            }
        }
    }

    Map<String, Integer> guessOfPlayer() {
        Map<String, Integer> guessOfPlayer = new HashMap<>();
        boolean isValid;
        int rowIndex = 0;
        int columnIndex = 0;

        do {
            isValid = true;
            try {
                System.out.print("Enter Row Number: ");
                rowIndex = scanner.nextInt() - 1;
                System.out.print("Enter Column Number: ");
                columnIndex = scanner.nextInt() - 1;
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                isValid = false;
                scanner.nextLine();
                continue;
            }
            if (rowIndex > rowLength - 1 || rowIndex < 0 || columnIndex > columnLength - 1 || columnIndex < 0) {
                System.out.println("Please enter a valid number.");
                isValid = false;
            }
        } while (!isValid);

        guessOfPlayer.put("row", rowIndex);
        guessOfPlayer.put("column", columnIndex);

        return guessOfPlayer;
    }

    boolean isThereMine(Map<String, Integer> guess) {
        return gameBoard[guess.get("row")][guess.get("column")].equals("X");
    }

    void updatePlayerBoard(Map<String, Integer> guess) {
        String num = String.valueOf(numberOfMinesAround(guess));
        playerBoard[guess.get("row")][guess.get("column")] = num;
    }

    int numberOfMinesAround(Map<String, Integer> guess) {
        int startRow = guess.get("row") == 0 ? 0 : guess.get("row") - 1;
        int endRow = guess.get("row") == rowLength - 1 ? rowLength - 1 : guess.get("row") + 1;
        int startColumn = guess.get("column") == 0 ? 0 : guess.get("column") - 1;
        int endColumn = guess.get("column") == columnLength - 1 ? columnLength - 1 : guess.get("column") + 1;

        int counter = 0;

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                if (gameBoard[i][j].equals("X")) counter++;
            }
        }
        return counter;
    }

    boolean isWin() {
        int countX = 0;
        int countXOnlyPlayer = 0;
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < columnLength; j++) {
                if (gameBoard[i][j].equals("X") && playerBoard[i][j].equals("X")) countX++;
                else if (!gameBoard[i][j].equals("X") && playerBoard[i][j].equals("X")) countXOnlyPlayer++;
            }
        }
        return countX == amountOfMines && countXOnlyPlayer == 0;
    }

    public void print(String[][] arr) {

        for (int i = 0; i < arr[0].length; i++) {
            System.out.print("\t  " + (i + 1));
        }

        System.out.println();

        for (int i = 0; i < arr.length; i++) {
            System.out.print(i + 1 + ")");
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print("\t| " + arr[i][j]);
            }
            System.out.println("\t|");
        }
    }
}
