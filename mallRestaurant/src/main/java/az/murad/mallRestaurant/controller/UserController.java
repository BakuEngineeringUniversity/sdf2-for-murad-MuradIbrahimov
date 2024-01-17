package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestHeader("Authorization") String token) {
        // Extract the token from the Authorization header

        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        System.out.println(username);
        if (user != null && user.getRole().equals("ROLE_ADMIN")) {
            // Access granted
            logger.info("Fetching all users");
            List<User> users = userService.getAllUsers();
            logger.info("Fetched {} users", users.size());
            return users;
        } else {
            // Access denied
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // Example: Only users with 'ROLE_USER' can access this endpoint
    public ResponseEntity<User> getUserById(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (user != null && (user.getRole().equals("ROLE_ADMIN") || user.getId().equals(id))) {
            // Access granted
            logger.info("Fetching user with id {}", id);
            User foundUser = userService.getUserById(id);
            if (foundUser != null) {
                logger.info("User found with id {}", id);
                return ResponseEntity.ok(foundUser);
            } else {
                logger.warn("User not found with id {}", id);
                return ResponseEntity.notFound().build();
            }
        } else {
            // Access denied
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }
    @PostMapping("/guest")
    public ResponseEntity<User> createGuestUser() {
        User guestUser = userService.createGuestUser();
        logger.info("Guest user created with id {}", guestUser.getId());
        return new ResponseEntity<>(guestUser, CREATED);
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User newUser, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (user != null && user.getRole().equals("ROLE_ADMIN")) {
            // Access granted
            logger.info("Creating a new user");
            User createdUser = userService.createUser(newUser);
            logger.info("User created with id {}", createdUser.getId());
            return new ResponseEntity<>(createdUser, CREATED);
        } else {
            // Access denied
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    // Additional methods...

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")  // Example: Only users with 'ROLE_ADMIN' can access this endpoint
    public ResponseEntity<Void> deleteUser(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (user != null && user.getRole().equals("ROLE_ADMIN")) {
            // Access granted
            logger.info("Deleting user with id {}", id);
            userService.deleteUser(id);
            logger.info("User deleted with id {}", id);
            return new ResponseEntity<>(OK);
        } else {
            // Access denied
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }
}
