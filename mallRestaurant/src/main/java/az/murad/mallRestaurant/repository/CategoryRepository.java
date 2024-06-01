// CategoryRepository.java

package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.Entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
