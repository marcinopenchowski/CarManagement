package com.openchowski.carmanagement.controller;

import com.openchowski.carmanagement.entity.Employee;
import com.openchowski.carmanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String showAll(Model model){

        List<Employee> employeeList = employeeService.findAll();

        model.addAttribute("employees", employeeList);

        return "list-employee";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model){

        Employee employee = new Employee();

        model.addAttribute("employee", employee);

        return "add-employee";
    }

    @GetMapping("/showUpdateForm")
    public String showAddForm(@RequestParam("employeeId") int id, Model model){

        Employee employee = employeeService.findById(id);

        model.addAttribute("employee", employee);

        return "add-employee";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee){

        employeeService.save(employee);

        return "redirect:/employees/list";
    }

    @PostMapping("/delete")
    public String deleteEmployee(@RequestParam(value = "idChecked", required = false) List<String> id){

        if(id != null) {
            for (String tempIdStr : id) {
                int tempId = Integer.parseInt(tempIdStr);
                employeeService.deleteById(tempId);
            }

        }

        return "redirect:/employees/list";
    }

}
