// breakout game Main class - use this class to start the game

// We need to access some JavaFX classes so we list ('import') them here
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * <h2>Main class</h2>
 * This class is what the uses first called what they lauch the game.
 * It calls the model and view classes in order the start up the game.
 * 
 * @author Anonymous
 * 
 * @see Model
 * @see View
 */
public class Main extends Application
{
    // The 'main' method - this is only used when launching from the command line.
    public static void main( String args[] )
    {
        // 'launch' initialises the system and then calls 'start'
        // (When running in BlueJ, the menu option 'Run JavaFX Application'
        // calls 'start' itself)
        launch(args);
    }

    // the layout object
    private GridPane grid;

    // GUI objects
    TextField textField1;
    Button button1;
    Label label1;
    Button button2;
    Label label2;
    ListView listView1;

    /*
     * The name of the player which they enter before the game starts
     * 
     * A static variable is used becuase it needs to be associated with the class,
     * not a particular instance object, when it is called from another class.
     */
    public static String name;

    private Stage window;
      
    /**
     * the 'start' method - this creates a grid showing the controls to tart the game
     * 
     * @return none
     */
    public void start(Stage theWindow) 
    {
        window = theWindow;
        grid = new GridPane();

        // TextField
        grid.add(new Label("Name: "), 0, 0);
        textField1 = new TextField();
        grid.add(textField1,1,0);

        // Start Button
        button1 = new Button("Start Game");
        button1.setOnAction(this::button1Clicked);
        grid.add(button1,1,1);        

        // Label
        label1 = new Label("");
        grid.add(label1,1,2);

        // Scoreboard Button
        button2 = new Button("Scoreboard");
        button2.setOnAction(this::button2Clicked);
        grid.add(button2,1,3);        

        // Label
        label2 = new Label("");
        grid.add(label2,2,3);
        


        // add the content and show the window
        window.setScene(new Scene(grid, 300, 250));
        window.show();
    }

    /**
     * If the user enters thir name this creates the Model, View and Controller objects and
     * makes them talk to each other, it then sets up the user interface (in the View 
     * object) and starts the game running (in the Model object)
     * 
     * @return none
     */
    public void button1Clicked(ActionEvent event) {
        name = textField1.getText();
        if (name.equals("")) {
            label1.setText("Please enter your name");
        } else {
            int H = 800;         // Height of game window (in pixels)
            int W = 600;         // Width  of game window (in pixels)

            // set up debugging and print initial debugging message
            Debug.set(true);    // change this to 'false' to stop breakout printing messages         
            Debug.trace("Main::start: Breakout starting");  

            // Create the Model, View and Controller objects
            Model model = new Model(W,H);
            View  view  = new View(W,H);
            Controller controller  = new Controller();

            // Link them together so they can talk to each other
            // Each one has instance variables for the other two
            model.view = view;
            model.controller = controller;

            controller.model = model;
            controller.view = view;

            view.model = model;
            view.controller = controller;

            // start up the game interface (the View object, passing it the window
            // object that JavaFX passed to this method, and then tell the model to 
            // start the game
            view.start(window); 				   
            model.startGame();

            // application is now running - print a debug message to say so
            Debug.trace("Main::start: Breakout running");
        }
    }

    
    /**
     * If the user wants to view the scores of other players, then it will display it
     * in a scrollable list view control
     * 
     * @return none
     */
    public void button2Clicked(ActionEvent event) {
        SavedScoreboard scoreboard = new SavedScoreboard("scoreboard.txt");
        try {
            scoreboard.load();
        } catch (Exception e) {
            Debug.error("SavingScore error" + e.getMessage());
        }
        listView1 = new ListView();
        grid.add(listView1,1,4);
        listView1.getItems().add(scoreboard.listing());
    }
}
