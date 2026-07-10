package PaoloF16.u5w2d5.services;

import PaoloF16.u5w2d5.entities.Employee;
import PaoloF16.u5w2d5.exceptions.BadRequestException;
import PaoloF16.u5w2d5.exceptions.NotFoundException;
import PaoloF16.u5w2d5.payloads.NewEmployeeDTO;
import PaoloF16.u5w2d5.repositories.EmployeesRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class EmployeesService {

    private static final Logger log = LoggerFactory.getLogger(EmployeesService.class);
    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private Cloudinary imgUploader;

    public Employee save(NewEmployeeDTO payload) {

        this.employeesRepository.findByEmail(payload.email()).ifPresent(employee -> {
            throw new BadRequestException("This email * " + employee.getEmail() + " * is already in use.");
        });

        this.employeesRepository.findByUsername(payload.username()).ifPresent(employee -> {
            throw new BadRequestException("This username * " + employee.getUsername() + " * is already in use.");
        });

        Employee newEmployee = new Employee(payload.firstName(), payload.lastName(), payload.email(), payload.username());
        newEmployee.setProfileImage("https://ui-avatars.com/api/?name=" + payload.firstName() + "+" + payload.lastName());
        this.employeesRepository.save(newEmployee);
        return newEmployee;
    }

    public List<Employee> findAll() {
        return this.employeesRepository.findAll();
    }

    public Employee findById(long employeeId) {
        return this.employeesRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public Employee findByIdAndUpdate(long employeeId, NewEmployeeDTO payload) {

        Employee found = this.findById(employeeId);

        if (!found.getEmail().equals(payload.email())) {
            this.employeesRepository.findByEmail(payload.email()).ifPresent(employee -> {
                throw new BadRequestException("The email * " + employee.getEmail() + " * is already in use!");
            });
        }

        if (!found.getUsername().equals(payload.username())) {
            this.employeesRepository.findByUsername(payload.username()).ifPresent(employee -> {
                throw new BadRequestException("The username * " + employee.getUsername() + " * is already in use!");
            });
        }

        found.setFirstName(payload.firstName());
        found.setLastName(payload.lastName());
        found.setEmail(payload.email());
        found.setUsername(payload.username());
        found.setProfileImage("https://ui-avatars.com/api/?name=" + payload.firstName() + "+" + payload.lastName());

        Employee editedEmployee = this.employeesRepository.save(found);

        log.info("The employee with id " + found.getId() + " was successfully updated");

        return editedEmployee;
    }

    public void findByIdAndDelete(long employeeId) {
        Employee found = this.findById(employeeId);
        this.employeesRepository.delete(found);
    }

    public String uploadAvatar(long employeeId, MultipartFile file) {
        Employee found = this.findById(employeeId);
        try {
            String url = (String) imgUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
            found.setProfileImage(url);
            this.employeesRepository.save(found);
            return url;
        } catch (IOException e) {
            throw new BadRequestException("There was a problem uploading this file.");
        }
    }

}