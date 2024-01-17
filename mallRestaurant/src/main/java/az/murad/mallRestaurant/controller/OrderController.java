package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Entity.Order;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.services.OrderService;
import az.murad.mallRestaurant.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public OrderController(OrderService orderService,UserService userService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        // Validate and process the order
        Order placedOrder = orderService.placeOrder(order);
        return new ResponseEntity<>(placedOrder, CREATED);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RESTAURANT')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String id, @RequestBody String status,
                                                   @RequestHeader("Authorization") String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        // Check if the user has the required role and authentication is not null
        if (user != null && user.getRole().equals("ROLE_RESTAURANT")) {
            // Update order status (for admins and restaurants)
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            if (updatedOrder != null) {
                return ResponseEntity.ok(updatedOrder);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Order> getAllOrders(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        // Check if the user has the required role and authentication is not null
        if (user != null && user.getRole().equals("ROLE_RESTAURANT")) {
            // Retrieve all orders (only for admins)
            return orderService.getAllOrders();
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id, Authentication authentication) {
        // Retrieve order by ID
        Order order = orderService.getOrderById(id);

        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/restaurant/{restaurantName}")
    public List<Order> getOrdersByRestaurant(@PathVariable String restaurantName, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && user.getRole().equals("ROLE_RESTAURANT")) {
            // Check if the authenticated user matches the requested restaurant
            if (!user.getUsername().equals(restaurantName)) {
                throw new AccessDeniedException("Access denied");
            }

            // Retrieve orders for the specified restaurant
            List<Order> orders = orderService.getOrdersByRestaurant(restaurantName);

            if (orders != null) {
                return orders;
            } else {
                throw new AccessDeniedException("Access denied");
            }
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

}
