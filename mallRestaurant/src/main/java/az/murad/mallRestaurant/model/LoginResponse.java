package az.murad.mallRestaurant.model;

public class LoginResponse {
    private final String message;
    private final String userId; // Use String for user ID to handle null values

    public LoginResponse(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }
}
