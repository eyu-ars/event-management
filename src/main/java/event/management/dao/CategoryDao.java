package event.management.dao;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import event.management.entity.Category;


public interface CategoryDao extends JpaRepository<Category, Long> {

  /**
   * A method takes a set of string categories and returns a set of categories those found in the data base.
   * @param categories Set<Category>
   * @return set of categories found in DB.
   */
  Set<Category> findAllByCategoryNameIn(Set<String> categories); 
  
}
