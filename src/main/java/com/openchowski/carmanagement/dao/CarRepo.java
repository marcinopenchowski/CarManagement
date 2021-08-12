package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.Car;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepo extends JpaRepository<Car, Integer> {

    @Query("select c from Car c where CONCAT(c.id, c.make, c.model, c.year) like %?1%")
    List<Car> searchCar(String searchName);

    @Query("select c from Car c where c.status like 'available'")
    List<Car> showAvailable(String sortField, String sortDirection);

    @Query("select c from Car c where c.status like 'unavailable'")
    List<Car> showUnavailable(String sortField, String sortDirection);



}
