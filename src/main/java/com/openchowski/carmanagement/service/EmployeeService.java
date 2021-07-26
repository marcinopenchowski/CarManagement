package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.EmployeeRepo;
import com.openchowski.carmanagement.entity.Employee;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<Employee> findAll(String sortField, String sortDirection){

        List<Employee> employeeList = employeeRepo.findAll(createSort(sortField, sortDirection));

        return employeeList;
    }

    public Employee findById(int id){

        Optional<Employee> result = employeeRepo.findById(id);

        Employee employee = null;

        if(result.isPresent()){
            employee = result.get();
        }
        else{
            throw new RuntimeException("Did not find employee(id - " + id + ")");
        }

        return employee;
    }

    public void save(Employee employee){

        employeeRepo.save(employee);

    }

    public void deleteById(int id){
        employeeRepo.deleteById(id);
    }


    public List<Employee> searchEmployee(String searchName) {

        List<Employee> employeeList = employeeRepo.searchEmployee(searchName);

        return employeeList;
    }

    private Sort createSort(String sortField, String sortDirection){

        Sort sort = sortDirection.equalsIgnoreCase(
                Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        return sort;
    }

}
