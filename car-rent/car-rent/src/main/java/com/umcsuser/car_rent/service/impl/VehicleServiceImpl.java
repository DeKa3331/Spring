package com.umcsuser.car_rent.service.impl;

import com.umcsuser.car_rent.models.Vehicle;
import com.umcsuser.car_rent.repository.RentalRepository;
import com.umcsuser.car_rent.repository.VehicleRepository;
import com.umcsuser.car_rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;


    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository,
                              RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll () {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return savedVehicle;
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(Vehicle::isActive)
                .filter(v -> rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty())
                .toList();
    }


    @Override
    public List<Vehicle> findRentedVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(Vehicle::isActive)
                .filter(v -> rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isPresent())
                .toList();
    }


    @Override
    public boolean isAvailable(String vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .filter(Vehicle::isActive)
                .filter(vehicle -> rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isEmpty())
                .isPresent();
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        vehicleRepository.findById(id).ifPresent(vehicle -> {
            vehicle.setActive(false);
            vehicleRepository.save(vehicle);
        });
    }



}