package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Entity.FoodItem;
import az.murad.mallRestaurant.services.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/foods")
public class ProductController {

    private final FoodService foodService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public List<FoodItem> getFoods() {
        logger.info("Fetching all food items");
        List<FoodItem> foodItems = foodService.getAllFoodItems();
        logger.info("Fetched {} food items", foodItems.size());
        return foodItems;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItemById(@PathVariable String id) {
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
    public ResponseEntity<FoodItem> createFoodItem(@RequestBody FoodItem foodItem) {
        logger.info("Creating a new food item");
        FoodItem createdFoodItem = foodService.createFoodItem(foodItem);
        logger.info("Food item created with id {}", createdFoodItem.getId());
        return new ResponseEntity<>(createdFoodItem, CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable String id, @RequestBody FoodItem updatedFoodItem) {
        logger.info("Updating food item with id {}", id);
        FoodItem updated = foodService.updateFoodItem(id, updatedFoodItem);
        if (updated != null) {
            logger.info("Food item updated with id {}", id);
            return ResponseEntity.ok(updated);
        } else {
            logger.warn("Food item not found for update with id {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable String id) {
        logger.info("Deleting food item with id {}", id);
        foodService.deleteFoodItem(id);
        logger.info("Food item deleted with id {}", id);
        return new ResponseEntity<>(OK);
    }
}
