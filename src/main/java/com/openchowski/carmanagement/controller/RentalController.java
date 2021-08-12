package com.openchowski.carmanagement.controller;

import com.openchowski.carmanagement.entity.Car;
import com.openchowski.carmanagement.entity.Employee;
import com.openchowski.carmanagement.entity.Rental;
import com.openchowski.carmanagement.exporter.RentalExcelExporter;
import com.openchowski.carmanagement.service.CarService;
import com.openchowski.carmanagement.service.EmployeeService;
import com.openchowski.carmanagement.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("rentals")
public class RentalController {

    RentalService rentalService;
    CarService carService;
    EmployeeService employeeService;

    @Autowired
    public RentalController(RentalService rentalService, CarService carService, EmployeeService employeeService) {
        this.rentalService = rentalService;
        this.carService = carService;
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String showList(
            Model model,
            @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
            ){
        // create rentalList with sorting

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        List<Rental> rentalList = rentalService.findAll(sortField, sortDir);

        model.addAttribute("rentals", rentalList);

        return "/rental/list-rental";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(
            Model model,
            @RequestParam(value = "carSortField", required = false, defaultValue = "id") String carSortField,
            @RequestParam(value = "carSortDir", required = false, defaultValue = "asc") String carSortDir,
            @RequestParam(value = "employeeSortField", required = false, defaultValue = "id") String employeeSortField,
            @RequestParam(value = "employeeSortDir", required = false, defaultValue = "asc") String employeeSortDir
    ){

        Rental rental = new Rental();

        List<Car> carList = carService.showAvailable(carSortField, carSortDir);

        List<Employee> employeeList = employeeService.findAll(employeeSortField, employeeSortDir);


        model.addAttribute("reverseCarSortDir", carSortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("reverseEmployeeSortDir", employeeSortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("rental", rental);
        model.addAttribute("cars", carList);
        model.addAttribute("employees", employeeList);

        return "/rental/add-rental";
    }


    @GetMapping("/showUpdateForm")
    public String showUpdateForm(
            @RequestParam(value = "carSortField", required = false, defaultValue = "id") String carSortField,
            @RequestParam(value = "carSortDir", required = false, defaultValue = "asc") String carSortDir,
            @RequestParam(value = "employeeSortField", required = false, defaultValue = "id") String employeeSortField,
            @RequestParam(value = "employeeSortDir", required = false, defaultValue = "asc") String employeeSortDir,
            @RequestParam("rentalId") int id,
            Model model){


        Rental rental = rentalService.findById(id);

        List<Car> carList = carService.showAvailable(carSortField, carSortDir);

        carList.add(rental.getCar());

        List<Employee> employeeList = employeeService.findAll(employeeSortField, employeeSortDir);

        model.addAttribute("reverseCarSortDir", carSortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("reverseEmployeeSortDir", employeeSortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("rental", rental);
        model.addAttribute("cars", carList);
        model.addAttribute("employees", employeeList);


        return "/rental/edit-rental";
    }


    @PostMapping("/save")
    public String saveRental(
            @RequestParam(value = "idCheckedCar", required = false) Integer idCar,
            @RequestParam(value = "idCheckedEmployee", required = false) Integer idEmployee,
            @ModelAttribute("rental") Rental rental
        ) {

        if (idCar == null || idEmployee == null) {

            return "redirect:/rentals/showAddForm?error=unselected";
        } else {
            rental.setPickUpDate(new Date());

            rental.setCar(carService.findById(idCar));
            rental.setEmployee(employeeService.findById(idEmployee));
            carService.findById(idCar).setStatus("unavailable");

            rentalService.save(rental);
            return "redirect:/rentals/list";
        }
    }

    @PostMapping("/edit")
    public String editRental(
            @RequestParam(value = "idCheckedCar", required = false) Integer idCar,
            @RequestParam(value = "idCheckedEmployee", required = false) Integer idEmployee,
            @RequestParam("rentalId") int idRental,
            @Valid @ModelAttribute("rental") Rental rental,
            BindingResult bindingResult
    ) {

        if(idCar == null || idEmployee == null){

            return "redirect:/rentals/showUpdateForm?rentalId=" + idRental + "&error=unselected";
        }else {
            Car previousCar = carService.findById(rentalService.findById(idRental).getCar().getId());

            previousCar.setStatus("available");


            rental.setCar(carService.findById(idCar));
            rental.setEmployee(employeeService.findById(idEmployee));
            carService.findById(idCar).setStatus("unavailable");

            if (bindingResult.hasFieldErrors("pickUpDate") && bindingResult.hasFieldErrors("returnDate")) {
                return "redirect:/rentals/showUpdateForm?rentalId=" + idRental + "&errorPickUpDate=wrongPickUpDate&errorReturnDate=wrongReturnDate";
            } else if (bindingResult.hasFieldErrors("returnDate")) {
                return "redirect:/rentals/showUpdateForm?rentalId=" + idRental + "&errorReturnDate=wrongReturnDate";
            } else if (bindingResult.hasFieldErrors("pickUpDate")) {
                return "redirect:/rentals/showUpdateForm?rentalId=" + idRental + "&errorPickUpDate=wrongPickUpDate";
            } else {
                rentalService.save(rental);
                return "redirect:/rentals/list";
            }
        }
    }

    @PostMapping("/delete")
    public String deleteRental(
            @RequestParam(value = "idChecked",
            required = false) List<String> id
    ){

        if(id != null){
            for(String tempIdStr : id){
                int tempId = Integer.parseInt(tempIdStr);

                Rental rental = rentalService.findById(tempId);

                Car car = carService.findById(rental.getCar().getId());

                car.setStatus("available");

                rentalService.delete(tempId);
            }
        }

        return "redirect:/rentals/list";
    }

    @GetMapping("/search")
    public String searchRental(
            @RequestParam(value = "searchName", required = false) String searchName,
            Model model){
        if(searchName.isBlank()){
            return "redirect:/rentals/list";
        }

        List<Rental> rentalList = rentalService.searchRental(searchName);
        model.addAttribute("rentals", rentalList);

        return "/rental/list-rental";
    }

    @GetMapping("/showReturnForm")
    public String showReturnForm(
            Model model,
            @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ){

        List<Car> carList = carService.showUnavailable(sortField, sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("cars", carList);


        return "/rental/remove-rental";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("carId") int idCar){

        Car car = carService.findById(idCar);

        car.setStatus("available");

        Rental rental = rentalService.findByIdCar(idCar);

        carService.save(car);

        rental.setReturnDate(new Date());

        rental.setStatus("completed");

        rentalService.save(rental);

        return "redirect:/rentals/list";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("text/xlsx");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = rentals_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Rental> rentalList = rentalService.findAll("id", "asc");

        RentalExcelExporter rentalExcelExporter = new RentalExcelExporter(rentalList);

        rentalExcelExporter.export(response);


    }

}
