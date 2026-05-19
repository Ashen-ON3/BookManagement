package bookmanagement;

/**
 * Represents a User entity mapped to tblusers.
 * Extends Entity (Inheritance) and encapsulates user data.
 */
public class User extends Entity {

    private String username;
    private String password;

    public User() {
        super();
    }

    public User(int userid, String username, String password) {
        super(userid);
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters (Encapsulation)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isValid() {
        return username != null && !username.trim().isEmpty()
            && password != null && !password.trim().isEmpty()
            && username.length() <= 10
            && password.length() <= 10;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}
