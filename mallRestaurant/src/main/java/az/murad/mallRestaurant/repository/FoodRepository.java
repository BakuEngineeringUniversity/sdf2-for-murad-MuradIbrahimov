package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.Entity.FoodItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<FoodItem, String> {
}
