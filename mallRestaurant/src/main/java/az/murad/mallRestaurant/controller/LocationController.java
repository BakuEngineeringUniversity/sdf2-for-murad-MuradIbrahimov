// LocationController.java

package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Entity.Location;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.services.LocationService;
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
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    public LocationController(LocationService locationService, UserService userService, JwtUtil jwtUtil) {
        this.locationService = locationService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Location> getLocations() {
        // Validate token and check user role
        logger.info("Fetching all locations");
        List<Location> locations = locationService.getAllLocations();
        logger.info("Fetched {} locations", locations.size());
        return locations;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable String id) {
        // Validate token and check user role
        logger.info("Fetching location with id {}", id);
        Location location = locationService.getLocationById(id);
        if (location != null) {
            logger.info("Location found with id {}", id);
            return ResponseEntity.ok(location);
        } else {
            logger.warn("Location not found with id {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Creating a new location");
            Location createdLocation = locationService.createLocation(location);
            logger.info("Location created with id {}", createdLocation.getId());
            return new ResponseEntity<>(createdLocation, CREATED);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable String id, @RequestBody Location updatedLocation, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Updating location with id {}", id);
            Location updated = locationService.updateLocation(id, updatedLocation);
            if (updated != null) {
                logger.info("Location updated with id {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Location not found for update with id {}", id);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Deleting location with id {}", id);
            locationService.deleteLocation(id);
            logger.info("Location deleted with id {}", id);
            return new ResponseEntity<>(OK);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }
}
