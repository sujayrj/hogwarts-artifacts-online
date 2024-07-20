package dev.jeppu.system;

public class StatusCode {
    public static final int SUCCESS = 200; // success
    public static final int INVALID_ARGUMENT = 400; // Bad request i.e. invalid parameters
    public static final int UNAUTHORIZED = 401; // username or password incorrect
    public static final int FORBIDDEN = 403; // no permission
    public static final int NOT_FOUND = 404; // not found
    public static final int INTERNAL_SERVER_ERROR = 500; // server internal error
    //    SUCCESS(200),
    //
    //    INVALID_ARGUMENT(400),
    //
    //    UNAUTHORIZED(401),
    //
    //    FORBIDDEN(403),
    //
    //    NOT_FOUND(404),
    //
    //    INTERNAL_SERVER_ERROR(500);
    //
    //    private final int statusCode;
    //
    //    private StatusCode(int statusCode) {
    //        this.statusCode = statusCode;
    //    }

}
