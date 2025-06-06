package com.umcsuser.car_rent.controller;

import com.umcsuser.car_rent.dto.RentalRequest;
import com.umcsuser.car_rent.models.Rental;
import com.umcsuser.car_rent.models.Vehicle;
import com.umcsuser.car_rent.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;



@RestController
@RequestMapping("/api/rentals")


public class RentalController {


    private RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest, Principal principal) {
        if (rentalRequest.vehicleId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            String username = principal.getName();
            Rental rental = rentalService.rent(rentalRequest.vehicleId, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        } catch (Exception e) {
            e.printStackTrace(); // TODO usunac jak znajde jak naprawic
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/return")
    public ResponseEntity<Void> returnVehicle(@RequestBody RentalRequest rentalRequest, Principal principal) {
        if (rentalRequest.vehicleId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            String username = principal.getName();
            boolean success = rentalService.returnRental(rentalRequest.vehicleId, username);
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.findAll();
    }

}
