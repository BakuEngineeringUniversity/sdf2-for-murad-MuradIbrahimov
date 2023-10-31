package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.model.FoodItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class FoodService {

    private final List<FoodItem> foodItems = new ArrayList<>();
    private int nextId = 1;

    public List<FoodItem> getAllFoodItems() {
        return foodItems;
    }

    public FoodItem getFoodItemById(String id) {
        return foodItems.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    public FoodItem createFoodItem(FoodItem foodItem) {
        foodItem.setId(String.valueOf(nextId));
        foodItems.add(foodItem);
        nextId++; // Increment the ID for the next item
        return foodItem;
    }

    public FoodItem updateFoodItem(String id, FoodItem updatedFoodItem) {
        for (int i = 0; i < foodItems.size(); i++) {
            FoodItem food = foodItems.get(i);
            if (food.getId().equals(id)) {
                // Update all properties with the new values
                food.setName(updatedFoodItem.getName());
                food.setCost(updatedFoodItem.getCost());
                food.setStar(updatedFoodItem.getStar());
                food.setTime(updatedFoodItem.getTime());
                food.setCategory(updatedFoodItem.getCategory());
                return food;
            }
        }
        return null; // Food item with the given ID doesn't exist
    }

    public boolean deleteFoodItem(String id) {
        return foodItems.removeIf(foodItem -> foodItem.getId().equals(id));
    }
}
