package com.openchowski.inventorymanagement.dao;

import com.openchowski.inventorymanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
}
