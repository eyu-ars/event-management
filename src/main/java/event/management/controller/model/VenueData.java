package event.management.controller.model;

import java.util.HashSet;
import java.util.Set;
import event.management.entity.EventDetail;
import event.management.entity.Venue;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VenueData {
  private Long venueId;  
  private String venueName;
  private Integer capacity;
  private String venueAddress;
  private String venueCity;
  private String venueZip;
  private String venueNote;
  
  private Set<EventDetailData> eventDetails = new HashSet<>();
  
  public VenueData(Venue venue) {
    this.venueId = venue.getVenueId();    
    this.venueName = venue.getVenueName();
    this.capacity = venue.getCapacity();
    this.venueAddress = venue.getVenueAddress();
    this.venueCity = venue.getVenueCity();
    this.venueZip = venue.getVenueZip();
    this.venueNote = venue.getVenueNote();
    
    for(EventDetail eventDetail : venue.getEventDetails()) {
      eventDetails.add(new EventDetailData(eventDetail));
    } // End of loop
    
  } // End of constructor
    
} // End of class
