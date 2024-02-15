import javafx.scene.paint.*;

/**
 * <h2>Ball - a type of GameObj</h2>
 * This class represents a Ball that bouces off the other bat and bricks objects
 * 
 * @author Anonymous
 * 
 * @see GameObj
 */
public class Ball extends GameObj
{
    public Ball( int x, int y, int w, int h){
        topX   = x;       
        topY = y;
        width  = w; 
        height = h; 
        colour = Color.GREY;
    }
}
