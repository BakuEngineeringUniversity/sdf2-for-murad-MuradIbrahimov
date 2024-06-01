package az.murad.mallRestaurant.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {

    @Id
    private String id;

    private boolean bestFood;
    private int categoryId; // Assuming CategoryId corresponds to the category ID
    private String description;
    private String imagePath;
    private int locationId; // Assuming LocationId corresponds to the location ID
    private double price;
    private int priceId; // Assuming PriceId corresponds to the price ID
    private double star;
    private int timeId; // Assuming TimeId corresponds to the time ID
    private int timeValue;
    private String title; // Assuming Title corresponds to the food item name

    // Additional fields, getters, and setters as needed
    private Date createDate = new Date();
    private String restaurantName; // Assuming there's a restaurant name field

    // Constructors and methods
}
