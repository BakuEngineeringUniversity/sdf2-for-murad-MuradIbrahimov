// Price.java

package az.murad.mallRestaurant.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price {

    @Id
    private String id;

    private String value;

    // Constructors, getters, and setters
}
