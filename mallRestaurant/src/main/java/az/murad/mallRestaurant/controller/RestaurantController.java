package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.model.Restaurant;
import az.murad.mallRestaurant.services.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // Initialize some mock data
    private final List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;

        // Add mock data
        restaurants.add(new Restaurant("01", "McDonalds"));
        restaurants.add(new Restaurant("02", "KFC"));
        restaurants.add(new Restaurant("03", "Papa Johns"));
    }

    @GetMapping
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurants.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);

        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        restaurant.setId(String.valueOf((long) (restaurants.size() + 1))); // Generate a new ID
        restaurants.add(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant updatedRestaurant) {
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            if (restaurant.getId().equals(id)) {
                restaurant.setName(updatedRestaurant.getName());
                return ResponseEntity.ok(restaurant);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        boolean removed = restaurants.removeIf(restaurant -> restaurant.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

