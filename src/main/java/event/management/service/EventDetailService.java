package event.management.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import event.management.controller.model.EventDetailData;
import event.management.controller.model.EventDetailResponse;
import event.management.dao.EventDetailDao;
import event.management.entity.Event;
import event.management.entity.EventDetail;
import event.management.entity.Venue;

@Service
public class EventDetailService {

  @Autowired
  private EventDetailDao eventDetailDao;

  @Autowired
  private EventService eventService;

  @Autowired
  private VenueService venueService;



  /**
   * A service layer method that save or update an event detail.
   * 
   * @param eventId Long
   * @param venueId Long
   * @param eventDetailData EventDetailData
   * @return created or modified EventDetailData object
   */
  public EventDetailData saveEventDetail(Long eventId, Long venueId,
      EventDetailData eventDetailData) {

    // Retrieving event
    Event event = eventService.findEventById(eventId);
    // Retrieving venu
    Venue venue = venueService.findVenueById(venueId);

    Long eventDetailId = eventDetailData.getEventDetailId();

    EventDetail eventDetail = findOrCreateEventDetail(eventId, venueId, eventDetailId);

    copyEventDetailFields(eventDetail, eventDetailData);

    // Setting relationships with event
    eventDetail.setEvent(event);
    event.getEventDetails().add(eventDetail);

    // Setting relationships with venue
    eventDetail.setVenue(venue);
    venue.getEventDetails().add(eventDetail);

    EventDetail dbEventDetail = eventDetailDao.save(eventDetail);
    return new EventDetailData(dbEventDetail);
  }


  /**
   * A convenient method for setting event detail object fields.
   * 
   * @param eventDetail EventDetail
   * @param eventDetailData EventDetailData
   */
  private void copyEventDetailFields(EventDetail eventDetail, EventDetailData eventDetailData) {
    eventDetail.setEventDetailId(eventDetailData.getEventDetailId());
    eventDetail.setDescription(eventDetailData.getDescription());
    eventDetail.setDate(eventDetailData.getDate());
    eventDetail.setStartTime(eventDetailData.getStartTime());
    eventDetail.setEndTime(eventDetailData.getEndTime());
    eventDetail.setIsFree(eventDetailData.getIsFree());
    eventDetail.setAvailability(eventDetailData.getAvailability());

  }


  /**
   * A method that accepts event detail id and returns event detail object associated with a give Id
   * if event detail Id is not null, otherwise creating new event object.
   * 
   * @param eventId Long
   * @param venueId Long
   * @param eventDetailId Long
   * @return EventDetal object
   */
  private EventDetail findOrCreateEventDetail(Long eventId, Long venueId, Long eventDetailId) {
    // If eventDetailId is null
    if (Objects.isNull(eventDetailId)) {
      // Create new event detail
      return new EventDetail();
    } else {
      // If not, find event detail
      return findEventDetailById(eventId, venueId, eventDetailId);
    }
  }


  /**
   * A method finds event detail from a given event detail Id.
   * 
   * @param eventId Long
   * @param venueId Long
   * @param eventDetailId Long
   * @return EventDetal object
   * @throws IllegalArgumentException if event Id or/and venue Id is/are not associated with
   *         eventDetailId.
   */
  private EventDetail findEventDetailById(Long eventId, Long venueId, Long eventDetailId) {
    // Retrieving event detail
    EventDetail eventDetail = findEventDetailById(eventDetailId);

    if (Objects.isNull(eventId) || Objects.isNull(venueId)) {
      throw new IllegalArgumentException("Event Id or/and Venue Id can not be null");
    }

    // Checking event Id and venue Id coming from controller and getting form the DAO by event
    // detail Id are the same.

    // If they are the same, we found event detail
    if (eventDetail.getEvent().getEventId() == eventId
        && eventDetail.getVenue().getVenueId() == venueId) {

      return eventDetail;

    }
    // Both are not the same, which means event Id and venue Id are not associated with event detail
    // Id.
    else if (eventDetail.getEvent().getEventId() != eventId
        && eventDetail.getVenue().getVenueId() != venueId) {

      throw new IllegalArgumentException("EventDetail with ID=" + eventDetailId
          + " is not associated with event ID=" + eventId + " and VENUE ID=" + venueId);

    }
    // event Id is not associated with event detail Id
    else if (eventDetail.getEvent().getEventId() != eventId) {
      throw new IllegalArgumentException(
          "EventDetail with ID=" + eventDetailId + " is not associated with EVENT ID=" + eventId);

    }
    // venue Id is not associated with event detail Id
    else {
      throw new IllegalArgumentException(
          "EventDetail with ID=" + eventDetailId + " is not associated with VENUE ID=" + venueId);
    }
  }


  /**
   * A method finds event detail from a given event detail Id.
   * 
   * @param eventDetailId
   * @return EventDetal object
   * @throws NoSuchElementException if event detail Id is not found.
   */
  private EventDetail findEventDetailById(Long eventDetailId) {
    EventDetail eventDetail =
        eventDetailDao.findById(eventDetailId).orElseThrow(() -> new NoSuchElementException(
            "Event Detail with ID=" + eventDetailId + " was not found."));

    return eventDetail;
  }


