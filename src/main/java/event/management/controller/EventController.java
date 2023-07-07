package event.management.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import event.management.controller.model.EventData;
import event.management.controller.model.EventDetailData;
import event.management.controller.model.EventDetailResponse;
import event.management.service.EventDetailService;
import event.management.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {

  @Autowired
  private EventService eventService;

  @Autowired
  private EventDetailService eventDetailService;

  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public EventData createEvent(@RequestBody EventData eventData) {
    log.info("Creating event {}", eventData);
    return eventService.saveEvent(eventData);
  }


  @GetMapping
  public List<EventData> retrieveAllEvent() {
    log.info("Retrieving all events.");
    return eventService.retrieveAllEvent();
  }


  @GetMapping("/{eventId}")
  public EventData retrieveEventById(@PathVariable Long eventId) {
    log.info("Retrieving event with ID={} ", eventId);
    return eventService.retrieveEventById(eventId);
  }



  @GetMapping("/categories")
  public List<EventData> retrieveAllEventByCategoryID(
      @RequestParam(name = "category_id", required = false) Optional<Long> categoryId,
      @RequestParam(name = "category_name", required = false) Optional<String> categoryName,
      HttpServletRequest request) {

    /*
     * If number of query parameters more than one, throw an error.
     */
    if (categoryId.isPresent() && categoryName.isPresent()) {
      log.warn("Only one parameter allowed at a time.");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Only one parameter allowed at a time.");
    }


    /*
     * Checking which query parameter is present.
     */
    // Category Id is present
    if (categoryId.isPresent()) {
      log.info("Retrieving all events by category ID= {}", categoryId);
      return eventService.retrieveAllEventByCategoryId(categoryId.get());
    }
    // Category name is present
    else if (categoryName.isPresent()) {
      log.info("Retrieving all events by category Name= {}", categoryName);
      return eventService.retrieveAllEventByCategory(categoryName.get());
    }
    // If the query parameter is either category_id or category_name this method
    // already calls the service layer method on the above lines based on the parameter, 
    // otherwise throw ResponseStatusException
    // If query parameter has invalid name, the size will be greater than 0.
    else if (request.getParameterMap().size() > 0) {
      log.warn("Invalid query parameter!!! Only allowed category_id or category_name.");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Invalid query parameter!!! Only allowed category_id or category_name.");
    }

    // If no query parameter provided, All event detail is returned.
    log.info("Retrieving all event with associated categories and details");
    return eventService.retrieveAllEvent();
  }


  @PostMapping("/{eventId}/venues/{venueId}/event-details")
  @ResponseStatus(code = HttpStatus.CREATED)
  public EventDetailData saveEventDetail(@PathVariable Long eventId, @PathVariable Long venueId,
      @RequestBody EventDetailData eventDetailData) {
    log.info("Creating event detail {} for event with ID={} and venue with ID={}", eventDetailData,
        eventId, venueId);
    return eventDetailService.saveEventDetail(eventId, venueId, eventDetailData);
  }


  @GetMapping("/{eventId}/event-details")
  public List<EventDetailResponse> retrieveAllEventDetailByEventId(@PathVariable Long eventId) {
    log.info("Retrieving event detail by event with ID={} ", eventId);
    return eventDetailService.retrieveAllEventDetailByEventId(eventId);
  }


}
