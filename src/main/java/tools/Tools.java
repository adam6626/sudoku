package tools;

import model.Field;
import resolver.Resolver;

import java.util.ArrayList;
import java.util.List;

public class Tools {

    private static ArrayList<Resolver> resolverThreads = new ArrayList<>();
    private static long resultStopTimeMilli;
    private static ArrayList<Field> resultSolvedBoard;


    public static String tempStringSudoku = ".....75..7..1...4.5.....2....139...83..786..48...417....8.....9.5...3..1..46.....";
    //static  String tempStringSudoku = "..............3.85..1.2.......5.7.....4...1...9.......5......73..2.1........4...9";
    public static ArrayList<Resolver> getResolverThreads() {
        return resolverThreads;
    }

    public static void addResolverThread(Resolver resolver) {
        resolverThreads.add(resolver);
    }
    public static void printSudoku(int[][] sudoku) {
        for(int i = 0; i < sudoku.length; i++) {
            for(int j = 0; j < sudoku[i].length; j++) {
                if (sudoku[i][j] == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print(sudoku[i][j] + " ");
                }
                if (j == 2 || j == 5) System.out.print("| ");
            }
            System.out.println();
            if (i == 2 || i == 5) System.out.println("------+-------+------");
        }
    }

    public static int[][] stringToArray(String string) {
        int[][] board = new int[9][9];
        int[] intArray = new int[81];
        for (int i = 0; i < 81; i ++) {
            char ch = string.charAt(i);
            int toPut = 0;
            if (ch != ".".charAt(0)) toPut = Character.getNumericValue(ch);
            intArray[i] = toPut;
        }

        int x = 0;
        int y = 0;
        for (int i : intArray) {
            board[y][x] = i;
            x++;
            if (x == 9) {
                y++;
                x = 0;
            }
        }
        return board;
    }

    public static int[][] fieldsToArray(List<Field> fields) {
        int[][] array = new int[9][9];
        for (Field field : fields) {
            array[field.getRowId()][field.getColumnId()] = field.getValue();
        }
        return array;
    }

    public static long getResultStopTimeMilli() {
        return resultStopTimeMilli;
    }

    public static void setResultStopTimeMilli(long resultStopTimeMilli) {
        Tools.resultStopTimeMilli = resultStopTimeMilli;
    }

    public static ArrayList<Field> getResultSolvedBoard() {
        return resultSolvedBoard;
    }

    public static void setResultSolvedBoard(ArrayList<Field> resultSolvedBoard) {
        Tools.resultSolvedBoard = resultSolvedBoard;
    }

    public static void resetThreadCount(){
        resolverThreads = new ArrayList<>();
    }
}
