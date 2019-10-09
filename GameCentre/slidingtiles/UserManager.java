package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class UserManager implements Serializable {
    /**
     * The master list of all users that have an account in GameCentre.
     */
    private List<User> users = new ArrayList<>();

    /**
     * Add new user into  master list of users
     *
     * @param username username of user
     * @param password password of user
     * @param games    all the games the user can play
     */
    boolean addUser(String username, String password, String[] games) {
        if (username.trim().equals("") || password.trim().equals("")) {
            return false;
        } else {
            User newUser = new User(username.trim(), password.trim(), games);
            users.add(newUser);
            return true;
        }

    }

    /**
     * Checks if user with this username exists.
     *
     * @param username username of user to check
     * @return whether user exists or not
     */
    boolean userExists(String username) {
        boolean exists = false;
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(username)) {
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Checks if the user has right username and password
     *
     * @param username username of user to check
     * @param password password of user to check
     * @return whether user entered right credentials
     */
    boolean validUser(String username, String password) {

        for (User existingUser : users) {
            if (existingUser.getUsername().equals(username)) {
                if (existingUser.getPassword().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return user with this username
     *
     * @param username username of user to get
     * @return user with this username or null if they do not exist
     */
    User getUser(String username) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(username)) {
                return existingUser;
            }
        }
        return null;
    }

}
