/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.barrenland;

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

    private static int count = 0;

    private static final int TILE_SIZE = 1;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    private static final int X_TILES = WIDTH / TILE_SIZE;
    private static final int Y_TILES = HEIGHT / TILE_SIZE;

    private static Coordinate[][] grid = new Coordinate[X_TILES][Y_TILES];

    public static void main(String[] args) {
//        String[] STDIN = {"0 292 399 307"};
        String[] STDIN = {"0 0 399 599"};
//        String[] strSTDIN = {"0 29 39 30"};
//        String[] STDIN = {"0 0 39 59"};
//        String[] STDIN = {"0 0 6 11", "6 0 10 4", "10 4 15 6", "15 0 22 4", "22 2 31 8", "31 8 35 10",
//        "22 8 24 10", "13 6 16 8", "18 10 35 11", "27 11 35 45", "13 8 18 10", "12 6 13 10", "10 6 12 11", 
//        "14 10 16 25", "12 6 13 10", "3 25 17 35", "30 38 3 45", "5 35 6 45", "10 35 27 45", "0 46 39 46", "36 0 36 59"};
//        String[] STDIN = {"5 19 35 20", "5 39 35 40", "12 5 13 54", "26 5 27 54"};
//        String[] STDIN = {"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"};

        String STDOUT = findFertileLand(STDIN);
        System.out.println(STDOUT);

    }

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
//                        co.setText("B");
                        break;
                    } else {
                        co.setIsBarren(false);
                    }
                }
                grid[x][y] = co;
            }
        }

//        Starting at (0,0) 

//        grid(grid);
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
//        visitTile(grid[0][0]);

    }

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
    
    //Method to populate a barren land rectangle with all the coordinates in that space
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

// Check through grid, find first unvisited point, flood fill the unbarren area directly connected to that point, and return the total area
    private static List<Integer> checkForUnvisitedAreasAndCountFertileLand(List<Integer> land, int xVal, int yVal) {
//        long start = System.currentTimeMillis();
//        System.out.println(start);
        for (int y = yVal; y < Y_TILES; y++) {
            for (int x = xVal; x < X_TILES; x++) {
                Coordinate tile = grid[x][y];
//                if (!tile.isVisited() && ! tile.isIsBarren()) {
                if (!tile.isVisited()) {
                    int totalFertileArea = floodFill(grid, x, y);
                    land.add(totalFertileArea);
                    checkForUnvisitedAreasAndCountFertileLand(land, x, y);
                }
            }
        }
//        long end = System.currentTimeMillis();
//        System.out.println("checkForUnvisited " + (end - start));
        return land;

    }

    private static int floodFill(Coordinate[][] grid, int x, int y) {
        long start = System.currentTimeMillis();
        int count = 0; // Count of grid squares being filled/visited

        Stack<Coordinate> stack = new Stack<Coordinate>();
        stack.push(new Coordinate(x, y));

        while (!stack.isEmpty()) {
            Coordinate c = stack.pop();

//            If Coordinate c is unvisited, visit it, increase count by 1, and add neighbors to the stack;
            if (isCoordinateUnvisited(grid, c)) {
                count += 1;
                stack.push(new Coordinate(c.getX(), c.getY() - 1));
                stack.push(new Coordinate(c.getX(), c.getY() + 1));
                stack.push(new Coordinate(c.getX() - 1, c.getY()));
                stack.push(new Coordinate(c.getX() + 1, c.getY()));
            }
//            if(floodFillImageDo(grid, c, allPointsQueue)) {     
//                count += 1;
//                try {
//                    if (!grid[c.getX()][c.getY() - 1].isVisited()) {
//                        queue.push(new Coordinate(c.getX(), c.getY() - 1));
//                    }
//                    if (!grid[c.getX()][c.getY() + 1].isVisited()) {
//                        queue.push(new Coordinate(c.getX(), c.getY() + 1));
//                    }
//                    if (!grid[c.getX() - 1][c.getY()].isVisited()) {
//                        queue.push(new Coordinate(c.getX() - 1, c.getY()));
//                    }
//                    if (!grid[c.getX() + 1][c.getY()].isVisited()) {
//                        queue.push(new Coordinate(c.getX() + 1, c.getY()));
//                    }
//                } catch (IndexOutOfBoundsException e) {
//                    System.out.println("caught");
//                }
//            }

        }
//        long end = System.currentTimeMillis();
//        System.out.println("floodFillImageDo " + (end - start));
        return count;
    }

    private static boolean isCoordinateUnvisited(Coordinate[][] grid, Coordinate c) {
        long start = System.currentTimeMillis();

//        Check that Coordinate c is not outside bounds of the grid
        if (c.getX() < 0 || c.getY() < 0 || c.getX() >= X_TILES || c.getY() >= Y_TILES) {
            return false;
        }

        Coordinate coordinateToCheck = grid[c.getX()][c.getY()];

        if (coordinateToCheck.isVisited()) {
            return false;
        }

        coordinateToCheck.setVisited(true);

        long end = System.currentTimeMillis();
//        System.out.println( "floodFillImageDo " + (end - start));
        return true;
    }


    
    
    
    private static void grid(Coordinate[][] grid) {

        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Coordinate tile = grid[x][y];
                if (tile.isIsBarren()) {
                    sb.append(tile.getText());
//                    System.out.print(tile.getText().getText());
                    continue;
                }

                long numberBarrenNeighbors = getNeighbors(tile).stream().filter(t -> t.isIsBarren()).count();
                if (numberBarrenNeighbors > 0) {
//                    tile.getText().setText(String.valueOf(numberBarrenNeighbors));
//                    tile.getText().setFont(Font.font(6));
                }
                sb.append(tile.getText());
//                System.out.print(tile.getText().getText());
            }
            sb.append("\n");
