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

public class Bishop extends Piece{
    /**
     * The black image.
    */
    public static final String bImage = "src/main/resources/XXLChess/b-bishop.png";
    /**
     * The white image.
    */
    public static final String wImage = "src/main/resources/XXLChess/w-bishop.png";
    
    /**
     * The moving pattern of bishop at a specific position of the board.
     * @param row, The row of the piece.
     * @param col, The column of the piece.
     * @return The moving pattern with 0 = illegal, 1 = legal, and 2 = itself.
    */
    public int[][] validMove(int row, int col){
        int[][] moves = new int[14][14];
        for(int i = 0; i < 14; i++){
            for(int j = 0; j < 14; j++){
                if(col - j == row - i || j - col == row - i){
                    moves[i][j] = 1;
                }
            }
        }
        
        moves[row][col] = 2;
        return moves;
    }
}