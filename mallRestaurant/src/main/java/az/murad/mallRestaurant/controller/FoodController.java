package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.model.FoodItem;
import az.murad.mallRestaurant.services.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;

        // Initialize some mock data
        initializeMockData();
    }

    private void initializeMockData() {
        FoodItem foodItem1 = new FoodItem();
        foodItem1.setName("Burger");
        foodItem1.setCost(10.99);
        foodItem1.setStar(4);
        foodItem1.setTime(15);
        foodItem1.setCategory("Fast Food");

        FoodItem foodItem2 = new FoodItem();
        foodItem2.setName("Pizza");
        foodItem2.setCost(12.99);
        foodItem2.setStar(4);
        foodItem2.setTime(20);
        foodItem2.setCategory("Italian");

        // Add mock data to the service
        foodService.createFoodItem(foodItem1);
        foodService.createFoodItem(foodItem2);
    }

    @GetMapping
    public List<FoodItem> getFoods() {
        return foodService.getAllFoodItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItemById(@PathVariable String id) {
        FoodItem foodItem = foodService.getFoodItemById(String.valueOf(id));
        return foodItem != null ? ResponseEntity.ok(foodItem) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FoodItem> createFoodItem(@RequestBody FoodItem foodItem) {
        FoodItem createdFoodItem = foodService.createFoodItem(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFoodItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable Long id, @RequestBody FoodItem updatedFoodItem) {
        FoodItem updated = foodService.updateFoodItem(String.valueOf(Math.toIntExact(id)), updatedFoodItem);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        if (foodService.deleteFoodItem(String.valueOf(Math.toIntExact(id)))) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}