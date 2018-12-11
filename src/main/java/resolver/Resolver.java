package resolver;

import model.Collection;
import model.Field;
import tools.Tools;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Resolver extends Thread {


    private ArrayList<Field> allFields = new ArrayList<>();
    private Collection[] rows = new Collection[9];
    private Collection[] columns = new Collection[9];
    private Collection[] squares = new Collection[9];
    static long startTime;
    static long stopTime;

    public Resolver(int[][] sudoku){
        divideBoard(sudoku);
        fullfillArraysWithEmptyCollections();
        addFieldsToCollections();
    }

    public void run(){
        try {
            resolve();
            if (!isSolved()) {
                Field field = null;
                int possibilities = 1;
                while (field == null && possibilities < 5) {
                    field = getFieldWithMultiplePossibilities(possibilities);
                    possibilities++;
                }
                for (Integer value : field.getProbablyValues()) {
                    field.setValue(value);
                    Resolver resolver = new Resolver(Tools.fieldsToArray(allFields));
                    Tools.addResolverThread(resolver);
                    resolver.start();
                }
            } else {
                for (Resolver resolver : Tools.getResolverThreads()) {
                    if (resolver.getId() != Thread.currentThread().getId()) {
                        resolver.interrupt();
                    }
                    try {
                        stopTime = System.currentTimeMillis();
                        sleep(1000);
                        System.out.println("\n\n\n\nSudoku solved by thread: " + Thread.currentThread().getId() + " in " + (stopTime - startTime) + " milliseconds.\n");
                        Tools.printSudoku(Tools.fieldsToArray(allFields));
                        System.out.println("Thread count: " + Tools.getResolverThreads().size());
                        Tools.setResultSolvedBoard(allFields);
                        Tools.setResultStopTimeMilli(stopTime - startTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (NullPointerException e){

        }catch (ConcurrentModificationException e){

        }
    }

    private boolean isSolved(){
        for(Field field: allFields){
            if(field.getValue() == 0){
                return false;
            }
        }
        return true;
    }

    private Field getFieldWithMultiplePossibilities(int possibilities){
        for(Field field: allFields){
            if(field.getValue() == 0 && findPossibleValue(field).size() > possibilities){
                return field;
            }
        }
        return null;
    }

    @Override
    public void interrupt(){
        System.out.println("Thread no. " + Thread.currentThread().getId() + " Has been interrupted");
    }

    public void resolve() {
        boolean areAllCertainFound = false;

        int numberOfInserted = 0;
        while(!areAllCertainFound) {
            areAllCertainFound = true;

            for (Field field : allFields) {
                if(field.getValue()!=0){
                    continue;
                }
                findPossibleValue(field);
                if (field.getProbablyValues() != null && field.getProbablyValues().size() == 1) {
                    field.setValue(field.getProbablyValues().get(0));
                    numberOfInserted++;
                    areAllCertainFound = false;
                }
            }
        }
    }

    private ArrayList<Integer> findPossibleValue(Field field){

        if(field.getValue()!=0){
            return null;
        }

        ArrayList<Integer> possibleValues = field.getProbablyValues();

        Collection row = rows[field.getRowId()];
        ArrayList<Field> fieldsInRow = row.getListOfFieldsInThisRow();
        Collection column = columns[field.getColumnId()];
        ArrayList<Field> fieldsInColumn = column.getListOfFieldsInThisRow();
        Collection square = squares[field.getSquareId()];
        ArrayList<Field> fieldsInSquare = square.getListOfFieldsInThisRow();

        ArrayList<Field> summaryFields = new ArrayList<>();
        summaryFields.addAll(fieldsInRow);
        summaryFields.addAll(fieldsInColumn);
        summaryFields.addAll(fieldsInSquare);

        for(Field fieldToCheckInRow : summaryFields) {
            if (possibleValues.contains(fieldToCheckInRow.getValue())) {
                possibleValues.remove(new Integer(fieldToCheckInRow.getValue()));
            }
        }


        field.setProbablyValues(possibleValues);

        return possibleValues;
    }

    private void fullfillArraysWithEmptyCollections(){
        for(int i = 0 ; i<9 ;i++){
            rows[i] = new Collection();
            columns[i] = new Collection();
            squares[i] = new Collection();
        }
    }


    private void addFieldsToCollections(){
        for(Field field : allFields){

            rows[field.getRowId()].addField(field);
            columns[field.getColumnId()].addField(field);
            squares[field.getSquareId()].addField(field);
        }
    }

    private void divideBoard(int[][] board){
        int currentRowNo = 0;
        int currentColumnNo = 0;
        int currentSquareNo = -1;
        for(int[] row:board){
            for(int fieldValue:row){

                if(currentColumnNo%3==0){
                    currentSquareNo ++ ;
                }

                Field fieldToAdd = new Field(fieldValue, currentRowNo, currentColumnNo, currentSquareNo);
                allFields.add(fieldToAdd);
                currentColumnNo ++;
            }

            currentRowNo ++;
            currentSquareNo = -1;
            currentColumnNo = 0;

            if(currentRowNo > 5){
                currentSquareNo += 6;
            }
            else if(currentRowNo > 2){
                currentSquareNo += 3;
            }
        }
    }
}
