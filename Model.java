// provides methods to allow the Controller
// to move the bat (and a couple of other functions - change the speed or stop 
// the game), and runs a background process (a 'thread') that moves the ball 
// every 20 milliseconds and checks for collisions 

import javafx.scene.paint.*;
import javafx.application.Platform;
import java.util.ArrayList;

/**
 * <h2>Model class</h2>
 * The model represents all the actual content and functionality of the game
 * For Breakout, it manages all the game objects that the View needs
 * (the bat, ball, bricks, and the score), 
 * 
 * @author Anonymous
 * 
 * @see View
 */
public class Model 
{
    // First,a collection of useful values for calculating sizes and layouts etc.

    private final int B              = 6;      // Border round the edge of the panel
    private final int M              = 40;     // Height of menu bar space at the top

    private final int BALL_SIZE      = 10;     // Ball side
    private final int BRICK_WIDTH    = 50;     // Brick size
    private final int BRICK_HEIGHT   = 30;

    private final int BAT_MOVE       = 5;      // Distance to move bat on each keypress
    private final int BALL_MOVE      = 3;      // Units to move the ball on each step

    private final int HIT_BRICK      = 50;     // Score for hitting a brick
    private final int HIT_BOTTOM     = -200;   // Score (penalty) for hitting the bottom of the screen

    // The other parts of the model-view-controller setup
    View view;
    Controller controller;

    // The game 'model' - these represent the state of the game
    // and are used by the View to display it
    private GameObj ball;                // The ball
    private ArrayList<GameObj> bricksList = new ArrayList<GameObj>();
    private GameObj bat;                 // The bat
    private int score = 0;               // The score

    // variables that control the game 
    private String gameState = "running";// Set to "finished" to end the game
    private boolean fast = false;        // Set true to make the ball go faster

    // initialisation parameters for the model
    private int width;                   // Width of game
    private int height;                  // Height of game

    private int level;

    // CONSTRUCTOR - needs to know how big the window will be
    public Model( int w, int h )
    {
        Debug.trace("Model::<constructor>");  
        width = w; 
        height = h;

    }

    // Animating the game
    // The game is animated by using a 'thread'. Threads allow the program to do 
    // two (or more) things at the same time. In this case the main program is
    // doing the usual thing (View waits for input, sends it to Controller,
    // Controller sends to Model, Model updates), but a second thread runs in 
    // a loop, updating the position of the ball, checking if it hits anything
    // (and changing direction if it does) and then telling the View the Model 
    // changed.

    // When we use more than one thread, we have to take care that they don't
    // interfere with each other (for example, one thread changing the value of 
    // a variable at the same time the other is reading it). We do this by 
    // SYNCHRONIZING methods. For any object, only one synchronized method can
    // be running at a time - if another thread tries to run the same or another
    // synchronized method on the same object, it will stop and wait for the
    // first one to finish.

    public Thread t;

    // Start the animation thread
    public void startGame()
    {
        initialiseGame();                           // set the initial game state
        //Thread t = new Thread( this::runGame );     // create a thread running the runGame method
        t = new Thread( this::runGame );
        t.setDaemon(true);                          // Tell system this thread can die when it finishes
        t.start();                                  // Start the thread running
    }   

    // Initialise the game - reset the score and create the game objects 
    private void initialiseGame()
    {       
        score = 0;
        ball   = new Ball(width/2, height/2, BALL_SIZE, BALL_SIZE);
        bat    = new Bat(width/2, height - BRICK_HEIGHT*3/2, BRICK_WIDTH*3, BRICK_HEIGHT/4);
        // *[1]******************************************************[1]*
        // * Fill in code to make the bricks array                      *
        // **************************************************************
        level = 1;
        makeBricks();
    }

