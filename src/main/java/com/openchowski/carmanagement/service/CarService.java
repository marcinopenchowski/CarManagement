package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.CarRepo;
import com.openchowski.carmanagement.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    CarRepo carRepo;

    @Autowired
    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }

    public List<Car> findAll(String sortField, String sortDirection){

        // add sorting
        Sort sort = sortDirection.equalsIgnoreCase(
                Sort.Direction.ASC.name())
                        ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending();

        List<Car> carList = carRepo.findAll(sort);

        return carList;
    }

    public Car findById(int id){

        Optional<Car> result = carRepo.findById(id);

        Car car = null;

        if(result.isPresent()){

            car = result.get();

        }else{
            throw new RuntimeException("Did not find car(id - " + id + ")");
        }

        return car;
    }

    public void save(Car car){

        carRepo.save(car);

    }

    public void deleteById(int id){

        carRepo.deleteById(id);

    }

    public List<Car> searchCar(String searchName){

        List<Car> carList = carRepo.searchCar(searchName);

        return carList;
    }

    public List<Car> showAvailable() {
        List<Car> carList = carRepo.showAvailable();

        return carList;
    }

    public List<Car> showUnavailable() {
        List<Car> carList = carRepo.showUnavailable();

        return carList;
    }



}
