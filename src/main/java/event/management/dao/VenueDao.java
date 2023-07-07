package event.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import event.management.entity.Venue;

public interface VenueDao extends JpaRepository<Venue, Long> {

}
