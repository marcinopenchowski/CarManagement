package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentalRepo extends JpaRepository<Rental, Integer> {

    @Query("select r from Rental r where r.status like %?1%")
    List<Rental> searchRental(String searchName);
    // add searchin to Query


    @Query("select r from Rental r where r.car.id = ?1 and r.status like 'in progress'")
    Rental findByIdCar(int idCar);


}
