package com.openchowski.carmanagement.controller;

import com.openchowski.carmanagement.entity.Car;
import com.openchowski.carmanagement.entity.Employee;
import com.openchowski.carmanagement.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
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
            @RequestParam(value = "errors", required = false) String errors,
            @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir

    ){

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");


        List<Car> carList = carService.findAll(sortField, sortDir);

        model.addAttribute("cars", carList);

        model.addAttribute("errors", errors);

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

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/save")
    public String saveCar(@Valid @ModelAttribute("car") Car car,
        BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return "car/add-car";
        }else {
            carService.save(car);
            return "redirect:/cars/list";
        }
    }

    @PostMapping("/delete")
    public String deleteCar(
            @RequestParam(value = "idChecked", required = false) List<String> id
            ){
        try {
        if(id != null){
                for (String tempIdStr : id) {
                    int tempId = Integer.parseInt(tempIdStr);

                        carService.deleteById(tempId);

                }
        }
        }catch (Exception e){
            return "redirect:/cars/list?errors=carIsRented";
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
