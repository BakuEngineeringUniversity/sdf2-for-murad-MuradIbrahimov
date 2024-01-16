package az.murad.mallRestaurant.entity;

public class LoginResponse {
    private final String message;
    private final String userId;
    private final String token;  // Add JWT token to the response

    public LoginResponse(String message, String userId, String token) {
        this.message = message;
        this.userId = userId;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
