package az.murad.mallRestaurant.Entity;

public class LoginResponse {
    private final String message;
    private final String userId;
    private final String token;
    private final String role;
    private final String email;

    public LoginResponse(String message, String userId, String token, String role, String email) {
        this.message = message;
        this.userId = userId;
        this.token = token;
        this.role = role;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }
}