    /**
     * Makes a pattern of bricks depending on what level the player is on
     */
    private void makeBricks() {
        int WALL_TOP = 100;                     // how far down the screen the wall starts
        int NUM_BRICKS = width/BRICK_WIDTH;     // how many bricks fit on screen
        switch (level)             
        {

            case 1:
            /*
             * Level 1
             * 
             * Draws 3 rows of bricks
             */
            for (int i=0; i < 3; i++) {
                for (int j=0; j < NUM_BRICKS; j++) {
                    GameObj brick = new Brick(BRICK_WIDTH*j, WALL_TOP + BRICK_HEIGHT*i, BRICK_WIDTH, BRICK_HEIGHT);
                    bricksList.add(brick);
                }
            }
            break;
            case 2:
            /*
             * Level 2
             * 
             * Draws 10 rows of bricks in a checkerboard pattern
             */
            for (int i=0; i < 10; i++) {
                for (int j=0; j < NUM_BRICKS; j++) {
                    if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                        GameObj brick = new Brick(BRICK_WIDTH*j, WALL_TOP + BRICK_HEIGHT*i, BRICK_WIDTH, BRICK_HEIGHT);
                        bricksList.add(brick);
                    }
                }
            }
            break;
            case 3:
            /*
             * Level 3
             * 
             * Draws bricks in a circle pattern
             */
            int radius = 5;
            for (int i  = -radius; i <= radius; i++) {
                for (int j = -radius ; j <= radius; j++) {
                    if (i*i + j*j <=radius*radius) {
                        GameObj brick = new Brick(BRICK_WIDTH*j + 275, WALL_TOP + BRICK_HEIGHT*i + 125, BRICK_WIDTH, BRICK_HEIGHT);
                        bricksList.add(brick);
                    }
                }
            }
            break;
        }
    }

    // The main animation loop
    private void runGame()
    {
        try
        {
            Debug.trace("Model::runGame: Game starting"); 
            // set game true - game will stop if it is set to "finished"
            setGameState("running");
            while (!getGameState().equals("finished"))
            {
                updateGame();                        // update the game state
                modelChanged();                      // Model changed - refresh screen
                //Thread.sleep( getFast() ? 10 : 20 ); // wait a few milliseconds
                Thread.sleep( getFast() ? 1 : 20 ); // wait a few milliseconds
            }
            Debug.trace("Model::runGame: Game finished"); 
        } catch (Exception e) 
        { 
            Debug.error("Model::runAsSeparateThread error: " + e.getMessage() );
        }
    }

    /**
     * Saves the score of the player to the scoreboard 
     * 
     * First a new instance of Saved Scoreboard is created, then the contents of the file is
     * loaded into the scoreboard HashMap. A new entry is created, by getting the player's
     * name they entered at the start, and their score from the finished game. The new
     * scores HashMap is then saved to the file.
     */
    private void saveScore() {
        SavedScoreboard scoreboard = new SavedScoreboard("scoreboard.txt");
        try {
            scoreboard.load();
            scoreboard.addScore(Main.name,score);
            scoreboard.save();
        } catch (Exception e) {
            Debug.error("SavingScore error" + e.getMessage());
        }
    }

    // updating the game - this happens about 50 times a second to give the impression of movement
    private synchronized void updateGame()
    {
        // move the ball one step (the ball knows which direction it is moving in)
        ball.moveX(BALL_MOVE);                      
        ball.moveY(BALL_MOVE);
        // get the current ball possition (top left corner)
        int x = ball.topX;  
        int y = ball.topY;
        // Deal with possible edge of board hit
        if (x >= width - B - BALL_SIZE)  ball.changeDirectionX();
        if (x <= 0 + B)  ball.changeDirectionX();
        if (y >= height - B - BALL_SIZE)  // Bottom
        { 
            ball.changeDirectionY(); 
            addToScore( HIT_BOTTOM );     // score penalty for hitting the bottom of the screen
        }
        if (y <= 0 + M)  ball.changeDirectionY();

        // check whether ball has hit a (visible) brick
        boolean hit = false;

        // *[3]******************************************************[3]*
        // * Fill in code to check if a visible brick has been hit      *
        // * The ball has no effect on an invisible brick               *
        // * If a brick has been hit, change its 'visible' setting to   *
        // * false so that it will 'disappear'                          * 
        // **************************************************************
        for (GameObj brick: bricksList) {
            if (brick.visible && brick.hitBy(ball)) {
                hit = true;
                addToScore( HIT_BRICK );    // add to score for hitting a brick
                bricksList.remove(brick);
                break;                      // must end the loop immediately when modifying ArrayLists in Loops
            }
        }

        if (hit) {
            ball.changeDirectionY();
        }

        // check whether ball has hit the bat
        if (ball.hitBy(bat)) {
            ball.changeDirectionY();
        }

        /*
         * Finishes the game if all the bricks are gone
         * 
         * Checks the size of the bricksList ArrayList.
         * If it is empty then it sets the gameState to finished
         */
        if (bricksList.size() == 0) {
            if (level != 3) {
                level++;
                makeBricks();
            }else {
                setGameState("finished");
                saveScore();
            }
        }
    }

    // This is how the Model talks to the View
    // Whenever the Model changes, this method calls the update method in
    // the View. It needs to run in the JavaFX event thread, and Platform.runLater 
    // is a utility that makes sure this happens even if called from the
    // runGame thread
    private synchronized void modelChanged()
    {
        Platform.runLater(view::update);
    }

    // Methods for accessing and updating values
    // these are all synchronized so that the can be called by the main thread 
    // or the animation thread safely

    // Change game state - set to "running" or "finished"
    public synchronized void setGameState(String value)
    {  
        gameState = value;
    }

    // Return game running state
    public synchronized String getGameState()
    {  
        return gameState;
    }

    // Change game speed - false is normal speed, true is fast
    public synchronized void setFast(Boolean value)
    {  
        fast = value;
    }

    // Return game speed - false is normal speed, true is fast
    public synchronized Boolean getFast()
    {  
        return(fast);
    }

    // Return bat object
    public synchronized GameObj getBat()
    {
        return(bat);
    }

    // return ball object
    public synchronized GameObj getBall()
    {
        return(ball);
    }

    // return bricks
    public synchronized ArrayList<GameObj> getBricksList()
    {
        return(bricksList);
    }

    // return level
    public synchronized int getLevel()
    {
        return(level);
    }

    // return score
    public synchronized int getScore()
    {
        return(score);
    }

    // update the score
    private synchronized void addToScore(int n)    
    {
        score += n;        
    }

    // move the bat one step - -1 is left, +1 is right
    public synchronized void moveBat( int direction )
    {        
        int dist = direction * BAT_MOVE;    // Actual distance to move
        Debug.trace( "Model::moveBat: Move bat = " + dist );
        bat.moveX(dist);
    }
}   