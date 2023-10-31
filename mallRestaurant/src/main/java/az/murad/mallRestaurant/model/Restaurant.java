package az.murad.mallRestaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant  {
    private String id;
    private String name;


}
