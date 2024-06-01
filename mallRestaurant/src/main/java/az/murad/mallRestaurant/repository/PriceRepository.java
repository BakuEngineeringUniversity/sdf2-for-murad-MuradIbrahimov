// PriceRepository.java

package az.murad.mallRestaurant.repository;

import az.murad.mallRestaurant.Entity.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, String> {
}
