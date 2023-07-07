package event.management.controller.model;

import java.util.HashSet;
import java.util.Set;
import event.management.entity.Category;
import event.management.entity.Event;
import event.management.entity.EventDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventData {
  private Long eventId;
  private String eventName;
  private String frequency;
  private String duration;
  private String eventNote;
  
  private Set<String> categories = new HashSet<>();
  
  private Set<EventDetailData> eventDetails = new HashSet<>();
  
  public EventData(Event event) {
    this.eventId = event.getEventId();
    this.eventName = event.getEventName();
    this.frequency = event.getFrequency();
    this.duration = event.getDuration();
    this.eventNote = event.getEventNote();
    
    for(Category category : event.getCategories()) {
      categories.add(category.getCategoryName());
    }
    
    for(EventDetail eventDetail : event.getEventDetails()) {
      eventDetails.add(new EventDetailData(eventDetail));
    }
  }
}
