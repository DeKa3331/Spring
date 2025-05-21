package com.umcsuser.car_rent.controller;


import com.umcsuser.car_rent.models.Vehicle;
import com.umcsuser.car_rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    @GetMapping // Mapowanie GET na główny URL /api/vehicles
    public List<Vehicle> getAllVehicles() {
        return vehicleService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id) {

        return vehicleService.findById(id)
                .map(vehicle -> {
                    return ResponseEntity.ok(vehicle);
// 200 OK z obiektem Vehicle
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();//404
                });
    }


    @PostMapping("/add")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle savedVehicle = vehicleService.save(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteVehicle(@PathVariable String id) {
        vehicleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.findAvailableVehicles();
    }

    @GetMapping("/rented")
    public List<Vehicle> getRentedVehicles() {
        return vehicleService.findRentedVehicles();
    }

    @GetMapping("/active")
    public List<Vehicle> getActiveVehicles() {
        return vehicleService.findAllActive();
    }





//...
}

