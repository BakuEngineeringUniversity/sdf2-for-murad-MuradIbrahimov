package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.model.FoodItem;
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
        if (updatedFoodItem.getName() != null) {
            existingFoodItem.setName(updatedFoodItem.getName());
        }
        if (updatedFoodItem.getRestaurantName() != null) {
            existingFoodItem.setRestaurantName(updatedFoodItem.getRestaurantName());
        }
        if (updatedFoodItem.getCost() != 0) {
            existingFoodItem.setCost(updatedFoodItem.getCost());
        }
        if (updatedFoodItem.getStar() != 0) {
            existingFoodItem.setStar(updatedFoodItem.getStar());
        }
        if (updatedFoodItem.getTime() != 0) {
            existingFoodItem.setTime(updatedFoodItem.getTime());
        }
        if (updatedFoodItem.getCategory() != null) {
            existingFoodItem.setCategory(updatedFoodItem.getCategory());
        }
        if (updatedFoodItem.getImageUrl() != null) {
            existingFoodItem.setImageUrl(updatedFoodItem.getImageUrl());
        }

        // Save the updated food item
        return foodRepository.save(existingFoodItem);
    }



    public void deleteFoodItem(String id) {
        foodRepository.deleteById(id);
    }


    // Additional methods...
}
