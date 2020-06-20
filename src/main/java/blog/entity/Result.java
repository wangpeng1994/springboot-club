package blog.entity;

/**
 * Result 基类
 * @param <T>
 */
public class Result<T> {
    private String status;
    private String msg;
    private T data;

    public static Result<Object> failure(String message) {
        return new Result<>("fail", message);
    }

    protected Result(String status, String msg) {
        this(status, msg, null);
    }

    protected Result(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
