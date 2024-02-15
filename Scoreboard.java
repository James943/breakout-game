import java.util.HashMap;

/**
 * A class to store a list of player's names and their score.
 *
 * @author Anonymous
 * @version 1.0
 */
public class Scoreboard
{
    /**
     * a scoreboard object. 
     * 
     * I am using a HashMap to store scores associated with the names of players of the game. 
     */
    protected HashMap<String, Integer> scores; 

    /**
     * constructor for objects of class Scoreboard
     */
    public Scoreboard()
    {
        // initialise the scores
        scores = new HashMap<String, Integer>();
    }

    /**
     * clears all the entries from the Scoreboard 
     */
    public void clear()
    {
        // clear the scores map
        scores.clear();
    }
    
    /**
     * adds a name and score to the Scoreboard
     *
     * @param  name  the name of the player
     * @param  score the player's score
     */
    public void addScore(String name, int score)
    {
        // add the name and score pair to the HashMap
        //************* note that there is no duplication check here - it simply overwrites
        scores.put(name, score);
    }
    
    /********************************************
     * retrieves a score from the Scoreboard
     *
     * @param  name  the name of the player
     * @return a score of a player or 0 if the player is not in the scoreboard
     */
    public int getScore(String name)
    {
        // check if the name is a key in the HashMap
        if (scores.containsKey(name)) {
            // return their score if so
            return scores.get(name);
        } else {
            // return 0 if not
            return 0;
        }
    } 
    
    /**
     * creates a String listing all the entries in the Scorboard
     * 
     * @return the listing, with each entry on a separate line
     */
    public String listing()
    {
        String list = ""; 
        // use keySet to return the Set of map keys. 
        for (String name:scores.keySet()) 
        {
            // add an entry to the end of the list, plus a newline
            list += name + ":" + scores.get(name) + "\n";
        }
        return list;
    }
    
    /********************************************
     * prints a listing of the Scoreboard on the Console output
     */
    public void printListing()
    {
        System.out.print(listing());
    }
}