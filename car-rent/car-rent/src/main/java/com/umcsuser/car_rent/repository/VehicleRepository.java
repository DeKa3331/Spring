package com.umcsuser.car_rent.repository;

import com.umcsuser.car_rent.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    // Methods findAll(), findById(), save(), deleteById() fromJpaRepository.
    List<Vehicle> findByIsActiveTrue();
    Optional<Vehicle> findByIdAndIsActiveTrue(String id);
    List<Vehicle> findByIsActiveTrueAndIdNotIn(Set<String> rentedVehicleIds);
}
