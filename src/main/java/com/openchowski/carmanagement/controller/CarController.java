package com.openchowski.carmanagement.controller;

import com.openchowski.carmanagement.entity.Car;
import com.openchowski.carmanagement.entity.Employee;
import com.openchowski.carmanagement.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    // add CRUD endpoint

    @GetMapping("/list")
    public String showAll(
            Model model,
            @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "ASC") String sortDir
    ){

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        List<Car> carList = carService.findAll(sortField, sortDir);

        model.addAttribute("cars", carList);

        return "/car/list-car";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model){

        Car car = new Car();

        model.addAttribute("car", car);

        return "car/add-car";
    }

    @GetMapping("/showUpdateForm")
    public String showAddForm(Model model, @RequestParam("carId") int id){

        Car car = carService.findById(id);

        model.addAttribute("car", car);

        return "car/add-car";
    }

    @PostMapping("/save")
    public String saveCar(@ModelAttribute("car") Car car){
        carService.save(car);

        return "redirect:/cars/list";
    }

    @PostMapping("/delete")
    public String deleteCar(
            @RequestParam(value = "idChecked", required = false) List<String> id){

        if(id != null){
            for(String tempIdStr : id){
                int tempId = Integer.parseInt(tempIdStr);
                carService.deleteById(tempId);
            }
        }

        return "redirect:/cars/list";
    }

    @GetMapping("/search")
    public String searchCar(
            @RequestParam(value = "searchName", required = false) String searchName,
            Model model
            ){
        if(searchName.isBlank()){
            return "redirect:/cars/list";
        }

        List<Car> carList = carService.searchCar(searchName);
        model.addAttribute("cars", carList);

        return "car/list-car";
    }

}