package az.murad.mallRestaurant.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;
    private String userId; // ID of the user placing the order
    private List<FoodItem> foodItems; // List of food items in the order
    private String status; // Order status (accepted, canceled, preparing, waiting)
    private double totalCost; // Total cost of the order

    // Additional fields, getters, and setters
}
