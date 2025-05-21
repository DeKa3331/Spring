package com.umcsuser.car_rent.models;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rental")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "rent_date", nullable = false)
    private LocalDateTime rentDate;
    @Column(name = "return_date")
    private LocalDateTime returnDate;
}