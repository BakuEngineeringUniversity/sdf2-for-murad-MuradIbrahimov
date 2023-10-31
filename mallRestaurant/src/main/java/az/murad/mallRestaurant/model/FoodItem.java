package az.murad.mallRestaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    private String id; // Use String for ID
    private String name;
    private double cost; // Cost of the food item
    private int star; // Star rating for the food item
    private int time; // Preparation time in minutes
    private String category; // Category of the food item
    // Other food item properties, getters, and setters
}

