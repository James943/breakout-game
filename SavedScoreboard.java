import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * an extension to {@link Scoreboard} which stores the scoreboard in a file.
 * <p>SavedScorecboard extends {@link Scoreboard} class by providing {@code save} and 
 * {@code load} methods for storing and retrieving the Scoreboard data in a file.</p>
 *
 * @author Anonymous
 * @version 1.0
 */
public class SavedScoreboard extends Scoreboard
{
    // The name of the file to use for storing
    private String filename;
    
    /**
     * constructor for objects of class SavedScoreboard
     * 
     * @param theFileName the name of the file to store the Scoreboard data in
     */
    public SavedScoreboard(String theFileName)
    {
        super();
        filename = theFileName;
    }

    /**
     * gets the filename
     * 
     * @return the filename (a String)
     */
    public String getFileName()
    {
        return filename;
    }
    
    // no setFileName method - no-one can change a filename except inside this class
    
    /**
     * loads data from the file
     * 
     * @throws IOException if it encounters an error opening the file for reading
     */
    public void load() throws IOException
    {
        // Create a Scanner object to read the file
        // Set the delimiter to be : or newline
        FileReader f = new FileReader(filename);
        Scanner input = new Scanner(f);
        input.useDelimiter(":|\n");
        
        // clear the HashMap of any existing data
        clear();
        
        // loop through the data in the file
        while (input.hasNext())
        {
            // read the name
            String name = input.next();
            // check that there is also a score - ignore this name if not
            //if (input.hasNext())     //**************************
            if (input.hasNextInt())
            {
                // add the name and score to the map 
                //addScore(name, input.next());     //**************************
                addScore(name, input.nextInt());
            }
        }
        // close the file bfore finishing
        f.close();
    }    
    
    /**
     * saves data into the file
     * 
     * @throws IOException if it encounters an error opening the file for writing
     */
    //public void save() throws IOException
    public void save() throws IOException

    {
        // create a filewriter, write the listing string to it, and then close the file
        FileWriter f = new FileWriter(filename);
        f.write(listing());
        f.close();
    }
}