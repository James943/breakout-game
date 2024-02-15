import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class for [@link Scoreboard].
 *
 * @author  Anonymous
 * @version 1.0
 */
public class ScoreboardTest
{
    /**
     * Default constructor for test class ScoreboardTest
     */
    public ScoreboardTest()
    {
    }

    // Instance variable to hold a Scoreboardboard object to test
    Scoreboard board1 = null;
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        board1 = new Scoreboard();
        board1.addScore("Harold", 6500);
        board1.addScore("Jimmy", 8600);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        board1 = null;
    }

    /**
     * tests getScore.
     * 
     * <p>Checks that getScore retrieves scores and returns 0 for unknown players
     */
    @Test
    public void TestGetScore()
    {
        assertEquals("Test1", board1.getScore("Harold"), 6500);
        assertEquals("Test2", board1.getScore("Jimmy"), 8600);
        assertEquals("Test3", board1.getScore("Penny"), 0);
    }    

    /**
     * tests that the listing method returns the expected string
     */
    @Test
    public void TestListing()
    { 
        assertEquals("Test1", board1.listing(),"Harold:6500\nJimmy:8600\n");
    }    
}