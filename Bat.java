import javafx.scene.paint.*;

/**
 * <h2>Bat - a type of GameObj</h2>
 * This class represents a Bat that the player controls
 * 
 * @author Anonymous
 * 
 * @see GameObj
 */
public class Bat extends GameObj 
{
        public Bat( int x, int y, int w, int h){
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = Color.BLUE;
    }
}