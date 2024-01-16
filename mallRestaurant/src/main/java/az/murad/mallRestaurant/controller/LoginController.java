package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.exception.LoginFailedException;
import az.murad.mallRestaurant.Entity.LoginRequest;
import az.murad.mallRestaurant.Entity.LoginResponse;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    public LoginController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        logger.info("Received login request: {}", loginRequest);

        try {
            User user = userRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

            if (user != null) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                LoginResponse response = new LoginResponse("Login successful", user.getId(), token);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("User not found for login request: {}", loginRequest);

                // User not found
                throw new LoginFailedException("Invalid credentials");
            }

        } catch (Exception e) {
            logger.error("Exception during login: {}", e.getMessage(), e);
            throw new LoginFailedException("Exception: " + e.getMessage());
        }
    }

}
