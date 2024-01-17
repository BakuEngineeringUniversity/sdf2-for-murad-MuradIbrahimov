package az.murad.mallRestaurant.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private User user; // Reference to the user who placed the order
    private FoodItem foodItem; // Reference to the ordered food item
    private String restaurantName; // Restaurant name
    private int quantity; // Quantity of the ordered food item
    private String status; // Order status (accepted, canceled, preparing, waiting)
    private double totalCost; // Total cost of the order

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    // Additional order attributes, getters, and setters
}
