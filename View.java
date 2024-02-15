// We import lots of JavaFX libraries (we may not use them all, but it
// saves us having to thinkabout them if we add new code)
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.event.ActionEvent;

/**
 * <h2>View class</h2>
 * The View class creates and manages the GUI for the application.
 * It doesn't know anything about the game itself, it just displays
 * the current state of the Model, and handles user input
 * 
 * @author Anonymous
 * 
 * @see Model
 */
public class View implements EventHandler<KeyEvent>
{ 
    // variables for components of the user interface
    private int width;       // width of window
    private int height;      // height of window

    // user interface objects
    private Pane pane;       // basic layout pane
    private Canvas canvas;   // canvas to draw game on
    private Label infoText;  // info at top of screen

    // The other parts of the model-view-controller setup
    protected Controller controller;
    protected Model model;

    private GameObj   bat;            // The bat
    private GameObj   ball;           // The ball
    private ArrayList<GameObj> bricksList = new ArrayList<GameObj>();
    private int       score =  0;     // The score
    private int level;                // The level

    private Stage window;

    // constructor method - we get told the width and height of the window
    public View(int w, int h)
    {
        Debug.trace("View::<constructor>");
        width = w;
        height = h;
    }

    // start is called from the Main class, to start the GUI up
    public void start(Stage theWindow) 
    {
        window = theWindow;
        // breakout is basically one big drawing canvas, and all the objects are
        // drawn on it as rectangles, except for the text at the top - this
        // is a label which sits 'in front of' the canvas.

        // Note that it is important to create control objects (Pane, Label,Canvas etc) 
        // here not in the constructor (or as initialisations to instance variables),
        // to make sure everything is initialised in the right order
        pane = new Pane();       // a simple layout pane
        pane.setId("Breakout");  // Id to use in CSS file to style the pane if needed

        // canvas object - we set the width and height here (from the constructor), 
        // and the pane and window set themselves up to be big enough
        canvas = new Canvas(width,height);  
        pane.getChildren().add(canvas);     // add the canvas to the pane

        // infoText box for the score - a label which we position in front of
        // the canvas (by adding it to the pane after the canvas)
        infoText = new Label("BreakOut: Score = " + score);
        infoText.setTranslateX(25);  // these commands setthe position of the text box
        infoText.setTranslateY(10);  // (measuring from the top left corner)
        pane.getChildren().add(infoText);  // add label to the pane

        // Make a new JavaFX Scene, containing the complete GUI
        Scene scene = new Scene(pane);   
        scene.getStylesheets().add("breakout.css"); // tell the app to use our css file

        // Add an event handler for key presses. By using 'this' (which means 'this 
        // view object itself') we tell JavaFX to call the 'handle' method (below)
        // whenever a key is pressed
        scene.setOnKeyPressed(this);

        // put the scene in the window and display it
        window.setScene(scene);
        window.show();
    }

    // Event handler for key presses - it just passes the event to the controller
    public void handle(KeyEvent event)
    {
        // send the event to the controller
        controller.userKeyInteraction( event );
    }

    // drawing the game image
    public void drawPicture()
    {
        // the game loop is running 'in the background' so we have
        // add the following line to make sure it doesn't change
        // the model in the middle of us updating the image
        synchronized ( model ) 
        {
            // get the 'paint brush' to pdraw on the canvas
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // clear the whole canvas to white
            gc.setFill( Color.WHITE );
            gc.fillRect( 0, 0, width, height );

            // draw the bat and ball
            displayGameObj( gc, ball );   // Display the Ball
            displayGameObj( gc, bat  );   // Display the Bat

            // *[2]****************************************************[2]*
            // * Display the bricks that make up the game                 *
            // * Fill in code to display bricks from the brick array      *
            // * Remember only a visible brick is to be displayed         *
            // ************************************************************
            for (GameObj brick: bricksList) {
                if (brick.visible) {
                    displayGameObj(gc, brick);
                }
            }  

            // update the score
            infoText.setText("BreakOut: Level = " + level + " Score = " + score);
        }
    }

    // Display a game object - it is just a rectangle on the canvas
    public void displayGameObj( GraphicsContext gc, GameObj go )
    {
        gc.setFill( go.colour );
        gc.fillRect( go.topX, go.topY, go.width, go.height );
    }

    // This is how the Model talks to the View
    // This method gets called BY THE MODEL, whenever the model changes
    // It has to do whatever is required to update the GUI to show the new game position
    public void update()
    {
        if (model.getGameState() == "running") {
            // Get from the model the ball, bat, bricks & score
            ball    = model.getBall();              // Ball
            bricksList  = model.getBricksList();            // Bricks
            bat     = model.getBat();               // Bat
            score   = model.getScore();             // Score
            level   = model.getLevel();
            //Debug.trace("Update");
            drawPicture();                     // Re draw game
        } else {
            drawEndScreen();
        }
    }    

    public void drawEndScreen() {
        // get the 'paint brush' to pdraw on the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // clear the whole canvas to white
        gc.setFill( Color.GREY );
        gc.fillRect( 0, 0, width, height );

        infoText.setText("");

        Label gameOverText = new Label("Game Over\nYou scored: " + score);
        gameOverText.setId("gameOver");
        gameOverText.setTranslateX(150);  // these commands setthe position of the text box
        gameOverText.setTranslateY(250);  // (measuring from the top left corner)
        pane.getChildren().add(gameOverText);  // add label to the pane

        Button buttonScoreboard = new Button("Scoreboard");
        buttonScoreboard.setOnAction(this::buttonScoreboardClicked);
        buttonScoreboard.setTranslateX(150);  // these commands setthe position of the text box
        buttonScoreboard.setTranslateY(400);  // (measuring from the top left corner)
        pane.getChildren().add(buttonScoreboard);  // add label to the pane

        Button buttonRestart = new Button("Restart Game");
        buttonRestart.setOnAction(this::buttonRestartClicked);
        buttonRestart.setTranslateX(250);  // these commands setthe position of the text box
        buttonRestart.setTranslateY(400);  // (measuring from the top left corner)
        pane.getChildren().add(buttonRestart);  // add label to the pane
    }

    public void buttonScoreboardClicked(ActionEvent event) {
        ListView listView1 = new ListView();
        listView1.setTranslateX(150);  // these commands setthe position of the text box
        listView1.setTranslateY(450);  // (measuring from the top left corner)
        pane.getChildren().add(listView1);  // add label to the pane

        SavedScoreboard scoreboard = new SavedScoreboard("scoreboard.txt");
        try {
            scoreboard.load();
        } catch (Exception e) {
            Debug.error("SavingScore error" + e.getMessage());
            listView1.getItems().add("Scoreboard not availible at this time");
        }

        listView1.getItems().add(scoreboard.listing());
    }

    public void buttonRestartClicked(ActionEvent event) {
        start(window); 				   
        model.startGame();
    }
}