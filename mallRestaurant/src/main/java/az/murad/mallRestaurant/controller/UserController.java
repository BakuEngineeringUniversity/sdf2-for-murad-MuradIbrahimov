package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.exception.InvalidTokenException;
import az.murad.mallRestaurant.exception.LoginFailedException;
import az.murad.mallRestaurant.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String token) {
        try {
            // Validate token and check user role
            String username = jwtUtil.getUsernameFromToken(token);
            User user = userService.getUserByUsername(username);

            if (user != null && user.getRole().equals("ROLE_ADMIN")) {
                // Access granted
                logger.info("Fetching all users");
                List<User> users = userService.getAllUsers();
                logger.info("Fetched {} users", users.size());
                return ResponseEntity.ok(users);
            } else {
                // Access denied
                logger.warn("Access denied for user: {}", username);
                throw new AccessDeniedException("Access denied");
            }
        } catch (Exception e) {
            // Handle other exceptions
            logger.error("Error during processing", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        // Validate token and check user role
            logger.info("Deleting user with id {}", id);
            userService.deleteUser(id);
            logger.info("User deleted with id {}", id);
            return new ResponseEntity<>(OK);

    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser,
                                           @RequestHeader("Authorization") String token) {
        try {
            // Validate token and check user role
            String username = jwtUtil.getUsernameFromToken(token);
            User adminUser = userService.getUserByUsername(username);

            if (adminUser != null && adminUser.getRole().equals("ROLE_ADMIN")) {
                // Access granted

                // Ensure the ID in the path matches the ID in the request body
                if (!id.equals(updatedUser.getId())) {
                    logger.warn("Mismatched IDs in path and request body");
                    return ResponseEntity.badRequest().build();
                }

                // Fetch the existing user
                User existingUser = userService.getUserById(id);

                if (existingUser != null) {
                    // Update properties
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setPassword(updatedUser.getPassword());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setRole(updatedUser.getRole());
                    existingUser.setIsGuest(updatedUser.isGuest());

                    // Save the updated user
                    User updatedUserEntity = userService.updateUser(existingUser);

                    logger.info("User updated with id {}", id);
                    return ResponseEntity.ok(updatedUserEntity);
                } else {
                    logger.warn("User not found with id {}", id);
                    return ResponseEntity.notFound().build();
                }
            } else {
                // Access denied
                logger.warn("Access denied for user: {}", username);
                throw new AccessDeniedException("Access denied");
            }
        } catch (Exception e) {
            logger.error("Error updating user", e);
            throw new RuntimeException("Error updating user");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        try {
            // Validate token and check user role
            String username = jwtUtil.getUsernameFromToken(token);
            User user = userService.getUserByUsername(username);

            if (user != null && !jwtUtil.isTokenBlacklisted(token)) {
                // Logout: Add the token to the blacklist
                jwtUtil.addToBlacklist(token);
                logger.info("User logged out: {}", username);
                System.out.println("Token blacklisted during logout: " + token);
                return new ResponseEntity<>(OK);
            } else {
                // Access denied or token already blacklisted
                logger.warn("Access denied or token already blacklisted for user: {}", username);
                System.out.println("Access denied or token already blacklisted for user: " + username);
                throw new AccessDeniedException("Access denied or token already blacklisted");
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            System.out.println("Exception during logout: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
