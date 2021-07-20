package com.openchowski.carmanagement.service;

import com.openchowski.carmanagement.dao.EmployeeRepo;
import com.openchowski.carmanagement.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Employee> findAll(){

        List<Employee> employees = employeeRepo.findAll();

        return employees;
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



}
