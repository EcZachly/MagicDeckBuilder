package com.zach.wilson.magic.app.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jakewilson on 7/25/14.
 */

public class Deck implements Serializable {



    Card[] mainBoard;
    Card[] sideBoard;
    String name;
    String creator;
    String format;

    public Card[] getMainBoard() {
        return mainBoard;
    }

    public void setMainBoard(Card[] mainBoard) {
        this.mainBoard = new Card[mainBoard.length];
        for(int i = 0; i < mainBoard.length; i++){
            this.mainBoard[i] = mainBoard[i];
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Card[] getSideBoard() {
        return sideBoard;
    }

    public void setSideBoard(Card[] sideBoard) {
        this.sideBoard = new Card[sideBoard.length];
        for(int i = 0; i < sideBoard.length; i++){
            this.sideBoard[i] = sideBoard[i];
        }
    }
}