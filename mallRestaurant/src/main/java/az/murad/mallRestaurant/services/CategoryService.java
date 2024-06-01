// CategoryService.java

package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.Category;
import az.murad.mallRestaurant.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category createCategory(Category newCategory) {
        return categoryRepository.save(newCategory);
    }

    public Category updateCategory(String id, Category updatedCategory) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Apply updates only to non-null fields
        if (updatedCategory.getName() != null) {
            existingCategory.setName(updatedCategory.getName());
        }
        if (updatedCategory.getImagePath() != null) {
            existingCategory.setImagePath(updatedCategory.getImagePath());
        }

        // Save the updated category
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    // Additional methods...
}
