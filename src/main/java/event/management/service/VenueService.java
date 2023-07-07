package event.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import event.management.controller.model.VenueData;
import event.management.dao.EventDetailDao;
import event.management.dao.VenueDao;
import event.management.entity.EventDetail;
import event.management.entity.Venue;

@Service
public class VenueService {

  @Autowired
  private VenueDao venueDao;

  @Autowired
  private EventDetailDao eventDetailDao;


  /**
   * A service layer method that save or update a venue.
   * 
   * @param venueData VenueData
   * @return created or modified VenueData object
   */
  @Transactional(readOnly = false)
  public VenueData saveOrUpdateVenue(VenueData venueData) {
    Venue venue = findOrCreateVenue(venueData.getVenueId());

    copyVenueFields(venue, venueData);

    return new VenueData(venueDao.save(venue));
  }


  /**
   * A convenient method for setting venue object fields.
   * 
   * @param venue Venue
   * @param venueData VenueData
   */
  private void copyVenueFields(Venue venue, VenueData venueData) {
    venue.setVenueId(venueData.getVenueId());
    venue.setVenueName(venueData.getVenueName());
    venue.setVenueAddress(venueData.getVenueAddress());
    venue.setVenueCity(venueData.getVenueCity());
    venue.setVenueZip(venueData.getVenueZip());
    venue.setCapacity(venueData.getCapacity());
    venue.setVenueNote(venueData.getVenueNote());
  }


  /**
   * A method that accepts pet venue id and returns venue object associated with a give Id if venue
   * Id is not null, otherwise creating new venue object.
   * 
   * @param venueId Long
   * @return venue object.
   */
  private Venue findOrCreateVenue(Long venueId) {
    Venue venue;
    if (Objects.isNull(venueId)) {
      venue = new Venue();
    } else {
      venue = findVenueById(venueId);
    }

    return venue;
  }


  /**
   * A method finds venue detail from a given venue Id.
   * 
   * @param venueId
   * @return Venu object.
   * @throws NoSuchElementException if venue Id is not found.
   */
  Venue findVenueById(Long venueId) {
    return venueDao.findById(venueId).orElseThrow(
        () -> new NoSuchElementException("Venue with ID=" + venueId + " was not found."));
  }


  /**
   * A service layer method that retrieves all venues.
   * 
   * @return List of EventData
   * @throws NoSuchElementException if no venues are found.
   */
  @Transactional(readOnly = true)
  public List<VenueData> retrieveAllVenue() {
    List<Venue> venues = venueDao.findAll();
    List<VenueData> allVenue = new ArrayList<>();

    for (Venue venue : venues) {
      VenueData venueData = new VenueData(venue);
      venueData.getEventDetails().clear();
      allVenue.add(venueData);

    }

    if (allVenue.isEmpty()) {
      throw new NoSuchElementException("No Venues");
    }

    return allVenue;
  }


  /**
   * A method finds venue from a given venue Id.
   * 
   * @param venueId Long
   * @return VenueData object
   */
  @Transactional(readOnly = true)
  public VenueData retrieveVenueById(Long venueId) {
    return new VenueData(findVenueById(venueId));
  }


  /**
   * A method deletes a venue by a given venue Id if the venue is not associated with any event
   * details.
   * 
   * @param venueId Long
   * @return deletion message.
   * @throws UnsupportedOperationException if the venue is associated with any event detail.
   */
  @Transactional(readOnly = false)
  public Map<String, String> deleteVenueById(Long venueId) {

    List<EventDetail> associatedEventDetails = eventDetailDao.findAllByVenueVenueId(venueId);

    // Checking venue Id is associated to an event details. If it is, throw exception. we can not
    // perform the deletion.
    if (associatedEventDetails.size() > 0) {
      throw new UnsupportedOperationException(
          "Can not be deleted!!! Venue with ID=" + venueId + " is already associated with event");
    }

    // Otherwise perform deletion.
    Venue venue = findVenueById(venueId);
    venueDao.delete(venue);

    return Map.of("massage", "Deletion of venue with ID=" + venueId + " was successful.");
  }

}
