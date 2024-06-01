// TimeController.java

package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Entity.Time;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.services.TimeService;
import az.murad.mallRestaurant.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/times")
public class TimeController {

    private final TimeService timeService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(TimeController.class);

    @Autowired
    public TimeController(TimeService timeService, UserService userService, JwtUtil jwtUtil) {
        this.timeService = timeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Time> getTimes() {
        // Validate token and check user role
        logger.info("Fetching all times");
        List<Time> times = timeService.getAllTimes();
        logger.info("Fetched {} times", times.size());
        return times;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Time> getTimeById(@PathVariable String id) {
        // Validate token and check user role
        logger.info("Fetching time with id {}", id);
        Time time = timeService.getTimeById(id);
        if (time != null) {
            logger.info("Time found with id {}", id);
            return ResponseEntity.ok(time);
        } else {
            logger.warn("Time not found with id {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Time> createTime(@RequestBody Time time, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Creating a new time");
            Time createdTime = timeService.createTime(time);
            logger.info("Time created with id {}", createdTime.getId());
            return new ResponseEntity<>(createdTime, CREATED);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Time> updateTime(@PathVariable String id, @RequestBody Time updatedTime, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Updating time with id {}", id);
            Time updated = timeService.updateTime(id, updatedTime);
            if (updated != null) {
                logger.info("Time updated with id {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Time not found for update with id {}", id);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Deleting time with id {}", id);
            timeService.deleteTime(id);
            logger.info("Time deleted with id {}", id);
            return new ResponseEntity<>(OK);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }
}
