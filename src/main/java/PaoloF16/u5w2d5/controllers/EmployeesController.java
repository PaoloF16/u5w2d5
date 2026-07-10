package PaoloF16.u5w2d5.controllers;

import PaoloF16.u5w2d5.entities.Employee;
import PaoloF16.u5w2d5.exceptions.ValidationException;
import PaoloF16.u5w2d5.payloads.NewEmployeeDTO;
import PaoloF16.u5w2d5.services.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    @Autowired
    private EmployeesService employeesService;

    @GetMapping
    public List<Employee> getEmployees() {
        return this.employeesService.findAll();
    }

    @GetMapping("/{employeeId}")
    public Employee getEmployeeById(@PathVariable long employeeId) {
        return this.employeesService.findById(employeeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody @Validated NewEmployeeDTO body, BindingResult validationResult) {
        if(validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.employeesService.save(body);
        }
    }

    @PutMapping("/{employeeId}")
    public Employee getEmployeeByIdAndUpdate(@RequestBody @Validated NewEmployeeDTO body, BindingResult validationResult, @PathVariable long employeeId) {
        if(validationResult.hasErrors()) {
            throw new ValidationException((validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList()));
        } else {
            return this.employeesService.findByIdAndUpdate(employeeId, body);
        }
    }

    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getEmployeeByIdAndDelete(@PathVariable long employeeId) {
        this.employeesService.findByIdAndDelete(employeeId);
    }

    @PatchMapping("/{employeeId}/profile_image")
    public String uploadImage(@PathVariable long employeeId, @RequestParam("profileImage") MultipartFile file) {
        return this.employeesService.uploadAvatar(employeeId, file);
    }
}
