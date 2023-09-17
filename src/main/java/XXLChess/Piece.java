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

public abstract class Piece{
    /**
     * The moving pattern of a piece at a specific position of the board.
     * @param row, The row of the piece.
     * @param col, The column of the piece.
     * @return The moving pattern with 0 = illegal, 1 = legal, and 2 = itself.
    */
    public abstract int[][] validMove(int row, int col);
}