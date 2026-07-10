package PaoloF16.u5w2d5.services;

import PaoloF16.u5w2d5.entities.Booking;
import PaoloF16.u5w2d5.entities.Employee;
import PaoloF16.u5w2d5.entities.Trip;
import PaoloF16.u5w2d5.enums.TripStatus;
import PaoloF16.u5w2d5.exceptions.BadRequestException;
import PaoloF16.u5w2d5.exceptions.NoBookingsFoundException;
import PaoloF16.u5w2d5.exceptions.NotFoundException;
import PaoloF16.u5w2d5.payloads.NewBookingDTO;
import PaoloF16.u5w2d5.repositories.BookingsRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingsService {

    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private TripsService tripsService;

    public Booking save(NewBookingDTO payload) {
        Employee employee = this.employeesService.findById(payload.employeeId());
        Trip trip = this.tripsService.findById(payload.tripId());

        if (this.bookingsRepository.existsByEmployeeAndTripDate(employee, trip.getDateOfTravel())) {
            throw new BadRequestException("This employee already has a booking for " + trip.getDateOfTravel());
        }

        if (trip.getStatus() == TripStatus.COMPLETED || trip.getDateOfTravel().isBefore(LocalDate.now())) {
            throw new BadRequestException("You cannot book past or completed trips.");
        }

        Booking newBooking = new Booking(payload.specialRequests(), employee, trip);
        return this.bookingsRepository.save(newBooking);
    }

    public List<Booking> findAll() {
        return this.bookingsRepository.findAll();
    }

    public Booking findById(long bookingId){
        return this.bookingsRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(bookingId));
    }

    public void findByIdAndDelete(long bookingId){
        Booking found = this.findById(bookingId);
        bookingsRepository.delete(found);
    }

    public List<Booking> findByEmployee(long employeeId) {
        Employee employee = employeesService.findById(employeeId);
        List<Booking> bookings = bookingsRepository.findByEmployee(employee);

        if (bookings.isEmpty()) {
            throw new NoBookingsFoundException("This employee has no bookings.");
        }

        return bookings;
    }

    //Paginazione
    public Page<Booking> getAll(int page, int size, String orderBy) {

        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));

        return this.bookingsRepository.findAll(pageable);
    }


    public List<Booking> findByTrip(long tripId) {
        Trip trip = tripsService.findById(tripId);
        return bookingsRepository.findByTrip(trip);
    }

    public Booking findByIdAndUpdate(long bookingId, NewBookingDTO payload) {
        Booking found = this.findById(bookingId);

        Employee employee = employeesService.findById(payload.employeeId());
        Trip trip = tripsService.findById(payload.tripId());

        found.setSpecialRequests(payload.specialRequests());
        found.setEmployee(employee);
        found.setTrip(trip);

        return bookingsRepository.save(found);
    }

    public Booking assignEmployeeToTrip(Long employeeId, Long tripId) {
        Employee employee = employeesService.findById(employeeId);
        Trip trip = tripsService.findById(tripId);

        if (trip.getStatus() == TripStatus.COMPLETED || trip.getDateOfTravel().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot assign employee to a completed or past trip.");
        }

        if (bookingsRepository.existsByEmployeeAndTrip(employee, trip)) {
            throw new BadRequestException("This employee is already booked for this trip.");
        }

        Booking booking = new Booking("Assigned by admin", employee, trip);
        return bookingsRepository.save(booking);
    }



}
