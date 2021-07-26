package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.CarRepo;
import com.openchowski.carmanagement.entity.Car;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarService {

    CarRepo carRepo;

    @Autowired
    public CarService(CarRepo carRepo) {
        this.carRepo = carRepo;
    }

    public List<Car> findAll(String sortField, String sortDirection){

        List<Car> carList = carRepo.findAll(createSort(sortField, sortDirection));

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

    public List<Car> showAvailable(String sortField, String sortDirection) {

        List<Car> carList = carRepo.findAll(createSort(sortField, sortDirection));

        List<Car> tempCarList = new ArrayList<>();

        for(Car tempCar : carList){

            if("available".equals(tempCar.getStatus())){

                tempCarList.add(tempCar);
            }

        }

        return tempCarList;
    }

    public List<Car> showUnavailable(String sortField, String sortDirection) {
        List<Car> carList = carRepo.findAll(createSort(sortField, sortDirection));

        List<Car> tempCarList = new ArrayList<>();

        for(Car tempCar : carList){

            if("unavailable".equals(tempCar.getStatus())){

                tempCarList.add(tempCar);
            }

        }

        return tempCarList;
    }

    private Sort createSort(String sortField, String sortDirection){

        Sort sort = sortDirection.equalsIgnoreCase(
                Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        return sort;
    }



}
