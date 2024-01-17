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
    private String id; // Use String for ID

    private Date createDate = new Date();
    private String restaurantName; // Restaurant name
    private String category;
    private String name;
    private double cost;
    private int star;
    private int time;
    private String imageUrl;
}