//            System.out.println("");
        }

        System.out.println(sb);
    }
    
    private static List<Coordinate> getNeighbors(Coordinate tile) {
        List<Coordinate> neighbors = new ArrayList();

        int[] points = new int[]{
            -1, -1, -1, 0, -1, 1, 0, -1,
            0, 1, 1, 1, 1, 0, 1, -1};

//        when loop ends, will autoincrement so then will start at the new x
        for (int i = 0; i < points.length; i++) {
//            get dx - 
            int dx = points[i];
//            preincrement then select
            int dy = points[++i];

//            would make the tile its own class and make getters and setters then use those.... not just ".x"
            int newX = tile.getX() + dx;
            int newY = tile.getY() + dy;

//            REPLACE WITH METHOD - ISVALIDPOINT...
            if (newX >= 0 && newX < X_TILES && newY >= 0 && newY < Y_TILES) {
                neighbors.add(grid[newX][newY]);
            }
        }

        return neighbors;
    }

}

//public static List<Integer[]> fillGrid(Integer[] bounds) {
//        List<Integer[]> grid = new ArrayList<>();
//        
//        for (int i = bounds[0]; i <= bounds[2]; i++) {
//            for(int j = bounds[1]; j <= bounds[3]; j++) {
////                System.out.print("\t(" + i +"," + j + ")");
////                Create integer array to store current coordinate values
//                Integer[] coordinates = {i, j};
////                Add integer array with current coordinate values to grid
//                grid.add(coordinates);
//            }
////            System.out.println("");
//        }
//        return grid;
//    }
//public static void printRectangle(int i, int j, Integer[] STDIN) {
//        if (i >= STDIN[0] && i <= STDIN[2]) {
//            if (j >= STDIN[1] && j <= STDIN[3]) {
//                System.out.print("\t*");
//            } else {
//                System.out.print("\t(" + i + "," + j + ")");
//            }
//        } else {
//            System.out.print("\t(" + i + "," + j + ")");
//        }
//    }
//private static void floodFill(Coordinate[][] arr, int r, int c) 
//    {
//        if (r >= 0 && r < X_TILES && c >= 0 && c < Y_TILES) {
//            Coordinate co = arr[r][c];
//            if (co.getText().getText().equals("-")) {
//                count += 1;
//                co.getText().setText("*");
//                grid(arr);
//
//                floodFill(arr, r + 1, c);
//                floodFill(arr, r - 1, c);
//                floodFill(arr, r, c + 1);
//                floodFill(arr, r, c - 1);
//            }
//        } else {
//            return;
//        }  
//    }
//private static void showGrid(List<Coordinate> fullGrid, int c) {
//        Coordinate co = fullGrid.get(c);
//        if (co.getText().equals("-" )) {
//            co.setVisited(true);
////            co.setSymbol(".");
//            displayGrid(fullGrid);
//            if (c < fullGrid.size()-1) {
//                showGrid(fullGrid, c+1);
//            } else {
//                return;
//            }
////            fillGrid(arr, r - 1, c);
////            fillGrid(arr, r, c + 1);
////            fillGrid(arr, r, c - 1);
//        }
//        if (co.getText().equals("*" )) {
////            co.setVisited(true);
//            displayGrid(fullGrid);
//            showGrid(fullGrid, c+1);
//        }
////        displayGrid(fullGrid);
////
////        showGrid(fullGrid, c+1);
//    }
//    
//private static void displayGrid( List<Coordinate> fullGrid) {
//        for (int g = bounds[1];g <= bounds[3]; g++) {
//            for (Coordinate c : fullGrid) {
//                if (c.getX().equals(g)) {
//                    if (c.isIsBarren()) {
//                        c.getText().setText("*");
//                        System.out.print(c.getText());
//                    } else if (c.isIsBarren() == false && c.isVisited() == false) {
//                        c.getText().setText("-");
//                        System.out.print(c.getText());
////                        c.setVisited(true);
//                    } else if (c.isIsBarren() == false && c.isVisited() == true) {
//                        c.getText().setText(".");
//                        System.out.print(c.getText());
//                    }
//                    
//                }
//            }
//            System.out.println("");
//        }
//    }
//        
//   
//private static void visitTile(Coordinate tile) {
//        
//        if (tile.isVisited()) {
//            return;
//        } else {
//            if (! tile.isIsBarren()) {
//                count += 1;
//            }
//            tile.setVisited(true);
//            if (tile.getText().getText().isEmpty()) {
//                List<Coordinate> neighbors = getNeighbors(tile);
//                for (Coordinate neighborTile : neighbors) {
//                    visitTile(neighborTile);
//                    grid(grid);
//                }
//            }
//        }
//    }
//OLD FLOODFILL METHODS: 
//private static int floodFillImage(Coordinate[][] grid ,int x, int y, String symbol) {
//        
//        int count = 0;
//        
//        String orig = grid[x][y].getText().getText();
//
//        Queue<Point> queue = new LinkedList<Point>();
//        queue.add(new Point(x, y));
//
//        while (!queue.isEmpty()) 
//        {
//            Point p = queue.remove();
//
//            if(floodFillImageDo(grid,p.x,p.y, orig, symbol)) {     
//                count += 1;
//                queue.add(new Point(p.x,p.y - 1)); 
//                queue.add(new Point(p.x,p.y + 1)); 
//                queue.add(new Point(p.x - 1,p.y)); 
//                queue.add(new Point(p.x + 1,p.y)); 
//            }
//        }
//        
//        return count;
//    }
//
//    private static boolean floodFillImageDo(Coordinate[][] grid, int x, int y, String orig, String newSymbol) 
//    {
//        
//        if (y < 0 || x < 0 || x >= X_TILES || y >= Y_TILES) {
//            return false;
//        }
//        
//        Coordinate co = grid[x][y];
//        
//        if (co.isVisited()) {
//            return false;
//        }
//        
//        if (co.getText().getText().equals("B")) {
//            co.setVisited(true);
//            return false;
//        }
//        
//        if (! co.getText().getText().equals(orig)) {
//            return false;
//        }
//
//        co.getText().setText(newSymbol);
//        co.setVisited(true);
//        return true;
//    }
//OLD FIND FERTILE LAND 
//public static String findFertileLand(String[] strSTDIN) {
//        List<Integer> fertileLand = new ArrayList<>();
//
//
////        List of rectangle coordinate arrays
////        List<String[]> listStrSTDIN = new ArrayList<>();
//        List<Integer[]> listRectangles = getBounds(strSTDIN);
//        
////        For each rectangle coordinates, split into array of strings, convert to array of ints, add array to list of arrays of rectangles 
////        for (int h = 0; h < strSTDIN.length; h++) {
//////            System.out.println(strSTDIN[h]);
////            String[] STDIN = strSTDIN[h].split(" ");
////            Integer[] ints = new Integer[STDIN.length];
////            for (int i = 0; i < STDIN.length; i++) {
////                ints[i] = Integer.parseInt(STDIN[i]);
////            }
////            listStrSTDIN.add(STDIN);
////            listSTDIN.add(ints);
////        }
//
////        Get all coordinates for the full grid
////        List<Integer[]> fullGrid = fillGrid(bounds);
////        List<Integer[]> barrenLand = new ArrayList<>();
////        for (Integer[] array : listSTDIN) {
////            barrenLand.addAll(fillGrid(array));
////        }
////        Get all coordinates for the full grid
////        List<Coordinate> fullGrid = fillGrid(bounds);
//
//        List<Coordinate> barrenLand = new ArrayList<>();
////        Fill barrenLand list 
//        for (Integer[] array : listRectangles) {
//            barrenLand.addAll(fillGrid(array));
//        }
//
//
////        for (Coordinate coordinate : fullGrid) {
////            if (barrenLand.contains(coordinate)) {
////                coordinate.setIsBarren(true);
////            } 
////
////        }
//        
//        for(int y = 0; y < Y_TILES; y++) {
//            for(int x = 0; x < X_TILES; x++) {
//                Coordinate co = new Coordinate(x,y);
//                for (Coordinate c : barrenLand) {
//                    if (c.getX() == x && c.getY() == y ) {
//                        co.setIsBarren(true);
//                        co.getText().setText("B");
//                        break;
//                    } else {
//                        co.setIsBarren(false);
//                    }
//                }                       
//                
//                
//                grid[x][y] = co;
////                System.out.print(co.getText().getText());
////                root.getChildren().add(tile);
//            }
////            System.out.println("");
//        }
//        
//        for(int y = 0; y < Y_TILES; y++) {
//            for(int x = 0; x < X_TILES; x++) {
//                Coordinate tile = grid[x][y];
//                
////                if (barrenLand.contains(tile)) {
////                    System.out.println("HIT");
////                    tile.setIsBarren(true);
////                    tile.getText().setFont(Font.font(6));
////                    
////                }
//                if (tile.isIsBarren()) {
//                    continue;
//                }
//
//                long numberBarrenNeighbors = getNeighbors(tile).stream().filter(t -> t.isIsBarren()).count();
//                if (numberBarrenNeighbors > 0 ) {
////                    tile.getText().setText(String.valueOf(numberBarrenNeighbors));
////                    tile.getText().setFont(Font.font(6));
//                }
////                System.out.print(tile.getText().getText());
//                
////                visitTile(tile);
//                
//            }
////            System.out.println("");
//        }
//        
//        
////        grid(grid);
//        
////        floodFill(grid, 0,0);
////        floodFillImage(grid,0,0,"*");
//        
////        fertileLand.add(count);
//        
//        fertileLand = checkForUnvisited(fertileLand, 0, 0);
//        
//        Collections.sort(fertileLand);
//        
//        String STDOUT = "";
//        
//        if (! fertileLand.isEmpty()) {
//            for (Integer land : fertileLand) {
//                STDOUT += land.toString() + " ";
//            }
//        } else {
//            STDOUT = "No fertile land available.";
//        }
//        
//        return STDOUT;
////        visitTile(grid[0][0]);
//        
//        
////        showGrid(fullGrid, 0);
//        
//        
//    }
//    private static List<Integer> checkForUnvisited(List<Integer> land) {
//        
//        for(int y = 0; y < Y_TILES; y++) {
//            for(int x = 0; x < X_TILES; x++) {
//                Coordinate tile = grid[x][y];
//                if (!tile.isVisited()) {
//                    int count = floodFillImage(grid,x,y,"*");
//                    if (count > 0) {
//                        land.add(count);
//                    }
//                    checkForUnvisited(land);
//                }
//            }
//        }
//        return land;
//        
//        
//    }
//    private static List<Integer> checkForUnvisited(List<Integer> land, int xVal, int yVal, Queue<Point> queue) {
//        
//        while (!queue.isEmpty()) 
//        {
//            Point p = queue.remove();
//
//            Coordinate tile = grid[p.x][p.y];
//                if (!tile.isVisited()) {
//                    int count = floodFillImage(grid,p.x,p.y,"*");
//                    if (count > 0) {
//                        land.add(count);
//                    }
//                    checkForUnvisited(land, p.x, p.y, queue);
//                }
//        }
//        
//        return land;
//        
//        
//    }
