package XXLChess;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javafx.scene.layout.Background;

import java.awt.Font;
import java.awt.Image;
import java.io.*;
import java.util.*;

public class Camel extends Piece{
    /**
     * The black image.
    */
    public static final String bImage = "src/main/resources/XXLChess/b-camel.png";
    /**
     * The white image.
    */
    public static final String wImage = "src/main/resources/XXLChess/w-camel.png";
    
    /**
     * The moving pattern of camel at a specific position of the board.
     * @param row, The row of the piece.
     * @param col, The column of the piece.
     * @return The moving pattern with 0 = illegal, 1 = legal, and 2 = itself.
    */
    public int[][] validMove(int row, int col){
        int[][] moves = new int[14][14];
        if(row + 3 < 14){
            if(col + 1 < 14){
                moves[row + 3][col + 1] = 1;
            }
            if(col - 1 >= 0){
                moves[row + 3][col - 1] = 1;
            }
        }
        if(row - 3 >= 0){
            if(col + 1 < 14){
                moves[row - 3][col + 1] = 1;
            }
            if(col - 1 >= 0){
                moves[row - 3][col - 1] = 1;
            }
        }
        if(col + 3 < 14){
            if(row + 1 < 14){
                moves[row + 1][col + 3] = 1;
            }
            if(row - 1 >= 0){
                moves[row - 1][col + 3] = 1;
            }
        }
        if(col - 3 >= 0){
            if(row + 1 < 14){
                moves[row + 1][col - 3] = 1;
            }
            if(row - 1 >= 0){
                moves[row - 1][col - 3] = 1;
            }
        }

        moves[row][col] = 2;
        return moves;
    }
}