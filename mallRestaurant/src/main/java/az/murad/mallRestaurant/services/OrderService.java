package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.FoodItem;
import az.murad.mallRestaurant.Entity.Order;
import az.murad.mallRestaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodService foodService;

    public OrderService(OrderRepository orderRepository, FoodService foodService) {
        this.orderRepository = orderRepository;
        this.foodService = foodService;
    }

    public Order placeOrder(Order order) {
        // Validate the order and update total cost
        String foodItemId = order.getFoodItemId();
        FoodItem foodItem = foodService.getFoodItemById(foodItemId);

        if (foodItem != null) {
            double totalCost = foodItem.getCost() * order.getQuantity();
            order.setTotalCost(totalCost);

            // Save the order
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("FoodItem not found with id: " + foodItemId);
        }
    }

    public Order updateOrderStatus(String id, String status) {
        // Update order status (for restaurants)
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingOrder.setStatus(status);

        // Save the updated order
        return orderRepository.save(existingOrder);
    }

    public List<Order> getAllOrders() {
        // Retrieve all orders
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        // Retrieve order by ID
        return orderRepository.findById(id)
                .orElse(null);
    }

    public List<Order> getOrdersByRestaurant(String restaurantName) {
        return orderRepository.findByRestaurantName(restaurantName);
    }

    public boolean deleteOrder(String id) {
        // Delete order by ID
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    // Additional methods...
}
