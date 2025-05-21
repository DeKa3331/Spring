package com.umcsuser.car_rent.service;

import com.umcsuser.car_rent.models.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalService {

    boolean isVehicleRented(String vehicleId);

    Optional<Rental> findActiveRentalByVehicleId(String vehicleId);

    Rental rent(String vehicleId, String userId);

    boolean returnRental(String vehicleId, String userId);

    List<Rental> findAll();


}