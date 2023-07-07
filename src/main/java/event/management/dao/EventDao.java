package event.management.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import event.management.entity.Event;

public interface EventDao extends JpaRepository<Event, Long> {

  /**
   * A method returns a list of event by a specific category Id.
   * @param categoryId Long
   * @return list of event.
   */
  List<Event> findAllByCategoriesCategoryId(Long categoryId);  
}
