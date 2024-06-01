// PriceService.java

package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.Price;
import az.murad.mallRestaurant.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    public Price getPriceById(String id) {
        return priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));
    }

    public Price createPrice(Price newPrice) {
        return priceRepository.save(newPrice);
    }

    public Price updatePrice(String id, Price updatedPrice) {
        Price existingPrice = priceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Price not found with id: " + id));

        // Apply updates only to non-null fields
        if (updatedPrice.getValue() != null) {
            existingPrice.setValue(updatedPrice.getValue());
        }

        // Save the updated price
        return priceRepository.save(existingPrice);
    }

    public void deletePrice(String id) {
        priceRepository.deleteById(id);
    }

    // Additional methods...
}
