package com.openchowski.carmanagement.dao;

import com.openchowski.carmanagement.entity.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e where CONCAT(e.id, e.email, e.firstName, e.jobTitle, e.lastName, e.phoneNumber) like %?1%")
    List<Employee> searchEmployee(String searchName);


}


