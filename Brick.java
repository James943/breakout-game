import javafx.scene.paint.*;

/**
 * <h2>Brick - a type of GameObj</h2>
 * This class represents a Brick that the player has to destroy
 * 
 * @author Anonymous
 * 
 * @see GameObj
 */
public class Brick extends GameObj
{
    public Brick( int x, int y, int w, int h)
    {
        topX   = x;       
        topY = y;
        width  = w; 
        height = h;
        colour = Color.RED;
    }
}
