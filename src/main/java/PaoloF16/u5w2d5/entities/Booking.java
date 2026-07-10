package PaoloF16.u5w2d5.entities;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "requested_on")
    @Setter(AccessLevel.NONE)
    private LocalDate requestedOn;

    @Column(name = "special_requests", nullable = false, length = 1000)
    private String specialRequests;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Booking(String specialRequests, Employee employee, Trip trip) {
        this.requestedOn = LocalDate.now();
        this.specialRequests = specialRequests;
        this.employee = employee;
        this.trip = trip;
    }
}