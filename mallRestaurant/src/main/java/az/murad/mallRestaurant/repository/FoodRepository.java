package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.Entity.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FoodRepository extends MongoRepository<FoodItem, String> {
    List<FoodItem> findByBestFood(boolean bestFood);
}
