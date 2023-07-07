package event.management.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import event.management.controller.model.EventDetailData;
import event.management.controller.model.EventDetailResponse;
import event.management.service.EventDetailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/event-details")
@Slf4j
public class EventDetailController {

  @Autowired
  private EventDetailService eventDetailService;


  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public EventDetailData saveEventDetail(
      @RequestParam(name = "event_id", required = true) Optional<Long> eventId,
      @RequestParam(name = "venue_id", required = true) Optional<Long> venueId,
      @RequestBody EventDetailData eventDetailData, HttpServletRequest request) {


    // Query parameter is required, so I have to check the parameters are null by
    // using HttpServletRequest object if either one or both of them are null then throw
    // NoSuchElementException, otherwise call the service method.
    if (Objects.isNull(request.getParameter("event_id"))
        || Objects.isNull(request.getParameter("venue_id"))) {
      throw new NoSuchElementException(
          "Valid Query Parameter Required!!! Event Id or/and Venue Id can not be null or/and "
              + "should be valid parameter name, which are event_id & venue_id.");
    }

    log.info("Creating event detail {} for event with ID={} and venue with ID={}", eventDetailData,
        eventId.get(), venueId.get());
    return eventDetailService.saveEventDetail(eventId.get(), venueId.get(), eventDetailData);
  }



  @GetMapping
  public List<EventDetailResponse> retrieveAllEventDetail(
      @RequestParam(name = "event_id", required = false) Optional<Long> eventId,
      @RequestParam(name = "event_name", required = false) Optional<String> eventName,
      @RequestParam(name = "venue_id", required = false) Optional<Long> venueId,
      @RequestParam(name = "venue_name", required = false) Optional<String> venueName,
      @RequestParam(name = "date", required = false) Optional<LocalDate> date,
      HttpServletRequest request) {

    int parameterCount = 0;
    /*
     * Counting number of query parameters
     */
    if (eventId.isPresent()) {
      parameterCount++;
    }
    if (eventName.isPresent()) {
      parameterCount++;
    }
    if (venueId.isPresent()) {
      parameterCount++;
    }
    if (venueName.isPresent()) {
      parameterCount++;
    }
    if (date.isPresent()) {
      parameterCount++;
    }
    // If number of query parameters more than one, throw an error.
    if (parameterCount > 1) {
      log.warn("Number of selected pararameters are " + parameterCount
          + " .Only one parameter allowed at a time.");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Number of selected pararameters are " + parameterCount
              + " .Only one parameter allowed at a time.");
    }

    // Checking which query parameter is present.
    // Event Id is present
    if (eventId.isPresent()) {
      log.info("Retrieving all event detail by event ID= {}", eventId);
      return eventDetailService.retrieveAllEventDetailByEventId(eventId.get());
    }
    // Event name is present
    else if (eventName.isPresent()) {
      log.info("Retrieving all event detail by event name= {}", eventName);
      return eventDetailService.retrieveAllEventDetailByEventName(eventName.get());
    }
    // Venue Id is present
    else if (venueId.isPresent()) {
      log.info("Retrieving all event detail by venue ID= {}", venueId);
      return eventDetailService.retrieveAllEventDetailByVenueId(venueId.get());
    }
    // Venue name is present
    else if (venueName.isPresent()) {
      log.info("Retrieving all event detail by venue name= {}", venueName);
      return eventDetailService.retrieveAllEventDetailByVenueName(venueName.get());
    }
    // date is present
    else if (date.isPresent()) {
      log.info("Retrieving all event detail on date {}", date);
      return eventDetailService.retrieveAllEventDetailByDate(date.get());
    }    
    // If the query parameter is valid this method
    // already calls the service layer method on the above lines based on the parameter, 
    // otherwise throw ResponseStatusException
    // If query parameter has invalid name, the size will be greater than 0.
    else if (request.getParameterMap().size() > 0) {
      log.warn(
          "Invalid query parameter!!! Only allowed date, event_id, event_name, venue_id or venue_name.");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Invalid query parameter!!! Only allowed date, event_id, event_name, venue_id or venue_name.");
    }

    // If no query parameter provided, All event detail is returned.
    log.info("Retrieving all event detail");
    return eventDetailService.retrieveAllEventDetail();
  }


  @PutMapping("/{eventDetailId}")
  public EventDetailData updateEventDetailAvailabilityById(@PathVariable Long eventDetailId) {
    log.info("Event detail with ID= {} availability status was modified to unavailable",
        eventDetailId);
    return eventDetailService.updateEventDetailAvailabilityById(eventDetailId);
  }



  @PutMapping
  public Map<String, String> updateEventDetailAvailabilityByDate(
      @RequestParam(required = false) Optional<LocalDate> date) {
    /*
     * Checking a query parameter. If the parameter is date then call the service class method,
     * otherwise returning error message
     */
    if (date.isPresent()) {
      log.info("Event detail with Date= {} availability status was modified to unavailable", date);
      return eventDetailService.updateEventDetailAvailabilityByDate(date.get());
    } else {
      return Map.of("massage", "Only 'date' is accepted as a query string name.");
    }
  }


  @DeleteMapping("/{eventDetailId}")
  public Map<String, String> deleteEventDetailById(@PathVariable Long eventDetailId) {
    log.info("Deleting event detail with ID= {}", eventDetailId);
    eventDetailService.deleteEventDetailById(eventDetailId);

    return Map.of("massage",
        "Deletion of event detail with ID=" + eventDetailId + " was successful.");
  }
}
