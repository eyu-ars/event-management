package event.management.controller.model;

import java.time.LocalDate;
import java.time.LocalTime;
import event.management.entity.EventDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventDetailData {
  private Long eventDetailId;
  private String description;
  private LocalDate date;
  private LocalTime startTime;
  private LocalTime endTime;
  private Boolean isFree;
  private Boolean availability;


  public EventDetailData(EventDetail eventDetail) {
    this.eventDetailId = eventDetail.getEventDetailId();
    this.description = eventDetail.getDescription();
    this.date = eventDetail.getDate();
    this.startTime = eventDetail.getStartTime();
    this.endTime = eventDetail.getEndTime();
    this.isFree = eventDetail.getIsFree();
    this.availability = eventDetail.getAvailability();
  }
}
