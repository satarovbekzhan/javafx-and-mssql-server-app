package sample.model;

public class User {
    private final Integer id;
    private final String email;
    private final String pass;
    private final Role role;

    public User(Integer id, String email, String pass, Role role) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", role=" + role +
                '}';
    }
}
