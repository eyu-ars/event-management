package event.management.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import event.management.entity.EventDetail;

public interface EventDetailDao extends JpaRepository<EventDetail, Long> {

  List<EventDetail> findAllByDate(LocalDate date);

  List<EventDetail> findAllByEventEventId(Long eventId);

  List<EventDetail> findAllByEventEventName(String event);
  
  List<EventDetail> findAllByVenueVenueId(Long eventId);

  List<EventDetail> findAllByVenueVenueName(String venue);
  
  
  /**
   * Updates a single record availability column on event detail table by event detail Id.
   * @param availability Boolean
   * @param eventDetailId Long
   */
  @Modifying                            
  @Query(value = "UPDATE event_detail ed SET ed.availability = ?1 WHERE ed.event_detail_id = ?2", nativeQuery = true)
  void updateEventDetailAvailabilityById(Boolean availability, Long eventDetailId);
  
 
  /**
   * Updates a group of records availability column on event detail table by a given date.
   * @param availability Boolean
   * @param date LocalDate
   * @return
   */
  @Modifying                            
  @Query(value = "UPDATE event_detail ed SET ed.availability = ?1 WHERE ed.date = ?2", nativeQuery = true)
  int updateEventDetailAvailabilityByDate(Boolean availability, LocalDate date);
  
}
