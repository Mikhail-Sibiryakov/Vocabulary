package com.example.vocabulary.adapter;

import java.io.Serializable;
import java.util.ArrayList;

public class ListItem implements Serializable {
    private String mainString;
    private String key;
    private ArrayList<String> values;

    public ListItem(String mainString, String key, ArrayList<String> values) {
        this.mainString = mainString;
        this.key = key;
        this.values = values;
    }

    public String getMainString() {
        return mainString;
    }

    public void setMainString(String mainString) {
        this.mainString = mainString;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public ArrayList<String> getArrayList() {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.add(this.key);
        tempList.addAll(values);
        return tempList;
    }
}
