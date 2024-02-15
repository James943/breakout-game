import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

/**
 * The test class for [@link SavedScoreboard}.
 *
 * @author  Anonymous
 * @version 1.0
 */
public class SavedScoreboardTest
{
    /**
     * Default constructor for test class SavedScoreboardTest
     */
    public SavedScoreboardTest()
    {
    }

    // Instance variable to hold a SavedScoreboard object to test
    SavedScoreboard b1 = null;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     * 
     * <p>It creates a SavedScoarboard object {@code b1} for {@code unitTest.txt}
     * and saves empty data to clear the file.
     * 
     * @throws IOException if it encounters an error creating or saving the file
     */
    @Before
    public void setUp() throws IOException
    {
        b1 = new SavedScoreboard("unitTest.txt");
        b1.save();
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * tests that loading an empty file, created by Before, results in an empty listing
     * 
     * @throws IOException if it encounters an error accessing the saved data
     */
    @Test
    public void TestEmpty() throws IOException
    {
        b1.load();
        assertEquals("Test1", b1.listing(), "");
    }    

    /**
     * tests that the persistent storage works
     * 
     * @throws IOException if it encounters an error accessing or writing the saved data
     */
    @Test
    public void TestSave() throws IOException
    {
        // Add two scores to the @Before created Scoreboard and save it
        b1.addScore("Kim", 10500);
        b1.addScore("Freddy", 500);
        b1.save();

        // Create a second instance, using the same file, then load the data and check that the
        // listing is as expected
        SavedScoreboard b2 = new SavedScoreboard("unitTest.txt");
        b2.load();
        assertEquals("Test1", b1.listing(),"Freddy:500\nKim:10500\n");

        // add a new score to b1 and save it
        b2.addScore("Kevin", 24000);
        b2.save();

        // reload the original and check that Kevin is now included in the listing
        b1.load();
        assertEquals("Test2", b1.listing(),"Kevin:24000\nFreddy:500\nKim:10500\n");
    }    
}