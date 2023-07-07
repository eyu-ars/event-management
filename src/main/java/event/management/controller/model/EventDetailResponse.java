package event.management.controller.model;

import java.time.LocalDate;
import java.time.LocalTime;
import event.management.entity.EventDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventDetailResponse {
  private Long eventDetailId;
  
  private Long eventId;
  private String eventName;
  private String venueName;
  
  private String description;
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;
  private Boolean isFree;
  private Boolean availability;



  public EventDetailResponse(EventDetail eventDetail) {
    this.eventDetailId = eventDetail.getEventDetailId();
    
    this.eventId = eventDetail.getEvent().getEventId();
    this.eventName = eventDetail.getEvent().getEventName();
    this.venueName = eventDetail.getVenue().getVenueName();
    
    this.description = eventDetail.getDescription();
    this.date = eventDetail.getDate();
    this.startTime = eventDetail.getStartTime();
    this.endTime = eventDetail.getEndTime();
    this.isFree = eventDetail.getIsFree();
    this.availability = eventDetail.getAvailability();


  }
}
