package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.exception.UserNotFoundException;
import az.murad.mallRestaurant.model.User;
import az.murad.mallRestaurant.services.UserService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        logger.log(Level.INFO, "Fetching all users");
        List<User> users = userService.getAllUsers();
        logger.log(Level.INFO, "Fetched {0} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        logger.log(Level.INFO, "Fetching user with id {0}", id);
        User user = userService.getUserById(id);
        if (user != null) {
            logger.log(Level.INFO, "User found with id {0}", id);
            return ResponseEntity.ok(user);
        } else {
            String errorMessage = "User not found with id: " + id;
            logger.log(Level.WARNING, errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        logger.log(Level.INFO, "Creating a new user");
        User createdUser = userService.createUser(newUser);
        logger.log(Level.INFO, "User created with id {0}", createdUser.getId());
        return new ResponseEntity<>(createdUser, CREATED);
    }

    // Additional methods...

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        logger.log(Level.INFO, "Deleting user with id {0}", id);
        userService.deleteUser(id);
        logger.log(Level.INFO, "User deleted with id {0}", id);
        return new ResponseEntity<>(OK);
    }
}
