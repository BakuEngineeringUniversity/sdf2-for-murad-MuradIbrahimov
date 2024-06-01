// PriceController.java

package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Entity.Price;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.services.PriceService;
import az.murad.mallRestaurant.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    public PriceController(PriceService priceService, UserService userService, JwtUtil jwtUtil) {
        this.priceService = priceService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Price> getPrices() {
        // Validate token and check user role
        logger.info("Fetching all prices");
        List<Price> prices = priceService.getAllPrices();
        logger.info("Fetched {} prices", prices.size());
        return prices;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Price> getPriceById(@PathVariable String id) {
        // Validate token and check user role
        logger.info("Fetching price with id {}", id);
        Price price = priceService.getPriceById(id);
        if (price != null) {
            logger.info("Price found with id {}", id);
            return ResponseEntity.ok(price);
        } else {
            logger.warn("Price not found with id {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Price> createPrice(@RequestBody Price price, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Creating a new price");
            Price createdPrice = priceService.createPrice(price);
            logger.info("Price created with id {}", createdPrice.getId());
            return new ResponseEntity<>(createdPrice, CREATED);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable String id, @RequestBody Price updatedPrice, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Updating price with id {}", id);
            Price updated = priceService.updatePrice(id, updatedPrice);
            if (updated != null) {
                logger.info("Price updated with id {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Price not found for update with id {}", id);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Deleting price with id {}", id);
            priceService.deletePrice(id);
            logger.info("Price deleted with id {}", id);
            return new ResponseEntity<>(OK);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }
}
