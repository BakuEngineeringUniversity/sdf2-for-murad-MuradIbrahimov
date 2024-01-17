package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.Entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByRestaurantName(String restaurantName);
}
