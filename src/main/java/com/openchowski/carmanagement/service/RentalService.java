package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.RentalRepo;
import com.openchowski.carmanagement.entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    RentalRepo rentalRepo;

    @Autowired
    public RentalService(RentalRepo rentalRepo) {
        this.rentalRepo = rentalRepo;
    }


    public List<Rental> findAll(String sortField, String sortDir){

        List<Rental> rentalList = rentalRepo.findAll(createSort(sortField, sortDir));

        return rentalList;
    }

    public Rental findById(int id){

        Optional<Rental> result = rentalRepo.findById(id);

        Rental rental = null;

        if(result.isPresent()){
            rental = result.get();
        }else{
            throw new RuntimeException("Did not find rental(id - " + id + ")");
        }

        return rental;
    }

    public void save(Rental rental) {
        rentalRepo.save(rental);
    }

    public void delete(int id){
        rentalRepo.deleteById(id);
    }

    public List<Rental> searchRental(String searchName) {

        List<Rental> rentalList = rentalRepo.searchRental(searchName);

        return rentalList;
    }

    public Rental findByIdCar(int idCar) {

        Rental rental = rentalRepo.findByIdCar(idCar);

        return rental;
    }

    private Sort createSort(String sortField, String sortDirection){

        Sort sort = sortDirection.equalsIgnoreCase(
                Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        return sort;
    }
}
