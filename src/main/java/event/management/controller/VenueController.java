package event.management.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import event.management.controller.model.EventDetailResponse;
import event.management.controller.model.VenueData;
import event.management.service.EventDetailService;
import event.management.service.VenueService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/venues")
@Slf4j
public class VenueController {

  @Autowired
  private VenueService venueService;
  
  @Autowired
  private EventDetailService eventDetailService;
  
  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public VenueData createVenue(@RequestBody VenueData venueData) {
    log.info("Creating venue {}", venueData);
    return venueService.saveOrUpdateVenue(venueData);
  }
  
  
  @PutMapping("/{venueId}")
  @ResponseStatus(code = HttpStatus.OK)
  public VenueData modifyVenue(@PathVariable Long venueId, @RequestBody VenueData venueData) {
    venueData.setVenueId(venueId);
    log.info("Modifiying venue {}", venueData);
    return venueService.saveOrUpdateVenue(venueData);
  }
  
  
  @GetMapping
  public List<VenueData> retrieveAllVenue(){
    log.info("Retrieving all venues.");
    return venueService.retrieveAllVenue();
  }
  
  
  @GetMapping("/{venueId}")
  public VenueData retrieveEventById(@PathVariable Long venueId) {
    log.info("Retrieving venue with ID={} ", venueId);
    return venueService.retrieveVenueById(venueId);
  }
  
  
  @GetMapping("/{venueId}/event-details")
  public List<EventDetailResponse> retrieveAllEventDetailByVenueId(@PathVariable Long venueId) {
    log.info("Retrieving event detail by venue with ID={} ", venueId);
    return eventDetailService.retrieveAllEventDetailByVenueId(venueId);
  }
  
  
  @DeleteMapping("/{venueId}")
  public Map<String, String> deleteEventDetailById(@PathVariable Long venueId) {
    log.info("Deleting a venue with ID= {}", venueId);
    return venueService.deleteVenueById(venueId);
  }
  
}
