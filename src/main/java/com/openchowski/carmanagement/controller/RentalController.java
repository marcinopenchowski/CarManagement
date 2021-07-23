package com.openchowski.carmanagement.controller;

import com.openchowski.carmanagement.entity.Car;
import com.openchowski.carmanagement.entity.Employee;
import com.openchowski.carmanagement.entity.Rental;
import com.openchowski.carmanagement.service.CarService;
import com.openchowski.carmanagement.service.EmployeeService;
import com.openchowski.carmanagement.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "sortDir", required = false, defaultValue = "ASC") String sortDir
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
    public String showAddForm(Model model){

        Rental rental = new Rental();

        List<Car> carList = carService.showAvailable();

        List<Employee> employeeList = employeeService.findAll("id", "asc");

        model.addAttribute("rental", rental);
        model.addAttribute("cars", carList);
        model.addAttribute("employees", employeeList);

        return "/rental/add-rental";
    }


    @GetMapping("/showUpdateForm")
    public String showUpdateForm(
            @RequestParam("rentalId") int id,
            Model model){

        Rental rental = rentalService.findById(id);

        List<Car> carList = carService.showAvailable();

        carList.add(rental.getCar());

        List<Employee> employeeList = employeeService.findAll("id", "asc");

        model.addAttribute("rental", rental);
        model.addAttribute("cars", carList);
        model.addAttribute("employees", employeeList);


        return "/rental/edit-rental";
    }


    @PostMapping("/save")
    public String saveRental(
            @RequestParam("idCheckedCar") int idCar,
            @RequestParam("idCheckedEmployee") int idEmployee,
            @ModelAttribute("rental") Rental rental
        ) {

            rental.setPickUpDate(new Date());

            rental.setCar(carService.findById(idCar));
            rental.setEmployee(employeeService.findById(idEmployee));
            carService.findById(idCar).setStatus("unavailable");


            rentalService.save(rental);
            return "redirect:/rentals/list";
        }


    @PostMapping("/edit")
    public String editRental(
            @RequestParam("idCheckedCar") int idCar,
            @RequestParam("idCheckedEmployee") int idEmployee,
            @RequestParam("rentalId") int idRental,
            @ModelAttribute("rental") Rental rental
    ) {

        Car previousCar = carService.findById(rentalService.findById(idRental).getCar().getId());

        previousCar.setStatus("available");



        rental.setCar(carService.findById(idCar));
        rental.setEmployee(employeeService.findById(idEmployee));
        carService.findById(idCar).setStatus("unavailable");

        rentalService.save(rental);
        return "redirect:/rentals/list";
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
    public String showReturnForm(Model model){

        List<Car> carList = carService.showUnavailable();

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

}