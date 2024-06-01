// LocationRepository.java

package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.Entity.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String> {
}
