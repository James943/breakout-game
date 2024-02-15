// Watch out for the different spellings of Color/colour - the class uses American
// spelling, but we have chosen to use British spelling for the instance variable!

// import Athe JavaFX Color class
import javafx.scene.paint.Color;

/**
 * <h2>Game Object</h2>
 * An object in the game, represented as a rectangle, with a position,
 * a size, a colour and a direction of movement.
 * 
 * @author Anonymous
 * 
 */
public class GameObj
{
    // state variables for a game object
    protected boolean visible  = true;     // Can be seen on the screen (change to false when the brick gets hit)
    protected int topX   = 0;              // Position - top left corner X
    protected int topY   = 0;              // position - top left corner Y
    protected int width  = 0;              // Width of object
    protected int height = 0;              // Height of object
    protected Color colour;                // Colour of object
    private int   dirX   = 1;            // Direction X (1, 0 or -1)
    private int   dirY   = 1;            // Direction Y (1, 0 or -1)

    public GameObj(){
        // nothing to do here - just use the built-in values
    }

    // move in x axis
    public void moveX( int units )
    {
        topX += units * dirX;
    }

    // move in y axis
    public void moveY( int units )
    {
        topY += units * dirY;
    }

    // change direction of movement in x axis (-1, 0 or +1)
    public void changeDirectionX()
    {
        dirX = -dirX;
    }

    // change direction of movement in y axis (-1, 0 or +1)
    public void changeDirectionY()
    {
        dirY = -dirY;
    }

    // Detect collision between this object and the argument object
    // It's easiest to work out if they do NOT overlap, and then
    // return the opposite
    public boolean hitBy( GameObj obj )
    {
        boolean separate =  
            topX >= obj.topX+obj.width     ||    // '||' means 'or'
            topX+width <= obj.topX         ||
            topY >= obj.topY+obj.height    ||
            topY+height <= obj.topY ;

        // use ! to return the opposite result - hitBy is 'not separate')
        return(! separate);  

    }
}