package blog.entity;

public class Result {
    private String status;
    private String msg;
    private Boolean isLogin;
    private Object data;

    public static Result failure(String message) {
        return new Result("fail", message, false);
    }

    public Result(String status, String msg, Boolean isLogin) {
        this(status, msg, isLogin, null);
    }

    public Result(String status, String msg, Boolean isLogin, User data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Boolean isIsLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }
}
