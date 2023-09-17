package XXLChess;

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import java.awt.Font;
import java.io.*;
import java.util.*;
import java.lang.Math;

public class App extends PApplet {

    /**
     * The size of a sprite.
    */
    public final int SPRITESIZE = 480;
    /**
     * The size of a cell.
    */
    public final int CELLSIZE = 48;
    /**
     * The size of the side bar.
    */
    public final int SIDEBAR = 120;
    /**
     * The width of the board.
    */
    public final int BOARD_WIDTH = 14;

    /**
     * The width of the window.
    */
    public int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    /**
     * The height of the window.
    */
    public int HEIGHT = BOARD_WIDTH*CELLSIZE;

    /**
     *The framerate. 
    */
    public final int FPS = 60;

    /**
     * The path of the config.
    */
    public String configPath;

    /**
     * A hash map that stores the image of each piece.
    */
    public HashMap<Character, PImage> pieces = new HashMap<Character, PImage>();
    /**
     * A hash map that stores the piece type of each character.
    */
    public HashMap<Character, Piece> pieceCode = new HashMap<Character, Piece>();
    /**
     * A hash map that stores the moving pattern of each piece.
    */
    public HashMap<Character, int[][]> pieceMoves= new HashMap<Character, int[][]>();
    /**
     * The board.
    */
    public char[][] board = new char[14][14];

    /**
     * The time left for player 1.
    */
    public int player1Time;
    /**
     * The time left for player 2.
    */
    public int player2Time;
    /**
     * The increment time for player 1.
    */
    public int player1Increment;
    /**
     * The increment time for player 2.
    */
    public int player2Increment;
    /**
     * The frame count.
    */
    public int frameCount = 0;
    /**
     * The colour of player 1.
    */
    public String playerColour;

    /**
     * The lower pawn.
    */
    public WhitePawn WhitePawn = new WhitePawn();
    /**
     * The upper pawn.
    */
    public BlackPawn BlackPawn = new BlackPawn();
    /**
     * The camel.
    */
    public Camel Camel = new Camel();
    /**
     * The chancellor.
    */
    public Chancellor Chancellor = new Chancellor();
    /**
     * The amazon.
    */
    public Amazon Amazon = new Amazon();
    /**
     * The archbishop.
    */
    public Archbishop Archbishop = new Archbishop();
    /**
     * The rook.
    */
    public Rook Rook = new Rook();
    /**
     * The bishop.
    */
    public Bishop Bishop = new Bishop();
    /**
     * The queen.
    */
    public Queen Queen = new Queen();
    /**
     * The guard.
    */
    public Guard Guard = new Guard();
    /**
     * The knight.
    */
    public Knight Knight = new Knight();
    /**
     * The king.
    */
    public King King = new King();

    /**
     * The row selected.
    */
    public int rowChosen = -1;
    /**
     * The column selected.
    */
    public int colChosen = -1;
    /**
     * The row where the last piece moved was at initially.
    */
    public int lastRow = -1;
    /**
     * The column where the last piece moved was at initially.
    */
    public int lastCol = -1;
    /**
     * The row where the last piece moved is at.
    */
    public int lastPieceRow = -1;
    /**
     * The column where the last piece moved is at.
    */
    public int lastPieceCol = -1;
    /**
     * Whether a piece is selected.
    */
    public boolean pieceSelected = false;
    /**
     * Whether a move has been made.
    */
    public boolean moved = false;
    /**
     * Whether it is player 1's turn.
    */
    public boolean player1Turn = true;
    /**
     * Whether the game has ended.
    */
    public boolean ended = false;
    /**
     * Whether the king at the bottom has been moved.
    */
    public boolean wKingMoved = false;
    /**
     * Whether the left rook at the bottom has been moved.
    */
    public boolean wLRookMoved = false;
    /**
     * Whether the right rook at the bottom has been moved.
    */
    public boolean wRRookMoved = false;
    /**
     * Whether the king at the top has been moved.
    */
    public boolean bKingMoved = false;
    /**
     * Whether the left rook at the top has been moved.
    */
    public boolean bLRookMoved = false;
    /**
     * Whether the right rook at the top has been moved.
    */
    public boolean bRRookMoved = false;
    /**
     * Whether the AI is on or not.
    */
    public boolean autoOn = false;
    /**
     * Whether to flash the king's tile.
    */
    public boolean flash = false;
    /**
     * The number of flashes completed.
    */
    public int flashCount = 0;
    /**
     * The legal move of the piece selected.
    */
    public int[][] path;
    /**
     * An array list storing all the pieces that are movable for the AI.
    */
    public ArrayList<Character> movables = new ArrayList<Character>();
    /**
     * The random generator.
    */
    public Random pick = new Random();

    /**
     * Constructor of the app 
    */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */

