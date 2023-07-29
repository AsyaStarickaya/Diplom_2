package pojo;

public class CreateUserJson {
    private String email;
    private String password;
    private String name;

    public CreateUserJson(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
