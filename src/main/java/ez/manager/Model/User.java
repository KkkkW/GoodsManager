package ez.manager.Model;

import lombok.Data;

@Data
public class User{
    private int id;
    private String userName;
    private String password;
    private String realName;
    private String  business;
    private String email;
}
