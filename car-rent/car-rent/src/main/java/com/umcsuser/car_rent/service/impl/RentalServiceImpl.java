package com.umcsuser.car_rent.service.impl;

import com.umcsuser.car_rent.models.Rental;
import com.umcsuser.car_rent.models.User;
import com.umcsuser.car_rent.models.Vehicle;
import com.umcsuser.car_rent.repository.RentalRepository;
import com.umcsuser.car_rent.repository.UserRepository;
import com.umcsuser.car_rent.repository.VehicleRepository;
import com.umcsuser.car_rent.service.RentalService;
import com.umcsuser.car_rent.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;




@Service
public class RentalServiceImpl implements RentalService {

    private VehicleService vehicleService;
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;
    private RentalRepository rentalRepository;

    @Autowired
    public RentalServiceImpl(VehicleService vehicleService, VehicleRepository vehicleRepository, UserRepository userRepository, RentalRepository rentalRepository) {
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
    }
    @Override
    @Transactional
    public Rental rent(String vehicleId, String userId) {
        if (!vehicleService.isAvailable(vehicleId)) {
            throw new IllegalStateException("Vehicle " + vehicleId + " is not available for rent.");
        }
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle consistency error. ID: " + vehicleId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found with ID: " + userId);
                });
        Rental newRental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicle(vehicle)
                .user(user)
                .rentDate(LocalDateTime.now())
                .returnDate(null)
                .build();
        Rental savedRental = rentalRepository.save(newRental);
        return savedRental;
    }

    @Override
    @Transactional
    public boolean returnRental(String vehicleId, String userId) {
        Optional<Rental> rentalOpt = rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if (rentalOpt.isPresent() && rentalOpt.get().getUser().getId().equals(userId)) {
            Rental rental = rentalOpt.get();
            rental.setReturnDate(LocalDateTime.now());
            rentalRepository.save(rental);
            return true;
        }
        return false;
    }


    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
