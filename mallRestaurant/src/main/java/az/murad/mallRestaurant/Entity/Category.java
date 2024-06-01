// Category.java

package az.murad.mallRestaurant.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;

    private String imagePath;
    private String name;

    // Constructors, getters, and setters
}
