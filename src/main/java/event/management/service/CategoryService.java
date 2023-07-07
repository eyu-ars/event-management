package event.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import event.management.controller.model.CategoryData;
import event.management.dao.CategoryDao;
import event.management.dao.EventDao;
import event.management.entity.Category;
import event.management.entity.Event;

@Service
public class CategoryService {

  @Autowired
  private CategoryDao categoryDao;
  
  @Autowired
  private EventDao eventDao;


  /**
   * A service layer method that save or update a category.
   * 
   * @param categoryData CategoryData
   * @return created or modified category
   */
  @Transactional(readOnly = false)
  public CategoryData saveOrUpdateCategory(CategoryData categoryData) {
    Category category = findOrCreateCategory(categoryData.getCategoryId());

    copyCategoryFields(category, categoryData);

    return new CategoryData(categoryDao.save(category));
  }


  /**
   * A convenient method for setting category object fields.
   * 
   * @param category Category
   * @param categoryData CategoryData
   */
  private void copyCategoryFields(Category category, CategoryData categoryData) {
    category.setCategoryId(categoryData.getCategoryId());
    category.setCategoryName(categoryData.getCategoryName());
    category.setCategoryNote(categoryData.getCategoryNote());
  }


  /**
   * A method that accepts pet category id and returns category object associated with a give Id if
   * category Id is not null, otherwise creating new category object.
   * 
   * @param categoryId Long
   * @return category object.
   */
  private Category findOrCreateCategory(Long categoryId) {
    Category category;
    if (Objects.isNull(categoryId)) {
      category = new Category();
    } else {
      category = findCategory(categoryId);
    }

    return category;
  }


  /**
   * A method finds category detail from a given category Id.
   * 
   * @param categoryId
   * @return category object.
   * @throws NoSuchElementException category Id is not found.
   */
  public Category findCategory(Long categoryId) {
    return categoryDao.findById(categoryId).orElseThrow(
        () -> new NoSuchElementException("Category with ID=" + categoryId + " was not found."));
  }


  /**
   * A service layer method that retrieves all categories.
   * 
   * @return List of categories
   * @throws NoSuchElementException if no categories are found.
   */
  @Transactional(readOnly = true)
  public List<CategoryData> retrieveAllCategory() {
    List<CategoryData> allCategory = new ArrayList<>();
    allCategory = categoryDao.findAll().stream().map(CategoryData::new).toList();

    if (allCategory.isEmpty()) {
      throw new NoSuchElementException("No Categories");
    }

    return allCategory;
  }


  /**
   * A method finds a category from a given category Id.
   * @param categoryId Long
   * @return CategoryData object.
   */
  @Transactional(readOnly = true)
  public CategoryData retrieveCategoryById(Long categoryId) {
    return new CategoryData(findCategory(categoryId));
  }

  
  /**
   * A method deletes a category by a given category Id if the category is not associated with any event.
   * 
   * @param categoryId Long
   * @return deletion message.
   * @throws UnsupportedOperationException if the category is associated with any event.
   */
  @Transactional(readOnly = false)
  public Map<String, String> deleteCategoryById(Long categoryId) {

    List<Event> associatedEvents = eventDao.findAllByCategoriesCategoryId(categoryId);

    // Checking category Id is associated to events. If it is, throw exception. we can not
    // perform the deletion.
    if (associatedEvents.size() > 0) {
      throw new UnsupportedOperationException(
          "Can not be deleted!!! Category with ID=" + categoryId + " is already associated with event");
    }

    // Otherwise perform deletion.
    Category category = findCategory(categoryId);
    categoryDao.delete(category);

    return Map.of("massage", "Deletion of category with ID=" + categoryId + " was successful.");
  }
}
