package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e where e.firstName like %?1% OR e.lastName like %?1% OR e.email like %?1% OR e.jobTitle like %?1%")
    List<Employee> searchEmployee(String searchName);
    // OR e.id = ?1 OR e.phoneNumber = ?1


}


