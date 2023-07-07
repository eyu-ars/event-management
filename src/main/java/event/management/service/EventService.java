package event.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import event.management.controller.model.EventData;
import event.management.dao.CategoryDao;
import event.management.dao.EventDao;
import event.management.entity.Category;
import event.management.entity.Event;


@Service
public class EventService {

  @Autowired
  private EventDao eventDao;

  @Autowired
  private CategoryDao categoryDao;


  /**
   * A service layer method that save or update an event.
   * 
   * @param eventData EventData
   * @return created or modified EventData object
   * @throws UnsupportedOperationException if the list of categories is empty or does not found in
   *         database category table.
   */
  @Transactional(readOnly = false)
  public EventData saveEvent(EventData eventData) {

    Set<Category> categories = categoryDao.findAllByCategoryNameIn(eventData.getCategories());

    // Checking all categories is in already In DB by comparing parameter from controller
    // and fetching categories from dao.
    if (eventData.getCategories().size() != categories.size()) {
      throw new UnsupportedOperationException(
          "One or more list of category(ies) is not valid! Create first.");
    }
    // Checking categories are not empty
    else if (eventData.getCategories().isEmpty()) {
      throw new UnsupportedOperationException(
          "Category can not be empty. It should be a string list.");
    }

    Event event = findOrCreateEvent(eventData.getEventId());
    setEventFields(event, eventData);

    // Setting relationships
    for (Category category : categories) {
      category.getEvents().add(event);
      event.getCategories().add(category);
    }

    Event dbEvent = eventDao.save(event);
    return new EventData(dbEvent);
  }


  /**
   * A method that accepts event id and returns event object associated with a give Id if event Id
   * is not null, otherwise creating new event object.
   * 
   * @param eventId Long
   * @return Event object.
   */
  private Event findOrCreateEvent(Long eventId) {
    Event event;

    if (Objects.isNull(eventId)) {
      event = new Event();
    } else {
      event = findEventById(eventId);
    }

    return event;
  }

  /**
   * A method finds event from a given event Id.
   * 
   * @param eventId
   * @return event object.
   * @throws NoSuchElementException if event Id is not found.
   */
  Event findEventById(Long eventId) {
    return eventDao.findById(eventId).orElseThrow(
        () -> new NoSuchElementException("Event with ID=" + eventId + " does not exist."));
  }


  /**
   * A convenient method for setting event object fields.
   * 
   * @param event Event
   * @param eventData EventData
   */
  private void setEventFields(Event event, EventData eventData) {
    event.setEventId(eventData.getEventId());
    event.setEventName(eventData.getEventName());
    event.setFrequency(eventData.getFrequency());
    event.setDuration(eventData.getDuration());
    event.setEventNote(eventData.getEventNote());
  }


  /**
   * A method retrieves all events without event details.
   * 
   * @return list of EventData
   * @throws NoSuchElementException if no events are found.
   */
  @Transactional(readOnly = true)
  public List<EventData> retrieveAllEvent() {
    List<Event> events = eventDao.findAll();
    List<EventData> result = new ArrayList<>();

    for (Event event : events) {
      EventData eventData = new EventData(event);
      eventData.getEventDetails().clear();
      result.add(eventData);
    }

    if (result.isEmpty()) {
      throw new NoSuchElementException("No Events");
    }

    return result;
  }


  /**
   * A method retrieves all events without event details by category name.
   * 
   * @param categoryName String
   * @return list of EventData
   * @throws NoSuchElementException if no events are found.
   */
  @Transactional(readOnly = true)
  public List<EventData> retrieveAllEventByCategory(String categoryName) {

    List<Event> events = eventDao.findAll();
    List<EventData> result = new ArrayList<>();

    for (Event event : events) {
      EventData eventData = new EventData(event);
      if (eventData.getCategories().contains(categoryName)) {
        eventData.getEventDetails().clear();
        result.add(eventData);
      }
    }

    if (result.isEmpty()) {
      throw new NoSuchElementException("No Events associated with category name: " + categoryName);
    }

    return result;
  }


  /**
   * A method finds event from a given event Id.
   * 
   * @param eventId Long
   * @return EventData object
   */
  public EventData retrieveEventById(Long eventId) {
    return new EventData(findEventById(eventId));
  }


  /**
   * A method retrieves all events without event details by category Id.
   * 
   * @param categoryId Long
   * @return list of EventData
   * @throws NoSuchElementException if no events are found.
   */
  @Transactional(readOnly = true)
  public List<EventData> retrieveAllEventByCategoryId(Long categoryId) {

    List<Event> events = eventDao.findAllByCategoriesCategoryId(categoryId);
    List<EventData> result = new ArrayList<>();

    for (Event event : events) {
      EventData eventData = new EventData(event);
      eventData.getEventDetails().clear();
      result.add(eventData);
    }

    if (result.isEmpty()) {
      throw new NoSuchElementException("No Events associated with category ID= " + categoryId);
    }

    return result;
  }
}
