package fall2018.csc2017.slidingtiles;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserAndManagerTest {
    /**
     * User manager for testing.
     */
    private UserManager testUsers;
    /**
     * List of games.
     */
    private String[] games = {"tiles", "minesweeper", "pattern"};

    private void setUpUserManager() {
        testUsers = new UserManager();
    }

    private void setUpUsers() {
        setUpUserManager();
        testUsers.addUser("bob", "524352", games);
        testUsers.addUser("mark23", "password", games);
        testUsers.addUser("keith", "23443", games);
    }

    /**
     * Test if addUser adds a valid or invalid user.
     */
    @Test
    public void testAddUser() {
        setUpUserManager();
        assertFalse(testUsers.addUser("bob", "", games));
        assertFalse(testUsers.addUser("", "123FG", games));
        assertFalse(testUsers.addUser("", "", games));
        assertTrue(testUsers.addUser("bob123", "BO45d", games));
    }

    /**
     * Test if userExists correctly returns whether a user exists or not.
     */
    @Test
    public void testUserExists() {
        setUpUsers();
        assertTrue(testUsers.userExists("bob"));
        assertFalse(testUsers.userExists("keith1"));
    }

    /**
     * Test if validUser correctly returns whether a user entered the right
     * username and password.
     */
    @Test
    public void testValidUser() {
        setUpUsers();
        assertTrue(testUsers.validUser("mark23", "password"));
        assertTrue(testUsers.validUser("bob", "524352"));
        assertFalse(testUsers.validUser("bob", "5243522"));
    }

    /**
     * Test if getUser returns the right user given the username.
     */
    @Test
    public void testGetUsers() {
        setUpUsers();
        assertNull(testUsers.getUser("mark"));
        Object user1 = testUsers.getUser("mark23");
        assertNotNull(user1);
        assertTrue(user1 instanceof User);
    }

    /**
     * Test if user methods returns the right game files.
     */
    @Test
    public void testGetFiles() {
        setUpUsers();
        User user1 = testUsers.getUser("bob");
        assertEquals(user1.getGameFile("tiles"), "bobtiles.ser");
        assertEquals(user1.getTempFile("pattern"), "bobpatternTemp.ser");
        assertEquals(user1.getScoresFile(), "bobscores.ser");
    }

}
