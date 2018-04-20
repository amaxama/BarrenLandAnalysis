/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.barrenland;

import java.util.Objects;

/**
 *
 * @author annamaxam
 */
public class Coordinate {
    
    private Integer x;
    private Integer y;
    private boolean isBarren;
    private boolean visited = false;
    private String text = new String();
//    private Rectangle border = new Rectangle();

    public Coordinate() {
        
    }
    
    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
//        text = "-";
//        border.setStroke(Color.CADETBLUE);
//        border.setFill(Color.DARKSEAGREEN);

//        text.setText(isBarren ? "B" : " ");

//        getChildren().addAll(border, text);
    }
    
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public boolean isIsBarren() {
        return isBarren;
    }

    public void setIsBarren(boolean isBarren) {
        this.isBarren = isBarren;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
        if(visited) {
            text ="*";
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    
    
//    public String getSymbol() {
//        return symbol;
//    }
//
//    public void setSymbol(String symbol) {
//        this.symbol = symbol;
//    }

        

//    @Override
//    public String toString() {
////        return "\t("  + x + "," + y + ')';
//        return symbol;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.x);
        hash = 79 * hash + Objects.hashCode(this.y);
        hash = 79 * hash + (this.isBarren ? 1 : 0);
        hash = 79 * hash + (this.visited ? 1 : 0);
        hash = 79 * hash + Objects.hashCode(this.text);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordinate other = (Coordinate) obj;
        if (this.isBarren != other.isBarren) {
            return false;
        }
        if (this.visited != other.visited) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
