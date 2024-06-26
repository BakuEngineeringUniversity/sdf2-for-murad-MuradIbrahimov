// FoodController.java

package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Entity.FoodItem;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.services.FoodService;
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
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);

    @Autowired
    public FoodController(FoodService foodService, UserService userService, JwtUtil jwtUtil) {
        this.foodService = foodService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<FoodItem> getFoods() {
        // Validate token and check user role
        logger.info("Fetching all food items");
        List<FoodItem> foodItems = foodService.getAllFoodItems();
        logger.info("Fetched {} food items", foodItems.size());
        return foodItems;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItemById(@PathVariable String id) {
        // Validate token and check user role
        logger.info("Fetching food item with id {}", id);
        FoodItem foodItem = foodService.getFoodItemById(id);
        if (foodItem != null) {
            logger.info("Food item found with id {}", id);
            return ResponseEntity.ok(foodItem);
        } else {
            logger.warn("Food item not found with id {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FoodItem> createFoodItem(@RequestBody FoodItem foodItem, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Creating a new food item");
            FoodItem createdFoodItem = foodService.createFoodItem(foodItem);
            logger.info("Food item created with id {}", createdFoodItem.getId());
            return new ResponseEntity<>(createdFoodItem, CREATED);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable String id, @RequestBody FoodItem updatedFoodItem, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Updating food item with id {}", id);
            FoodItem updated = foodService.updateFoodItem(id, updatedFoodItem);
            if (updated != null) {
                logger.info("Food item updated with id {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Food item not found for update with id {}", id);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Deleting food item with id {}", id);
            foodService.deleteFoodItem(id);
            logger.info("Food item deleted with id {}", id);
            return new ResponseEntity<>(OK);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }
    @GetMapping("/best-foods")
    public List<FoodItem> getBestFoods() {
        // Validate token and check user role
        logger.info("Fetching best food items");
        List<FoodItem> bestFoodItems = foodService.getBestFoodItems();
        logger.info("Fetched {} best food items", bestFoodItems.size());
        return bestFoodItems;
    }
}
