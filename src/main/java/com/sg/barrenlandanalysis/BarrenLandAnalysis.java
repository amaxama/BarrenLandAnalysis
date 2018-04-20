/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.barrenlandanalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author annamaxam
 */
public class BarrenLandAnalysis {

    private static final int TILE_SIZE = 1;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    private static final int X_TILES = WIDTH / TILE_SIZE;
    private static final int Y_TILES = HEIGHT / TILE_SIZE;

    private static Coordinate[][] grid = new Coordinate[X_TILES][Y_TILES];

    public static void main(String[] args) {
        String[] STDIN = {"0 292 399 307"};
//        String[] STDIN = {"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"};

        String STDOUT = findFertileLand(STDIN);
        System.out.println(STDOUT);

    }

    /**
     * Find total fertile land in grid based on String array of rectangle endpoints
     * @param rectangleCornersArray
     * @return String of all the fertile land area in square meters, sorted from smallest area to greatest, separated by a space
     */
    public static String findFertileLand(String[] rectangleCornersArray) {
        List<Integer> fertileLand = new ArrayList<>();

        List<Integer[]> barrenLandEndPoints = getBarrenLandCoordinates(rectangleCornersArray);

        List<Coordinate> totalBarrenLand = new ArrayList<>();
//        Fill barrenLand list 
        for (Integer[] rectangle : barrenLandEndPoints) {
            totalBarrenLand.addAll(findTotalBarrenLandForRectangle(rectangle));
        }

//        Loop through bounds of the grid filling a multidimentional array with each of the coordinate points in it
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Coordinate co = new Coordinate(x, y);
//                for each coordinate, if it's present in the totalBarrenLand list, mark that coordinate as barren and visited
                for (Coordinate c : totalBarrenLand) {
                    if (c.getX() == x && c.getY() == y) {
                        co.setIsBarren(true);
                        co.setVisited(true);
                        break;
                    } else {
                        co.setIsBarren(false);
                    }
                }
                grid[x][y] = co;
            }
        }

        fertileLand = checkForUnvisitedAreasAndCountFertileLand(fertileLand, 0, 0);

        Collections.sort(fertileLand);

        String STDOUT = "";

        if (!fertileLand.isEmpty()) {
            for (Integer land : fertileLand) {
                STDOUT += land.toString() + " ";
            }
        } else {
            STDOUT = "No fertile land available.";
        }

        return STDOUT;

    }

    /**
     * Get list of integer arrays for each string of rectangle endpoints
     * @param rectangleCornersArray
     * @return Integer array of rectangle endpoint arrays
     */
    private static List<Integer[]> getBarrenLandCoordinates(String[] rectangleCornersArray) {

//        List of arrays of rectangle points 
        List<Integer[]> rectanglePoints = new ArrayList<>();
//        For each rectangle coordinates, split into array of strings, convert to array of ints, add array to list of arrays of rectangles 
        for (int h = 0; h < rectangleCornersArray.length; h++) {
            String[] strRectangleCorner = rectangleCornersArray[h].split(" ");
            Integer[] intRectangleCorner = new Integer[strRectangleCorner.length];
            for (int i = 0; i < strRectangleCorner.length; i++) {
                intRectangleCorner[i] = Integer.parseInt(strRectangleCorner[i]);
            }
            rectanglePoints.add(intRectangleCorner);
        }

        return rectanglePoints;
    }

    /**
     * Populate a barren land rectangle with all the coordinates in that space
     * @param bounds
     * @return List of coordinates in barren land rectangle
     */
    private static List<Coordinate> findTotalBarrenLandForRectangle(Integer[] bounds) {
        List<Coordinate> allBarrenLandCoordinates = new ArrayList<>();

//        Loop through endpoints and create new coordinate for each coordinate within rectangle endpoints - then add to allBarrenLandCoordinates list
        for (int i = bounds[0]; i <= bounds[2]; i++) {
            for (int j = bounds[1]; j <= bounds[3]; j++) {
                Coordinate coordinates = new Coordinate(i, j);
                allBarrenLandCoordinates.add(coordinates);
            }
        }
        return allBarrenLandCoordinates;
    }

    /**
     * Check through grid, find first unvisited point, flood fill the fertile area directly connected to that point, and return the total area
     * @param land
     * @param xVal
     * @param yVal
     * @return List of area of each fertile land plot
     */
    private static List<Integer> checkForUnvisitedAreasAndCountFertileLand(List<Integer> land, int xVal, int yVal) {
        for (int y = yVal; y < Y_TILES; y++) {
            for (int x = xVal; x < X_TILES; x++) {
                Coordinate tile = grid[x][y];
                if (!tile.isVisited()) {
                    int totalFertileArea = floodFill(grid, x, y);
                    land.add(totalFertileArea);
                    checkForUnvisitedAreasAndCountFertileLand(land, x, y);
                }
            }
        }
        return land;

    }

    /**
     * Visit all coordinates in a fertile land space and find the area
     * @param grid
     * @param x
     * @param y
     * @return area (int) of the current fertile land space
     */
    private static int floodFill(Coordinate[][] grid, int x, int y) {
        int count = 0; // Count of grid squares being filled/visited

        Stack<Coordinate> stack = new Stack<Coordinate>();
        stack.push(new Coordinate(x, y));

        while (!stack.isEmpty()) {
            Coordinate c = stack.pop();

//         If Coordinate c is unvisited, visit it, increase count by 1, and add neighbors to the stack;
            if(isCoordinateUnvisited(grid, c)) {     
                count += 1;
                    if ( c.getY() -1 >= 0 && !grid[c.getX()][c.getY() - 1].isVisited() ) {
                        stack.push(new Coordinate(c.getX(), c.getY() - 1));
                    }
                    if (c.getY() +1 < Y_TILES && !grid[c.getX()][c.getY() + 1].isVisited()) {
                        stack.push(new Coordinate(c.getX(), c.getY() + 1));
                    }
                    if ( c.getX() -1 >= 0 && !grid[c.getX() - 1][c.getY()].isVisited()) {
                        stack.push(new Coordinate(c.getX() - 1, c.getY()));
                    }
                    if (c.getX() +1 < X_TILES && !grid[c.getX() + 1][c.getY()].isVisited()) {
                        stack.push(new Coordinate(c.getX() + 1, c.getY()));
                    }
            }

        }
        return count;
    }

    /**
     * Check if coordinate has been visited already - if not, switch visited to true
     * @param grid
     * @param c
     * @return boolean value representing whether coordinate c has been visited or not
     */
    private static boolean isCoordinateUnvisited(Coordinate[][] grid, Coordinate c) {

//        Check that Coordinate c is not outside bounds of the grid
        if (c.getX() < 0 || c.getY() < 0 || c.getX() >= X_TILES || c.getY() >= Y_TILES) {
            return false;
        }

        Coordinate coordinateToCheck = grid[c.getX()][c.getY()];

        if (coordinateToCheck.isVisited()) {
            return false;
        }

        coordinateToCheck.setVisited(true);

        return true;
    }

}