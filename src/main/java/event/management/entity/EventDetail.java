package event.management.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class EventDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long eventDetailId;
  
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "event_id")
  private Event event;  
  
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "venue_id")
  private Venue venue;
  
  private String description;
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;
  private Boolean isFree;
  private Boolean availability;
  

}
