package event.management.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import event.management.controller.model.CategoryData;
import event.management.service.CategoryService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {
  
  @Autowired
  private CategoryService categoryService;
  
  
  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public CategoryData createCategory(@RequestBody CategoryData categoryData) {
    log.info("Creating category {}", categoryData);
    return categoryService.saveOrUpdateCategory(categoryData);
  }

  
  @PutMapping("/{categoryId}")
  @ResponseStatus(code = HttpStatus.OK)
  public CategoryData modifyCategory(@PathVariable Long categoryId, @RequestBody CategoryData categoryData) {
    categoryData.setCategoryId(categoryId);
    log.info("Modefiying category {}", categoryData);
    return categoryService.saveOrUpdateCategory(categoryData);
  }
  
  
  @GetMapping
  public List<CategoryData> retrieveAllCategory(){
    log.info("Retrieving all categories.");
    return categoryService.retrieveAllCategory();
  }
  
  
  @GetMapping("/{categoryId}")
  public CategoryData retrieveCategoryById(@PathVariable Long categoryId){
    log.info("Retrieving all categories.");
    return categoryService.retrieveCategoryById(categoryId);
  }
  
  
  @DeleteMapping("/{categoryId}")
  public Map<String, String> deleteEventDetailById(@PathVariable Long categoryId) {
    log.info("Deleting a category with ID= {}", categoryId);
    return categoryService.deleteCategoryById(categoryId);
  }
}
