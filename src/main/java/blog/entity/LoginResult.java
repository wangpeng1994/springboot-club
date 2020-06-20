package blog.entity;

public class LoginResult extends Result<User> {
    private Boolean isLogin;

    public LoginResult(String status, String msg, boolean isLogin, User user) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static LoginResult success(String msg, boolean isLogin) {
        return success(msg, isLogin, null);
    }

    public static LoginResult success(String msg, boolean isLogin, User user) {
        return new LoginResult("ok", msg, isLogin, user);
    }

    public Boolean getLogin() {
        return isLogin;
    }

}
