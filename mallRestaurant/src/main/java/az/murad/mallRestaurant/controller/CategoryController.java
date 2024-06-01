
package az.murad.mallRestaurant.controller;

import az.murad.mallRestaurant.Entity.Category;
import az.murad.mallRestaurant.Entity.User;
import az.murad.mallRestaurant.Util.JwtUtil;
import az.murad.mallRestaurant.services.CategoryService;
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
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService, JwtUtil jwtUtil) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Category> getCategories() {
        // Validate token and check user role
        logger.info("Fetching all categories");
        List<Category> categories = categoryService.getAllCategories();
        logger.info("Fetched {} categories", categories.size());
        return categories;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        // Validate token and check user role
        logger.info("Fetching category with id {}", id);
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            logger.info("Category found with id {}", id);
            return ResponseEntity.ok(category);
        } else {
            logger.warn("Category not found with id {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Creating a new category");
            Category createdCategory = categoryService.createCategory(category);
            logger.info("Category created with id {}", createdCategory.getId());
            return new ResponseEntity<>(createdCategory, CREATED);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category updatedCategory, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Updating category with id {}", id);
            Category updated = categoryService.updateCategory(id, updatedCategory);
            if (updated != null) {
                logger.info("Category updated with id {}", id);
                return ResponseEntity.ok(updated);
            } else {
                logger.warn("Category not found for update with id {}", id);
                return ResponseEntity.notFound().build();
            }
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id, @RequestHeader("Authorization") String token) {
        // Validate token and check user role
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // Check if the user has the required role and authentication is not null
        if (user != null && (user.getRole().equals("ROLE_RESTAURANT") || user.getRole().equals("ROLE_ADMIN"))) {
            logger.info("Deleting category with id {}", id);
            categoryService.deleteCategory(id);
            logger.info("Category deleted with id {}", id);
            return new ResponseEntity<>(OK);
        } else {
            logger.warn("Access denied for user: {}", username);
            throw new AccessDeniedException("Access denied");
        }
    }
}
