package com.openchowski.carmanagement.controller;

import com.openchowski.carmanagement.entity.Car;
import com.openchowski.carmanagement.entity.Employee;
import com.openchowski.carmanagement.exporter.CarExcelExporter;
import com.openchowski.carmanagement.exporter.EmployeeExcelExporter;
import com.openchowski.carmanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.management.monitor.StringMonitor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public String showAll(
            Model model,
            @RequestParam(value = "errors", required = false) String errors,
            @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir

    ){

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("errors", errors);


        List<Employee> employeeList = employeeService.findAll(sortField, sortDir);

        model.addAttribute("employees", employeeList);

        return "employee/list-employee";
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model){

        Employee employee = new Employee();

        model.addAttribute("employee", employee);

        return "employee/add-employee";
    }

    @GetMapping("/showUpdateForm")
    public String showAddForm(@RequestParam("employeeId") int id, Model model){

        Employee employee = employeeService.findById(id);

        model.addAttribute("employee", employee);

        return "employee/add-employee";
    }

    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult bindingResult
                               ){

        if(bindingResult.hasErrors()){
            return "employee/add-employee";
        }else {
            employeeService.save(employee);
            return "redirect:/employees/list";
        }
    }

    @PostMapping("/delete")
    public String deleteEmployee(@RequestParam(value = "idChecked", required = false) List<String> id){

        try {
            if (id != null) {
                for (String tempIdStr : id) {
                    int tempId = Integer.parseInt(tempIdStr);
                    employeeService.deleteById(tempId);
                }

            }
        }catch (Exception e){
            return "redirect:/employees/list?errors=carIsRented";
        }

        return "redirect:/employees/list";
    }

    @GetMapping("/search")
    public String searchEmployee(@RequestParam(value = "searchName", required = false) String searchName, Model model){

        if(searchName.isBlank()){
            return "redirect:/employees/list";
        }

        List<Employee> employeeList = employeeService.searchEmployee(searchName);
        model.addAttribute("employees", employeeList);

        return "employee/list-employee";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("text/xlsx");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = employees_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Employee> employeeList = employeeService.findAll("id", "asc");

        EmployeeExcelExporter excelExporter = new EmployeeExcelExporter(employeeList);

        excelExporter.export(response);

    }
}
