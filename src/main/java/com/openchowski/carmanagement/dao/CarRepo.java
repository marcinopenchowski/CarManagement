package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepo extends JpaRepository<Car, Integer> {

    @Query("select c from Car c where c.make like %?1% or c.model like %?1%")
    List<Car> searchCar(String searchName);

    @Query("select c from Car c where c.status like 'available' ")
    List<Car> showAvailable();

    @Query("select c from Car c where c.status like 'unavailable' ")
    List<Car> showUnavailable();

    // add converted int value (ID and YEAR)

}
