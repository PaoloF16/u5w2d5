package PaoloF16.u5w2d5.controllers;

import PaoloF16.u5w2d5.entities.Trip;
import PaoloF16.u5w2d5.enums.TripStatus;
import PaoloF16.u5w2d5.exceptions.BadEnumException;
import PaoloF16.u5w2d5.exceptions.ValidationException;
import PaoloF16.u5w2d5.payloads.NewTripDTO;
import PaoloF16.u5w2d5.payloads.UpdateTripStatusDTO;
import PaoloF16.u5w2d5.services.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripsController {

    @Autowired
    private TripsService tripsService;

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripsService.findAll();
    }

    @GetMapping("/{tripId}")
    public Trip getTripById(@PathVariable long tripId) {
        return tripsService.findById(tripId);
    }

    @GetMapping("/by-date")
    public List<Trip> getTripsByDate(@RequestParam LocalDate date) {
        return tripsService.findByDateOfTravel(date);
    }

    @GetMapping("/by-destination")
    public List<Trip> getTripsByDestination(@RequestParam String destination) {
        return tripsService.findByDestination(destination);
    }

    @GetMapping("/status")
    public List<Trip> getTripsByStatus(@RequestParam String status) {
        try {
            TripStatus enumStatus = TripStatus.valueOf(status.toUpperCase());
            return tripsService.findByStatus(enumStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadEnumException("Invalid trip status: " + status + ". Allowed values: IN_PROGESS, COMPLETED.");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trip createTrip(@RequestBody @Validated NewTripDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return tripsService.save(body);
    }

    @PutMapping("/{tripId}")
    public Trip updateTrip(@PathVariable long tripId, @RequestBody @Validated NewTripDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return tripsService.findByIdAndUpdate(tripId, body);
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(@PathVariable long tripId) {
        tripsService.findByIdAndDelete(tripId);
    }

    @PatchMapping("/{tripId}/status")
    public Trip updateStatus(@PathVariable long tripId, @RequestBody @Validated UpdateTripStatusDTO body) {
        return tripsService.updateTripStatus(tripId, body.newStatus());
    }

}