  /**
   * A method retrieves all event details.
   * 
   * @return list of EventDetailResponse.
   * @throws NoSuchElementException if no event details are found.
   */
  @Transactional(readOnly = true)
  public List<EventDetailResponse> retrieveAllEventDetail() {
    List<EventDetailResponse> eventDetails = new ArrayList<>();
    eventDetails = eventDetailDao.findAll().stream().map(EventDetailResponse::new).toList();

    if (eventDetails.isEmpty()) {
      throw new NoSuchElementException("No Event detail found.");
    }

    return eventDetails;
  }


  /**
   * A method retrieves all event details by date.
   * 
   * @param date LocalDate
   * @return list of EventDetailResponse.
   * @throws NoSuchElementException if no event details are found.
   */
  @Transactional(readOnly = true)
  public List<EventDetailResponse> retrieveAllEventDetailByDate(LocalDate date) {
    List<EventDetailResponse> eventDetailsByDate = new ArrayList<>();
    eventDetailsByDate =
        eventDetailDao.findAllByDate(date).stream().map(EventDetailResponse::new).toList();

    if (eventDetailsByDate.isEmpty()) {
      throw new NoSuchElementException("No Event detail found on " + date);
    }

    return eventDetailsByDate;
  }


  /**
   * A method retrieves all event details by event name.
   * 
   * @param eventName String
   * @return list of EventDetailResponse.
   * @throws NoSuchElementException if no event details are found.
   */
  @Transactional(readOnly = true)
  public List<EventDetailResponse> retrieveAllEventDetailByEventName(String eventName) {
    List<EventDetailResponse> eventDetailsByEventName = new ArrayList<>();
    eventDetailsByEventName = eventDetailDao.findAllByEventEventName(eventName).stream()
        .map(EventDetailResponse::new).toList();

    if (eventDetailsByEventName.isEmpty()) {
      throw new NoSuchElementException("No Event detail found by event name= " + eventName);
    }

    return eventDetailsByEventName;
  }


  /**
   * A method retrieves all event details by event Id.
   * 
   * @param eventId Long
   * @return list of EventDetailResponse.
   * @throws NoSuchElementException if no event details are found.
   */
  @Transactional(readOnly = true)
  public List<EventDetailResponse> retrieveAllEventDetailByEventId(Long eventId) {
    List<EventDetailResponse> eventDetailsByEventId = new ArrayList<>();
    eventDetailsByEventId = eventDetailDao.findAllByEventEventId(eventId).stream()
        .map(EventDetailResponse::new).toList();

    if (eventDetailsByEventId.isEmpty()) {
      throw new NoSuchElementException("No Event detail found by event ID= " + eventId);
    }

    return eventDetailsByEventId;
  }


  /**
   * A method retrieves all event details by venue name.
   * 
   * @param venueName String
   * @return list of EventDetailResponse.
   * @throws NoSuchElementException if no event details are found.
   */
  @Transactional(readOnly = true)
  public List<EventDetailResponse> retrieveAllEventDetailByVenueName(String venueName) {
    List<EventDetailResponse> eventDetailsByVenueName = new ArrayList<>();
    eventDetailsByVenueName = eventDetailDao.findAllByVenueVenueName(venueName).stream()
        .map(EventDetailResponse::new).toList();

    if (eventDetailsByVenueName.isEmpty()) {
      throw new NoSuchElementException("No Event detail found by venu name= " + venueName);
    }

    return eventDetailsByVenueName;
  }


  /**
   * A method retrieves all event details by venue Id.
   * 
   * @param venueId Long
   * @return list of EventDetailResponse.
   * @throws NoSuchElementException if no event details are found.
   */
  @Transactional(readOnly = true)
  public List<EventDetailResponse> retrieveAllEventDetailByVenueId(Long venueId) {
    List<EventDetailResponse> eventDetailsByVenueId = new ArrayList<>();
    eventDetailsByVenueId = eventDetailDao.findAllByVenueVenueId(venueId).stream()
        .map(EventDetailResponse::new).toList();

    if (eventDetailsByVenueId.isEmpty()) {
      throw new NoSuchElementException("No Event detail found by venue ID= " + venueId);
    }

    return eventDetailsByVenueId;
  }


  /**
   * A method deletes an event detail by a given event detail Id.
   * @param eventDetailId Long
   */
  @Transactional(readOnly = false)
  public void deleteEventDetailById(Long eventDetailId) {
    EventDetail eventDetail = findEventDetailById(eventDetailId);
    eventDetailDao.delete(eventDetail);
  }


  /**
   * A method updates a single event detail availability by a given event detail Id.
   * @param eventDetailId
   * @return EventDetailData object
   */
  @Transactional(readOnly = false)
  public EventDetailData updateEventDetailAvailabilityById(Long eventDetailId) {
    eventDetailDao.updateEventDetailAvailabilityById(false, eventDetailId);
    EventDetail eventDetail = findEventDetailById(eventDetailId);
    return new EventDetailData(eventDetail);
  }


  /**
   * A method updates event detail(s) availability by a specific date.
   * @param date LocalDate
   * @return update operation message Map type.
   */
  @Transactional(readOnly = false)
  public Map<String, String> updateEventDetailAvailabilityByDate(LocalDate date) {
    int numberOfEventDetail = 0;
    numberOfEventDetail = eventDetailDao.updateEventDetailAvailabilityByDate(false, date);

    if (numberOfEventDetail > 0) {
      return Map.of("message:",
          numberOfEventDetail + " event details become unavailable on " + date);
    } else {
      return Map.of("message:", "No event details previously available on " + date);
    }
  }
}
