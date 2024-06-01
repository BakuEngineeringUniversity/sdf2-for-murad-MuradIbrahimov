// LocationService.java

package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.Location;
import az.murad.mallRestaurant.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(String id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    public Location createLocation(Location newLocation) {
        return locationRepository.save(newLocation);
    }

    public Location updateLocation(String id, Location updatedLocation) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        // Apply updates only to non-null fields
        if (updatedLocation.getLoc() != null) {
            existingLocation.setLoc(updatedLocation.getLoc());
        }

        // Save the updated location
        return locationRepository.save(existingLocation);
    }

    public void deleteLocation(String id) {
        locationRepository.deleteById(id);
    }

    // Additional methods...
}
