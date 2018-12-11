package model;

import java.util.ArrayList;

public class Collection {

    ArrayList<Field> listOfFieldsInThisRow;

    public Collection(){ listOfFieldsInThisRow = new ArrayList<>(); }

    public void addField(Field field){
        listOfFieldsInThisRow.add(field);
    }

    public ArrayList<Field> getListOfFieldsInThisRow() {
        return listOfFieldsInThisRow;
    }
}
