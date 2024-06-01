// TimeRepository.java

package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.Entity.Time;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeRepository extends MongoRepository<Time, String> {
}