    public void setup() {
        frameRate(FPS);
        noStroke();
        
        // load config
        JSONObject config = loadJSONObject(new File(this.configPath));
        JSONObject time = config.getJSONObject("time_controls");
        player1Time = time.getJSONObject("player").getInt("seconds");
        player2Time = time.getJSONObject("cpu").getInt("seconds");
        player1Increment = time.getJSONObject("player").getInt("increment");
        player2Increment = time.getJSONObject("cpu").getInt("increment");
        playerColour = config.getString("player_colour");

        if(playerColour.equals("white")){
          PImage wCamel = loadImage(Camel.wImage);
          wCamel.resize(48, 48);
          this.pieces.put('c', wCamel);
          this.pieceCode.put('c', Camel);
          PImage bCamel = loadImage(Camel.bImage);
          bCamel.resize(48, 48);
          this.pieces.put('C', bCamel);
          this.pieceCode.put('C', Camel);

          PImage wRook = loadImage(Rook.wImage);
          wRook.resize(48, 48);
          this.pieces.put('r', wRook);
          this.pieceCode.put('r', Rook);
          PImage bRook = loadImage(Rook.bImage);
          bRook.resize(48, 48);
          this.pieces.put('R', bRook);
          this.pieceCode.put('R', Rook);

          PImage wAmazon = loadImage(Amazon.wImage);
          wAmazon.resize(48, 48);
          this.pieces.put('a', wAmazon);
          this.pieceCode.put('a', Amazon);
          PImage bAmazon = loadImage(Amazon.bImage);
          bAmazon.resize(48, 48);
          this.pieces.put('A', bAmazon);
          this.pieceCode.put('A', Amazon);

          PImage wArchbishop = loadImage(Archbishop.wImage);
          wArchbishop.resize(48, 48);
          this.pieces.put('h', wArchbishop);
          this.pieceCode.put('h', Archbishop);
          PImage bArchbishop = loadImage(Archbishop.bImage);
          bArchbishop.resize(48, 48);
          this.pieces.put('H', bArchbishop);
          this.pieceCode.put('H', Archbishop);

          PImage wBishop = loadImage(Bishop.wImage);
          wBishop.resize(48, 48);
          this.pieces.put('b', wBishop);
          this.pieceCode.put('b', Bishop);
          PImage bBishop = loadImage(Bishop.bImage);
          bBishop.resize(48, 48);
          this.pieces.put('B', bBishop);
          this.pieceCode.put('B', Bishop);

          PImage wChancellor = loadImage(Chancellor.wImage);
          wChancellor.resize(48, 48);
          this.pieces.put('e', wChancellor);
          this.pieceCode.put('e', Chancellor);
          PImage bChancellor = loadImage(Chancellor.bImage);
          bChancellor.resize(48, 48);
          this.pieces.put('E', bChancellor);
          this.pieceCode.put('E', Chancellor);

          PImage wKing = loadImage(King.wImage);
          wKing.resize(48, 48);
          this.pieces.put('k', wKing);
          this.pieceCode.put('k', King);
          PImage bKing = loadImage(King.bImage);
          bKing.resize(48, 48);
          this.pieces.put('K', bKing);
          this.pieceCode.put('K', King);

          PImage wKnight = loadImage(Knight.wImage);
          wKnight.resize(48, 48);
          this.pieces.put('n', wKnight);
          this.pieceCode.put('n', Knight);
          PImage bKnight = loadImage(Knight.bImage);
          bKnight.resize(48, 48);
          this.pieces.put('N', bKnight);
          this.pieceCode.put('N', Knight);

          PImage wGuard = loadImage(Guard.wImage);
          wGuard.resize(48, 48);
          this.pieces.put('g', wGuard);
          this.pieceCode.put('g', Guard);
          PImage bGuard = loadImage(Guard.bImage);
          bGuard.resize(48, 48);
          this.pieces.put('G', bGuard);
          this.pieceCode.put('G', Guard);

          PImage wPawn = loadImage(WhitePawn.wImage);
          wPawn.resize(48, 48);
          this.pieces.put('p', wPawn);
          this.pieceCode.put('p', WhitePawn);
          PImage bPawn = loadImage(BlackPawn.bImage);
          bPawn.resize(48, 48);
          this.pieces.put('P', bPawn);
          this.pieceCode.put('P', BlackPawn);

          PImage wQueen = loadImage(Queen.wImage);
          wQueen.resize(48, 48);
          this.pieces.put('q', wQueen);
          this.pieceCode.put('q', Queen);
          PImage bQueen = loadImage(Queen.bImage);
          bQueen.resize(48, 48);
          this.pieces.put('Q', bQueen);
          this.pieceCode.put('Q', Queen);
        }else{
          this.player1Turn = false;
          PImage wCamel = loadImage(Camel.bImage);
          wCamel.resize(48, 48);
          this.pieces.put('c', wCamel);
          this.pieceCode.put('c', Camel);
          PImage bCamel = loadImage(Camel.wImage);
          bCamel.resize(48, 48);
          this.pieces.put('C', bCamel);
          this.pieceCode.put('C', Camel);

          PImage wRook = loadImage(Rook.bImage);
          wRook.resize(48, 48);
          this.pieces.put('r', wRook);
          this.pieceCode.put('r', Rook);
          PImage bRook = loadImage(Rook.wImage);
          bRook.resize(48, 48);
          this.pieces.put('R', bRook);
          this.pieceCode.put('R', Rook);

          PImage wAmazon = loadImage(Amazon.bImage);
          wAmazon.resize(48, 48);
          this.pieces.put('a', wAmazon);
          this.pieceCode.put('a', Amazon);
          PImage bAmazon = loadImage(Amazon.wImage);
          bAmazon.resize(48, 48);
          this.pieces.put('A', bAmazon);
          this.pieceCode.put('A', Amazon);

          PImage wArchbishop = loadImage(Archbishop.bImage);
          wArchbishop.resize(48, 48);
          this.pieces.put('h', wArchbishop);
          this.pieceCode.put('h', Archbishop);
          PImage bArchbishop = loadImage(Archbishop.wImage);
          bArchbishop.resize(48, 48);
          this.pieces.put('H', bArchbishop);
          this.pieceCode.put('H', Archbishop);

          PImage wBishop = loadImage(Bishop.bImage);
          wBishop.resize(48, 48);
          this.pieces.put('b', wBishop);
          this.pieceCode.put('b', Bishop);
          PImage bBishop = loadImage(Bishop.wImage);
          bBishop.resize(48, 48);
          this.pieces.put('B', bBishop);
          this.pieceCode.put('B', Bishop);

          PImage wChancellor = loadImage(Chancellor.bImage);
          wChancellor.resize(48, 48);
          this.pieces.put('e', wChancellor);
          this.pieceCode.put('e', Chancellor);
          PImage bChancellor = loadImage(Chancellor.wImage);
          bChancellor.resize(48, 48);
          this.pieces.put('E', bChancellor);
          this.pieceCode.put('E', Chancellor);

          PImage wKing = loadImage(King.bImage);
          wKing.resize(48, 48);
          this.pieces.put('k', wKing);
          this.pieceCode.put('k', King);
          PImage bKing = loadImage(King.wImage);
          bKing.resize(48, 48);
          this.pieces.put('K', bKing);
          this.pieceCode.put('K', King);

          PImage wKnight = loadImage(Knight.bImage);
          wKnight.resize(48, 48);
          this.pieces.put('n', wKnight);
          this.pieceCode.put('n', Knight);
          PImage bKnight = loadImage(Knight.wImage);
          bKnight.resize(48, 48);
          this.pieces.put('N', bKnight);
          this.pieceCode.put('N', Knight);

          PImage wGuard = loadImage(Guard.bImage);
          wGuard.resize(48, 48);
          this.pieces.put('g', wGuard);
          this.pieceCode.put('g', Guard);
          PImage bGuard = loadImage(Guard.wImage);
          bGuard.resize(48, 48);
          this.pieces.put('G', bGuard);
          this.pieceCode.put('G', Guard);

          PImage wPawn = loadImage(WhitePawn.bImage);
          wPawn.resize(48, 48);
          this.pieces.put('p', wPawn);
          this.pieceCode.put('p', WhitePawn);
          PImage bPawn = loadImage(BlackPawn.wImage);
          bPawn.resize(48, 48);
          this.pieces.put('P', bPawn);
          this.pieceCode.put('P', BlackPawn);

          PImage wQueen = loadImage(Queen.bImage);
          wQueen.resize(48, 48);
          this.pieces.put('q', wQueen);
          this.pieceCode.put('q', Queen);
          PImage bQueen = loadImage(Queen.wImage);
          bQueen.resize(48, 48);
          this.pieces.put('Q', bQueen);
          this.pieceCode.put('Q', Queen);
        }

        String[] lines = loadStrings(config.getString("layout"));
        for (int i = 0; i < lines.length; i++) {
          String line = lines[i];
          for (int j = 0; j < 14; j++) {
            if(line.length() == 14){
              this.board[i][j] = line.charAt(j);
            }else{
              this.board[i][j] = ' ';
            }
          }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     * If key = r and the game has ended, reset the game.
     * If key = e or ESC, resign and end the game.
     * If key = a, turn on/off the AI.
    */
    public void keyPressed(){
        if(key == 'r' && ended){
          this.moved = false;
          if(playerColour.equals("white")){
            this.player1Turn = true;
          }else{
            this.player1Turn = false;
          }
          this.pieceSelected = false;
          this.ended = false;
          this.rowChosen = -1;
          this.colChosen = -1;
          this.lastRow = -1;
          this.lastCol = -1;
          this.lastPieceRow = -1;
          this.lastPieceCol = -1;
          this.wKingMoved = false;
          this.wLRookMoved = false;
          this.wRRookMoved = false;
          this.bKingMoved = false;
          this.bLRookMoved = false;
          this.bRRookMoved = false;
          this.autoOn = false;
          this.frameCount = 0;
          this.path = null;
          this.flash = false;
          this.flashCount = 0;

          JSONObject config = loadJSONObject(new File(this.configPath));
          JSONObject time = config.getJSONObject("time_controls");
          player1Time = time.getJSONObject("player").getInt("seconds");
          player2Time = time.getJSONObject("cpu").getInt("seconds");

          String[] lines = loadStrings(config.getString("layout"));
          for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < 14; j++) {
              if(line.length() == 14){
                this.board[i][j] = line.charAt(j);
              }else{
                this.board[i][j] = ' ';
              }
            }
          }
        }

        if((key == ESC || key == 'e')&& !ended){
          key = 0;
          fill(168, 168, 168);
          rect(690, 325, 50, 16);
          if(player1Turn){
            if(autoOn){
              textSize(15);
              fill(255, 255, 255);
              text("You resigned", 680, 320);
              textSize(11);
              text("press r to restart", 680, 400);
            }else{
              textSize(15);
              fill(255, 255, 255);
              if(playerColour.equals("white")){
                text("White resigned", 680, 320);
              }else{
                text("Black resigned", 680, 320);
              }
              textSize(11);
              text("press r to restart", 680, 400);
            }
          }else{
            if(autoOn){
              textSize(15);
              fill(255, 255, 255);
              text("You resigned", 680, 320);
              textSize(11);
              text("press r to restart", 680, 400);
            }else{
              textSize(15);
              fill(255, 255, 255);
              if(playerColour.equals("white")){
                text("Black resigned", 680, 320);
              }else{
                text("White resigned", 680, 320);
              }
              textSize(11);
              text("press r to restart", 680, 400);
            }
          }
          this.ended = true;
        }

        if(key == 'a'){
          if(!autoOn){
            this.autoOn = true;
          }else{
            this.autoOn = false;
          }
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
     * @deprecated
    */

    public void keyReleased(){

    }

    /**
     * Detects mouse pressed event.
     * Change the player's turn and tell the game to flash on the king's tile when a move that does not prevent king from being checked is called.
     * @param e, The mouse event.
    */

    @Override
    public void mousePressed(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      int row = (int) Math.floor(y / CELLSIZE);
      int col = (int) Math.floor(x / CELLSIZE);
      
      if(col < 14 && row < 14){
        if(!pieceSelected){
          if(this.board[row][col] != ' '){
            if(player1Turn && Character.isLowerCase(this.board[row][col])){
              this.rowChosen = row;
              this.colChosen = col;
              this.pieceSelected = true;
              this.moved = false;
              this.path = vmCheckLimit(rowChosen, colChosen);
            }else if(!player1Turn && Character.isUpperCase(this.board[row][col]) && !autoOn){
              this.rowChosen = row;
              this.colChosen = col;
              this.pieceSelected = true;
              this.moved = false;
              this.path = vmCheckLimit(rowChosen, colChosen);
            }
          }
        }else{
          if((rowChosen != row || colChosen != col) && vmCheckLimit(rowChosen, colChosen)[row][col] == 0 && getValidMove(rowChosen, colChosen, this.board)[row][col] == 1){
            this.flash = true;
          }
          if((rowChosen != row || colChosen != col) && vmCheckLimit(rowChosen, colChosen)[row][col] == 1){       //only move pieces when the second coordinate is not at the initial point
            movePiece(rowChosen, colChosen, row, col);
            this.lastPieceRow = row;
            this.lastPieceCol = col;
            
            //switch turn
            if(moved){
              if(player1Turn){
                this.player2Time += this.player2Increment;
                if(this.player2Time > 180){
                  this.player2Time = 180;
                }
                this.frameCount = 0;
                this.player1Turn = false;
              }else{
                this.player1Time += this.player1Increment;
                if(this.player1Time > 180){
                  this.player1Time = 180;
                }
                this.frameCount = 0;
                this.player1Turn = true;
              }
            }
          }else{
            this.pieceSelected = false;
            this.moved = false;
          }
        }
      }
    }  

    /**
     * detects a mouse dragged event.
     * @param e, the mouse event.
     * @deprecated
    */

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * Draw all elements in the game by current frame. 
     * Draw and run the timer by counting frames.
     * Display any message in certain conditions.
    */
    public void draw() {
      if(!ended){
        this.frameCount++;
        background(168, 168, 168);
        drawBoard();
        drawPath();

        if(player1Turn){
          if(frameCount == 60){
            this.player1Time = max(0, this.player1Time - 1);
            this.frameCount = 0;
          }
          if(this.player1Time == 0){
            if(autoOn){
              textSize(15);
              fill(255, 255, 255);
              text("You lost on \ntime!", 680, 320);
              textSize(11);
              text("press r to restart", 680, 400);
            }else{
              textSize(15);
              fill(255, 255, 255);
              if(playerColour.equals("white")){
                text("Black won on \ntime!", 680, 320);
              }else{
                text("White won on \ntime!", 680, 320);
              }
              textSize(11);
              text("press r to restart", 680, 400);
            }
            this.ended = true;
          }
          if(!flash){
            drawCheck('k');
          }else{
            textSize(15);
            fill(255, 255, 255);
            text("You must\ndefend\nyour king!", 690, 340);
            if(frameCount % 60 >= 30 && flashCount < 4){
              drawCheck('k');
            }
            if(frameCount == 0){
              this.flashCount += 1;
            }
            if(flashCount == 3){
              this.flash = false;
              this.flashCount = 0;
            }
          }
          if(checkmate('k')){
            if(autoOn){
              textSize(15);
              fill(255, 255, 255);
              text("You lost by \ncheckmate!", 680, 320);
              textSize(11);
              text("press r to restart", 680, 400);
            }else{
              textSize(15);
              fill(255, 255, 255);
              if(playerColour.equals("white")){
                text("Black won by \ncheckmate!", 680, 320);
              }else{
                text("White won by \ncheckmate!", 680, 320);
              }
              textSize(11);
              text("press r to restart", 680, 400);
            }
            this.ended = true;
          }
        }else{
          if(frameCount == 60){
            this.player2Time = max(0, this.player2Time - 1);
            this.frameCount = 0;
          }
          if(this.player2Time == 0){
            if(autoOn){
              textSize(15);
              fill(255, 255, 255);
              text("You won on \ntime!", 680, 320);
              textSize(11);
              text("press r to restart", 680, 400);
            }else{
              textSize(15);
              fill(255, 255, 255);
              if(playerColour.equals("white")){
                text("White won on \ntime!", 680, 320);
              }else{
                text("Black won on \ntime!", 680, 320);
              }
              textSize(11);
              text("press r to restart", 680, 400);
            }
            this.ended = true;
          }
          if(!flash){
            drawCheck('K');
          }else{
            textSize(15);
            fill(255, 255, 255);
            text("You must\ndefend\nyour king!", 690, 340);
            if(frameCount % 60 >= 30 && flashCount < 4){
              drawCheck('K');
            }
            if(frameCount == 0){
              this.flashCount += 1;
            }
            if(flashCount == 3){
              this.flash = false;
              this.flashCount = 0;
            }
          }
          if(checkmate('K')){
            if(autoOn){
              textSize(15);
              fill(255, 255, 255);
              text("You won by \ncheckmate!", 680, 320);
              textSize(11);
              text("press r to restart", 680, 400);
            }else{
              textSize(15);
              fill(255, 255, 255);
              if(playerColour.equals("white")){
                text("White won by \ncheckmate!", 680, 320);
              }else{
                text("Black won by \ncheckmate!", 680, 320);
              }
              textSize(11);
              text("press r to restart", 680, 400);
            }
            this.ended = true;
          }
          if(autoOn && !ended){
            autoMove();
          }
        }
        textSize(11);
        fill(255, 255, 255);
        if(autoOn){
          text("AI: On \npress a to turn \non/off AI", 690, 70);
        }else{
          text("AI: Off \npress a to turn \non/off AI", 690, 70);
        }
        textSize(32);
        fill(255, 255, 255);
        text(timeString(player1Time), 690, 655);
        text(timeString(player2Time), 690, 40);
        drawPieces();
      }
    }
	
	  // Add any additional methods or attributes you want. Please put classes in different files.

    /**
     * Convert frames to time.
     * @param time, The number of frames.
     * @return Formatted time string for human to read.
    */
    public String timeString(int time){
      int minutes = time / 60;
      int seconds = (time % 60);
      return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Moves the piece selected, perform castling/ pawn to queen/ change the status of kings and rooks (moved or not).
     * @param startX, The starting row.
     * @param startY, The starting column.
     * @param endX, The destination row.
     * @param endY, The destination column.
    */
    public void movePiece(int startX, int startY, int endX, int endY){
      if(this.board[endX][endY] == ' '){
        this.board[endX][endY] = this.board[startX][startY];

        //castling
        if(this.board[startX][startY] == 'k'){
          this.wKingMoved = true;
          if(endY == startY - 2){
            this.board[13][endY + 1] = this.board[13][0];
            this.board[13][0] = ' ';
          }
          if(endY == startY + 2){
            this.board[13][endY - 1] = this.board[13][13];
            this.board[13][13] = ' ';
          }
        }
        if(this.board[startX][startY] == 'K'){
          this.bKingMoved = true;
          if(endY == startY - 2){
            this.board[0][endY + 1] = this.board[0][0];
            this.board[0][0] = ' ';
          }
          if(endY == startY + 2){
            this.board[0][endY - 1] = this.board[0][13];
            this.board[0][13] = ' ';
          }
        }

        //rook moved or not
        if(this.board[startX][startY] == 'r'){
          if(startY == 0){
            this.wLRookMoved = true;
          }else{
            this.wRRookMoved = true;
          }
        }
        if(this.board[startX][startY] == 'R'){
          if(startY == 0){
            this.bRRookMoved = true;
          }else{
            this.bLRookMoved = true;
          }
        }

        //pawn to queen
        if(this.board[startX][startY] == 'p'){
          if(endX < 7){
            this.board[endX][endY] = 'q';
          }
        }
        if(this.board[startX][startY] == 'P'){
          if(endX >= 7){
            this.board[endX][endY] = 'Q';
          }
        }

        this.board[startX][startY] = ' ';
        this.pieceSelected = false;
        this.moved = true;
      }

      if(isEnemy(this.board[startX][startY], this.board[endX][endY])){
        this.board[endX][endY] = this.board[startX][startY];
        
        //castling
        if(this.board[startX][startY] == 'k'){
          this.wKingMoved = true;
        }
        if(this.board[startX][startY] == 'K'){
          this.bKingMoved = true;
        }

        //rook moved or not
        if(this.board[startX][startY] == 'r'){
          if(startY == 0){
            this.wLRookMoved = true;
          }else{
            this.wRRookMoved = true;
          }
        }
        if(this.board[startX][startY] == 'R'){
          if(startY == 0){
            this.bRRookMoved = true;
          }else{
            this.bLRookMoved = true;
          }
        }

        //pawn to queen
        if(this.board[startX][startY] == 'p'){
          if(endX < 7){
            this.board[endX][endY] = 'q';
          }        
        }
        if(this.board[startX][startY] == 'P'){
          if(endX >= 7){
            this.board[endX][endY] = 'Q';
          }
        }
        this.board[startX][startY] = ' ';
        this.pieceSelected = false;
        this.moved = true;
      }
    }
    
    /**
     * Highlighting the path (the previous moved tile and its previous position) and legal moves.
    */
    public void drawPath(){
      if(colChosen != -1 && rowChosen != -1 && moved){
        this.lastRow = rowChosen;
        this.lastCol = colChosen;
      }
      if(this.lastRow != -1 && this.lastCol != -1){
        if((this.lastRow + this.lastCol) % 2 == 0){
          fill(210, 210, 0);
        }else{
          fill(170, 170, 0);
        }
        rect(this.lastCol * CELLSIZE, this.lastRow * CELLSIZE, CELLSIZE, CELLSIZE);

        if((this.lastPieceRow + this.lastPieceCol) % 2 == 0){
          fill(210, 210, 0);
        }else{
          fill(170, 170, 0);
        }
        rect(this.lastPieceCol * CELLSIZE, this.lastPieceRow * CELLSIZE, CELLSIZE, CELLSIZE);   
      }
      if(pieceSelected){
        fill(102, 153, 0);
        rect(colChosen * CELLSIZE, rowChosen * CELLSIZE, CELLSIZE, CELLSIZE);
        drawValidMove();
      }
    }

    /**
     * Draw the board.
    */
    public void drawBoard() {
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if((row + col) % 2 == 0){
            fill(255, 228, 181);
          }else{
            fill(205, 133, 63);
          }
          rect(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
        }
      }
    }

    /**
     * Draw the pieces.
    */
    public void drawPieces() {
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          char piece = this.board[row][col];
          if (piece != ' ') {
            PImage img = pieces.get(piece);
            image(img, col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
          }
        }
      }
    }

    /**
     * Modify the legal move for each piece in special cases (blocking, capturing).
     * Identify the piece from the row and column.
     * Does not take check conditions into account.
     * @param rowChosen, The row selected.
     * @param colChosen, The column selected.
     * @param board, The board.
     * @return A 2D array showing the legal moves with 0 = illegal, 1 = legal, 2 = the piece itself.
    */
    public int[][] getValidMove(int rowChosen, int colChosen, char[][] board){
      Piece p = pieceCode.get(board[rowChosen][colChosen]);
      int[][] vm = p.validMove(rowChosen, colChosen);

      //for white king castling
      if(board[rowChosen][colChosen] == 'k'){
        if(!wKingMoved && !wLRookMoved){
          if(board[rowChosen][colChosen - 6] == ' ' && board[rowChosen][colChosen - 5] == ' ' && board[rowChosen][colChosen - 4] == ' ' && board[rowChosen][colChosen - 3] == ' ' && board[rowChosen][colChosen - 2] == ' ' && board[rowChosen][colChosen - 1] == ' '){
            vm[rowChosen][colChosen - 2] = 1;
          }
        }
        if(!wKingMoved && !wRRookMoved){
          if(board[rowChosen][colChosen + 5] == ' ' && board[rowChosen][colChosen + 4] == ' ' && board[rowChosen][colChosen + 3] == ' ' && board[rowChosen][colChosen + 2] == ' ' && board[rowChosen][colChosen + 1] == ' '){
            vm[rowChosen][colChosen + 2] = 1;
          }
        }
      }

      //for black king castling
      if(board[rowChosen][colChosen] == 'K'){
        if(!bKingMoved && !bLRookMoved){
          if(board[rowChosen][colChosen - 6] == ' ' && board[rowChosen][colChosen - 5] == ' ' && board[rowChosen][colChosen - 4] == ' ' && board[rowChosen][colChosen - 3] == ' ' && board[rowChosen][colChosen - 2] == ' ' && board[rowChosen][colChosen - 1] == ' '){
            vm[rowChosen][colChosen - 2] = 1;
          }
        }
        if(!bKingMoved && !bRRookMoved){
          if(board[rowChosen][colChosen + 5] == ' ' && board[rowChosen][colChosen + 4] == ' ' && board[rowChosen][colChosen + 3] == ' ' && board[rowChosen][colChosen + 2] == ' ' && board[rowChosen][colChosen + 1] == ' '){
            vm[rowChosen][colChosen + 2] = 1;
          }
        }
      }

      //for white pawn
      if(p == WhitePawn){
        if(colChosen + 1 < 14){
          if(board[rowChosen - 1][colChosen + 1] != ' ' && Character.isUpperCase(board[rowChosen - 1][colChosen + 1])){
            vm[rowChosen - 1][colChosen + 1] = 1;
          }
        }
        if(colChosen - 1 >= 0){
          if(board[rowChosen - 1][colChosen - 1] != ' ' && Character.isUpperCase(board[rowChosen - 1][colChosen - 1])){
            vm[rowChosen - 1][colChosen - 1] = 1;
          }
        }
        if(rowChosen == 12 && board[rowChosen - 2][colChosen] == ' ' && board[rowChosen - 1][colChosen] == ' '){
          vm[rowChosen - 2][colChosen] = 1;
        }
        if(board[rowChosen - 1][colChosen] != ' '){
          vm[rowChosen - 1][colChosen] = 0;
        }
      }

      //for black pawn since the direction is the opposite
      if(p == BlackPawn){
        if(colChosen + 1 < 14){
          if(board[rowChosen + 1][colChosen + 1] != ' ' && Character.isLowerCase(board[rowChosen + 1][colChosen + 1])){
            vm[rowChosen + 1][colChosen + 1] = 1;
          }
        }
        if(colChosen - 1 >= 0){
          if(board[rowChosen + 1][colChosen - 1] != ' ' && Character.isLowerCase(board[rowChosen + 1][colChosen - 1])){
            vm[rowChosen + 1][colChosen - 1] = 1;
          }
        }
        if(rowChosen == 1 && board[rowChosen + 2][colChosen] == ' ' && board[rowChosen + 1][colChosen] == ' '){
          vm[rowChosen + 2][colChosen] = 1;
        }
        if(board[rowChosen + 1][colChosen] != ' '){
          vm[rowChosen + 1][colChosen] = 0;
        }
      }

      if(p != WhitePawn && p != BlackPawn){
        for(int row = 0; row < 14; row++){
          for(int col = 0; col < 14; col++){

            //cut the path if it is blocked
            if(vm[row][col] == 1 && board[row][col] != ' '){

              //for vertical and horizontal
              if(col < colChosen){
                for(int backCol = col; backCol >= 0; backCol--){
                  vm[row][backCol] = 0;
                }
                if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                  vm[row][col] = 1;
                }
              }else if(col > colChosen){
                for(int nextCol = 0; nextCol < 14 - col; nextCol++){
                  vm[row][col + nextCol] = 0;
                }
                if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                  vm[row][col] = 1;
                }
              }else if(row < rowChosen){
                for(int backRow = row; backRow >= 0; backRow--){
                  vm[backRow][col] = 0;
                }
                if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                  vm[row][col] = 1;
                }
              }else if(row > rowChosen){
                for(int nextRow = 0; nextRow < 14 - row; nextRow++){
                  vm[row + nextRow][col] = 0;
                }
                if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                  vm[row][col] = 1;
                }
              }

              //for bishop path
              if(row - rowChosen == col - colChosen || row - rowChosen == colChosen - col){
                if(row < rowChosen && col < colChosen){
                  for(int backRow = row; backRow >= 0; backRow--){
                    for(int backCol = col; backCol >= 0; backCol--){
                      vm[backRow][backCol] = 0;
                    }
                  }
                  if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                    vm[row][col] = 1;
                  }
                }else if(row > rowChosen && col > colChosen){
                  for(int nextRow = 0; nextRow < 14 - row; nextRow++){
                    for(int nextCol = 0; nextCol < 14 - col; nextCol++){
                      vm[row + nextRow][col + nextCol] = 0;
                    }
                  }
                  if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                    vm[row][col] = 1;
                  }
                }else if(row < rowChosen && col > colChosen){
                  for(int backRow = row; backRow >= 0; backRow--){
                    for(int nextCol = 0; nextCol < 14 - col; nextCol++){
                      vm[backRow][col + nextCol] = 0;
                    }
                  }
                  if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                    vm[row][col] = 1;
                  }
                }else if(row > rowChosen && col < colChosen){
                  for(int nextRow = 0; nextRow < 14 - row; nextRow++){
                    for(int backCol = col; backCol >= 0; backCol--){
                      vm[row + nextRow][backCol] = 0;
                    }
                  }
                  if(isEnemy(board[rowChosen][colChosen], board[row][col])){
                    vm[row][col] = 1;
                  }
                }
              }
            }
          }
        }
        
        //there was a bug that if there is a piece next to the 2nd square of bishop path, the path will be invalid
        if(p == Bishop || p == Archbishop || p == Amazon || p == Queen){
          if(rowChosen + 2 < 14 && colChosen + 2 < 14){
            if(board[rowChosen + 1][colChosen + 1] == ' '){
              if(board[rowChosen + 2][colChosen + 2] == ' '){
                vm[rowChosen + 2][colChosen + 2] = 1;
              }else if(isEnemy(board[rowChosen][colChosen], board[rowChosen + 2][colChosen + 2])){
                vm[rowChosen + 2][colChosen + 2] = 1;
                for(int i = 3; rowChosen + i < 14; i++){
                  for(int j = 3; colChosen + j < 14; j++){
                    vm[rowChosen + i][colChosen + j] = 0;
                  }
                }
              }else{
                for(int i = 2; rowChosen + i < 14; i++){
                  for(int j = 2; colChosen + j < 14; j++){
                    vm[rowChosen + i][colChosen + j] = 0;
                  }
                }
              }
            }
          }
          if(rowChosen - 2 >= 0 && colChosen + 2 < 14){
            if(board[rowChosen - 1][colChosen + 1] == ' '){
              if(board[rowChosen - 2][colChosen + 2] == ' '){
                vm[rowChosen - 2][colChosen + 2] = 1;
              }else if(isEnemy(board[rowChosen][colChosen], board[rowChosen - 2][colChosen + 2])){
                vm[rowChosen - 2][colChosen + 2] = 1;
                for(int i = 3; rowChosen - i >= 0; i++){
                  for(int j = 3; colChosen + j < 14; j++){
                    vm[rowChosen - i][colChosen + j] = 0;
                  }
                }
              }else{
                for(int i = 2; rowChosen - i >= 0; i++){
                  for(int j = 2; colChosen + j < 14; j++){
                    vm[rowChosen - i][colChosen + j] = 0;
                  }
                }
              }
            }
          }
          if(rowChosen + 2 < 14 && colChosen - 2 >= 0){
            if(board[rowChosen + 1][colChosen - 1] == ' '){
              if(board[rowChosen + 2][colChosen - 2] == ' '){
                vm[rowChosen + 2][colChosen - 2] = 1;
              }else if(isEnemy(board[rowChosen][colChosen], board[rowChosen + 2][colChosen - 2])){
                vm[rowChosen + 2][colChosen - 2] = 1;
                for(int i = 3; rowChosen + i < 14; i++){
                  for(int j = 3; colChosen - j >= 0; j++){
                    vm[rowChosen + i][colChosen - j] = 0;
                  }
                }
              }else{
                for(int i = 2; rowChosen + i < 14; i++){
                  for(int j = 2; colChosen - j >= 0; j++){
                    vm[rowChosen + i][colChosen - j] = 0;
                  }
                }
              }
              
            }
          }
          if(rowChosen - 2 >= 0 && colChosen - 2 >= 0){
            if(board[rowChosen - 1][colChosen - 1] == ' '){
              if(board[rowChosen - 2][colChosen - 2] == ' '){
                vm[rowChosen - 2][colChosen - 2] = 1;
              }else if(isEnemy(board[rowChosen][colChosen], board[rowChosen - 2][colChosen - 2])){
                vm[rowChosen - 2][colChosen - 2] = 1;
                for(int i = 3; rowChosen - i >= 0; i++){
                  for(int j = 3; colChosen - j >= 0; j++){
                    vm[rowChosen - i][colChosen - j] = 0;
                  }
                }
              }else{
                for(int i = 2; rowChosen - i >= 0; i++){
                  for(int j = 2; colChosen - j >= 0; j++){
                    vm[rowChosen - i][colChosen - j] = 0;
                  }
                }
              }
            }
          }
        } 
      }

      //for knight path
      if(p == Chancellor || p == Guard || p == Archbishop || p == Amazon || p == Knight){
        if(rowChosen + 2 < 14){
          if(colChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 2][colChosen + 1]) && board[rowChosen + 2][colChosen + 1] != ' '){
              vm[rowChosen + 2][colChosen + 1] = 0;
            }else{
              vm[rowChosen + 2][colChosen + 1] = 1;
            }
          }
          if(colChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 2][colChosen - 1]) && board[rowChosen + 2][colChosen - 1] != ' '){
              vm[rowChosen + 2][colChosen - 1] = 0;
            }else{
              vm[rowChosen + 2][colChosen - 1] = 1;
            }
          }
        }
        if(rowChosen - 2 >= 0){
          if(colChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 2][colChosen + 1]) && board[rowChosen - 2][colChosen + 1] != ' '){
              vm[rowChosen - 2][colChosen + 1] = 0;
            }else{
              vm[rowChosen - 2][colChosen + 1] = 1;
            }
          }
          if(colChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 2][colChosen - 1]) && board[rowChosen - 2][colChosen - 1] != ' '){
              vm[rowChosen - 2][colChosen - 1] = 0;
            }else{
              vm[rowChosen - 2][colChosen - 1] = 1;
            }
          }
        }
        if(colChosen - 2 >= 0){
          if(rowChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 1][colChosen - 2]) && board[rowChosen + 1][colChosen - 2] != ' '){
              vm[rowChosen + 1][colChosen - 2] = 0;
            }else{
              vm[rowChosen + 1][colChosen - 2] = 1;
            }
          }
          if(rowChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 1][colChosen - 2]) && board[rowChosen - 1][colChosen - 2] != ' '){
              vm[rowChosen - 1][colChosen - 2] = 0;
            }else{
              vm[rowChosen - 1][colChosen - 2] = 1;
            }
          }
        }
        if(colChosen + 2 < 14){
          if(rowChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 1][colChosen + 2]) && board[rowChosen + 1][colChosen + 2] != ' '){
              vm[rowChosen + 1][colChosen + 2] = 0;
            }else{
              vm[rowChosen + 1][colChosen + 2] = 1;
            }
          }
          if(rowChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 1][colChosen + 2]) && board[rowChosen - 1][colChosen + 2] != ' '){
              vm[rowChosen - 1][colChosen + 2] = 0;
            }else{
              vm[rowChosen - 1][colChosen + 2] = 1;
            }
          }
        }
      }

      //for camel path
      if(p == Camel){
        if(rowChosen + 3 < 14){
          if(colChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 3][colChosen + 1]) && board[rowChosen + 3][colChosen + 1] != ' '){
              vm[rowChosen + 3][colChosen + 1] = 0;
            }
          }
          if(colChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 3][colChosen - 1]) && board[rowChosen + 3][colChosen - 1] != ' '){
              vm[rowChosen + 3][colChosen - 1] = 0;
            }
          }
        }

        if(rowChosen - 3 >= 0){
          if(colChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 3][colChosen + 1]) && board[rowChosen - 3][colChosen + 1] != ' '){
              vm[rowChosen - 3][colChosen + 1] = 0;
            }
          }
          if(colChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 3][colChosen - 1]) && board[rowChosen - 3][colChosen - 1] != ' '){
              vm[rowChosen - 3][colChosen - 1] = 0;
            }
          }
        }

        if(colChosen - 3 >= 0){
          if(rowChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 1][colChosen - 3]) && board[rowChosen + 1][colChosen - 3] != ' '){
              vm[rowChosen + 1][colChosen - 3] = 0;
            }
          }
          if(rowChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 1][colChosen - 3]) && board[rowChosen - 1][colChosen - 3] != ' '){
              vm[rowChosen - 1][colChosen - 3] = 0;
            }
          }
        }

        if(colChosen + 3 < 14){
          if(rowChosen + 1 < 14){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen + 1][colChosen + 3]) && board[rowChosen + 1][colChosen + 3] != ' '){
              vm[rowChosen + 1][colChosen + 3] = 0;
            }
          }
          if(rowChosen - 1 >= 0){
            if(!isEnemy(board[rowChosen][colChosen], board[rowChosen - 1][colChosen + 3]) && board[rowChosen - 1][colChosen + 3] != ' '){
              vm[rowChosen - 1][colChosen + 3] = 0;
            }
          }
        }
      }
      return vm;
    }
    
    /**
     * Highlighting legal moves.
    */
    public void drawValidMove(){
      if(!player1Turn && autoOn){
        return;
      }
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(this.path[row][col] == 1){
            if((row + col) % 2 == 0){
              fill(214, 237, 245);
              rect(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
            }else{
              fill(136, 200, 221);
              rect(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
            }
            if(isEnemy(this.board[rowChosen][colChosen], this.board[row][col])){
              fill(255, 153, 102);
              rect(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
            }
          }  
        }
      } 
    }

    /**
     * Check if a piece is an enemy to the other.
     * @param piece1, The first piece.
     * @param piece2, The second piece.
     * @return Whether they are enemy or not.
    */
    public boolean isEnemy(char piece1, char piece2){
      if(piece2 == ' '){
        return false;
      }
      if(Character.isLowerCase(piece1)){
        if(Character.isUpperCase(piece2)){
          return true;
        }else{
          return false;
        }
      }else if(Character.isUpperCase(piece1)){
        if(Character.isLowerCase(piece2)){
          return true;
        }else{
          return false;
        }
      }
      return false;
    }

    /**
     * Identify check.
     * @param king, The king (either k or K).
     * @param board, The board.
     * @return Whether the king is under check or not.
    */
    public boolean check(char king, char[][] board){
      boolean check = false;
      boolean kingFound = false;
      if(king != 'k' && king != 'K'){
        return false;
      }
      int kingRow = -1;
      int kingCol = -1;
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(board[row][col] == king){
            kingRow = row;
            kingCol = col;
            kingFound = true;
            break;
          }
        }
        if(kingFound){
          break;
        }
      }
      
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(king == 'k'){
            if(board[row][col] != ' ' && Character.isUpperCase(board[row][col])){
              if(getValidMove(row, col, board)[kingRow][kingCol] == 1){
                check = true;
                break;
              }
            }
          }else if(king == 'K'){
            if(board[row][col] != ' ' && Character.isLowerCase(board[row][col])){
              if(getValidMove(row, col, board)[kingRow][kingCol] == 1){
                check = true;
                break;
              }
            }
          }
        }
        if(check){
          break;
        }
      }
      return check;
    }

    /**
     * Highlight a king's tile if it is being checked.
     * @param king, The king (either k or K).    
    */
    public void drawCheck(char king){
      if(king != 'k' && king != 'K'){
        return;
      }
      boolean checkmate = false;
      int kingRow = -1;
      int kingCol = -1;
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(this.board[row][col] == king){
            kingRow = row;
            kingCol = col;
            break;
          }
        }
      }
      if(checkmate(king)){
        for(int row = 0; row < 14; row++){
          for(int col = 0; col < 14; col++){
            if(getValidMove(kingRow, kingCol, board)[row][col] == 1){
              for(int i = 0; i < 14; i++){
                for(int j = 0; j < 14; j++){
                  if(isEnemy(king, board[i][j])){
                    if(getValidMove(i, j, board)[row][col] == 1){
                      fill(255, 153, 102);
                      rect(j * CELLSIZE, i * CELLSIZE, CELLSIZE, CELLSIZE);
                      break;
                    }
                  }
                }
              }
            } 
          }
        }
        checkmate = true;
      }
      if(check(king, this.board)){
        fill(208, 0, 0);
        rect(kingCol * CELLSIZE, kingRow * CELLSIZE, CELLSIZE, CELLSIZE);
        if(this.board[rowChosen][colChosen] == king){
          if(pieceSelected){
            fill(102, 153, 0);
            rect(colChosen * CELLSIZE, rowChosen * CELLSIZE, CELLSIZE, CELLSIZE);
            drawValidMove();
          }
        }  
        if(!checkmate && !flash){
          textSize(15);
          fill(255, 255, 255);
          text("Check!", 690, 340);
        }  
      }
    }

    /**
     * Modify the legal move under check condition.
     * @param rowChosen, The row selected.
     * @param colChosen, The column selected.
     * @return A 2D array that stores the legal moves for that specific piece (2 = the piece itself, 1 = legal, 0 = illegal).
    */
    public int[][] vmCheckLimit(int rowChosen, int colChosen){
      char king;
      if(player1Turn){
        king = 'k';
      }else{
        king = 'K';
      }
      int[][] vm = getValidMove(rowChosen, colChosen, this.board);
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(vm[row][col] == 1){
            char[][] tempBoard = new char[14][14];
            for (int i = 0; i < 14; i++) {
              for (int j = 0; j < 14; j++) {
                tempBoard[i][j] = this.board[i][j];
              }
            }

            if(tempBoard[row][col] == ' '){ 
              tempBoard[row][col] = tempBoard[rowChosen][colChosen];
              tempBoard[rowChosen][colChosen] = ' ';
              if(tempBoard[row][col] == king){
                if(col - colChosen == -2){
                  tempBoard[row][col + 1] = tempBoard[row][0];
                  tempBoard[row][0] = ' ';
                }
                if(col - colChosen == 2){
                  tempBoard[row][col - 1] = tempBoard[row][13];
                  tempBoard[row][13] = ' ';
                }
              }
              if(check(king, tempBoard)){
                vm[row][col] = 0;
              }
            }
            if(isEnemy(tempBoard[rowChosen][colChosen], tempBoard[row][col])){
              tempBoard[row][col] = tempBoard[rowChosen][colChosen];
              tempBoard[rowChosen][colChosen] = ' ';
              if(check(king, tempBoard)){
                vm[row][col] = 0;
              }
            } 
          }
        }
      }
      return vm;
    }

    /**
     * Identify checkmate.
     * @param king, The king (either k or K).
     * @return Whether the king specified is checkmated or not.
    */
    public boolean checkmate(char king){
      if(king != 'k' && king != 'K'){
        return false;
      }
      if(!check(king, this.board)){
        return false;
      }else{
        int sum = 0;
        for(int row = 0; row < 14; row++){
          for(int col = 0; col < 14; col++){
            if(king == 'k' && this.board[row][col] != ' ' && Character.isLowerCase(this.board[row][col])){
              for(int[] i: vmCheckLimit(row, col)){
                for(int j: i){
                  if(j != 2){
                    sum += j;
                  }
                }
              }
            }
            if(king == 'K' && this.board[row][col] != ' ' && Character.isUpperCase(this.board[row][col])){
              for(int[] i: vmCheckLimit(row, col)){
                for(int j: i){
                  if(j != 2){
                    sum += j;
                  }
                }
              }
            }
          }
        }
        if(sum == 0){
          return true;
        }
      }
      return false;
    }

    /**
     * AI scans the board and get all the movable pieces.
     * @param board, The board.
     * @return An array storing all movable pieces.
    */
    public char[] autoScan(char[][] board){
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(board[row][col] != ' ' && Character.isUpperCase(board[row][col])){
            boolean pathFound = false;
            for(int[] i: vmCheckLimit(row, col)){
              for(int j: i){
                if(j == 1){
                  movables.add(board[row][col]);
                  pathFound = true;
                  break;
                }
              }
              if(pathFound){
                break;
              }
            }
          }
        }
      }
      char[] returnValue = new char[movables.size()];
      for(int i = 0; i < movables.size(); i++){
        returnValue[i] = movables.get(i);
      }
      movables.removeAll(movables);
      return returnValue;
    }

    /**
     * Let AI make the move, choosing a random piece and move randomly.
    */
    public void autoMove(){
      if(player1Turn){
        return;
      }
      char[] pieces = autoScan(this.board);
      int randomIndex = pick.nextInt(pieces.length);
      char piecePicked = pieces[randomIndex];
      boolean found = false;
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(this.board[row][col] == piecePicked){
            this.rowChosen = row;
            this.colChosen = col;
            this.pieceSelected = true;
            found = true;
            break;
          }
        }
        if(found){
          break;
        }
      }
      boolean aimoved = false;
      for(int row = 0; row < 14; row++){
        for(int col = 0; col < 14; col++){
          if(vmCheckLimit(rowChosen, colChosen)[row][col] == 1){
            randomIndex = pick.nextInt(10);
            if(randomIndex == 1 || isEnemy(this.board[rowChosen][colChosen], this.board[row][col])){
              movePiece(rowChosen, colChosen, row, col);
              this.lastPieceRow = row;
              this.lastPieceCol = col;
              aimoved = true;
              break;
            }
          }
        }
        if(aimoved){
          break;
        }
      }
      if(aimoved){
        this.player1Time += this.player1Increment;
        if(this.player1Time > 180){
          this.player1Time = 180;
        }
        this.frameCount = 0;
        this.player1Turn = true;
      }else{
        autoMove();
      }
    }

    /**
     * Set the config for testing.
    */
    public void testing(){
      this.configPath = "test_config.json";
    }

    /**
     * The main method.
     * @param args, The input.
    */
    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }
}