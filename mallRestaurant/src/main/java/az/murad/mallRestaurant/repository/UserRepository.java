package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Spring Data JPA will automatically create a query for this method
    User findByEmailAndPassword(String email, String password);

}
