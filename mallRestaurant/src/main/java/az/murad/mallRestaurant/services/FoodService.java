// FoodService.java

package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.FoodItem;
import az.murad.mallRestaurant.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<FoodItem> getAllFoodItems() {
        return foodRepository.findAll();
    }

    public FoodItem getFoodItemById(String id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
    }

    public FoodItem createFoodItem(FoodItem newFoodItem) {
        return foodRepository.save(newFoodItem);
    }

    public FoodItem updateFoodItem(String id, FoodItem updatedFoodItem) {
        FoodItem existingFoodItem = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

        // Apply updates only to non-null fields
        if (updatedFoodItem.getTitle() != null) {
            existingFoodItem.setTitle(updatedFoodItem.getTitle());
        }
        if (updatedFoodItem.getRestaurantName() != null) {
            existingFoodItem.setRestaurantName(updatedFoodItem.getRestaurantName());
        }
        if (updatedFoodItem.getPrice() != 0) {
            existingFoodItem.setPrice(updatedFoodItem.getPrice());
        }
        if (updatedFoodItem.getStar() != 0) {
            existingFoodItem.setStar(updatedFoodItem.getStar());
        }
        if (updatedFoodItem.getTimeValue() != 0) {
            existingFoodItem.setTimeValue(updatedFoodItem.getTimeValue());
        }
        if (updatedFoodItem.getCategoryId() != 0) {
            existingFoodItem.setCategoryId(updatedFoodItem.getCategoryId());
        }
        if (updatedFoodItem.getImagePath() != null) {
            existingFoodItem.setImagePath(updatedFoodItem.getImagePath());
        }
        if (updatedFoodItem.getDescription() != null) {
            existingFoodItem.setDescription(updatedFoodItem.getDescription());
        }

        // Save the updated food item
        return foodRepository.save(existingFoodItem);
    }



    public void deleteFoodItem(String id) {
        foodRepository.deleteById(id);
    }

    public List<FoodItem> getBestFoodItems() {
        return foodRepository.findByBestFood(true);
    }
    // Additional methods...
}
