package PaoloF16.u5w2d5.services;

import PaoloF16.u5w2d5.entities.Trip;
import PaoloF16.u5w2d5.enums.TripStatus;
import PaoloF16.u5w2d5.exceptions.BadEnumException;
import PaoloF16.u5w2d5.exceptions.BadRequestException;
import PaoloF16.u5w2d5.exceptions.NotFoundException;
import PaoloF16.u5w2d5.payloads.NewTripDTO;
import PaoloF16.u5w2d5.repositories.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TripsService {

    @Autowired
    private TripsRepository tripsRepository;

    public Trip save(NewTripDTO payload) {

        if (tripsRepository.existsByDestinationAndDateOfTravelAndStatus(payload.destination(), payload.dateOfTravel(), TripStatus.IN_PROGRESS)) {
            throw new BadRequestException("A trip to " + payload.destination() + " on " + payload.dateOfTravel() + " is already in progress.");
        }

        Trip newTrip = new Trip(payload.destination(), payload.dateOfTravel(), payload.status());
        return this.tripsRepository.save(newTrip);
    }

    public List<Trip> findAll() {
        return this.tripsRepository.findAll();
    }

    public Trip findById(long tripId) {
        return this.tripsRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException(tripId));
    }

    public Trip findByIdAndUpdate(long tripId, NewTripDTO payload) {
        Trip found = this.findById(tripId);
        found.setDestination(payload.destination());
        found.setDateOfTravel(payload.dateOfTravel());
        found.setStatus(payload.status());
        return this.tripsRepository.save(found);
    }

    public void findByIdAndDelete(long tripId) {
        Trip found = this.findById(tripId);
        tripsRepository.delete(found);
    }

    public Trip updateTripStatus(long tripId, TripStatus newStatus) {
        if (newStatus == null) {
            throw new BadEnumException("Status cannot be null. Accepted values: IN_PROGRESS, COMPLETED.");
        }

        Trip found = this.findById(tripId);
        found.setStatus(newStatus);
        return tripsRepository.save(found);
    }

    //Paginazione
    public Page<Trip> getAll(int page, int size, String orderBy) {
        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));

        return this.tripsRepository.findAll(pageable);
    }

    public List<Trip> findByStatus(TripStatus status) {
        return tripsRepository.findByStatus(status);
    }

    public List<Trip> findByDateOfTravel(LocalDate date) {
        return tripsRepository.findByDateOfTravel(date);
    }

    public List<Trip> findByDestination(String destination) {
        return tripsRepository.findByDestination(destination);
    }


}