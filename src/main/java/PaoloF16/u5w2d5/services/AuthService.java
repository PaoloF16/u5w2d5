package PaoloF16.u5w2d5.services;

import PaoloF16.u5w2d5.entities.Employee;
import PaoloF16.u5w2d5.exceptions.UnauthorisedException;
import PaoloF16.u5w2d5.payloads.LoginDTO;
import PaoloF16.u5w2d5.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bCrypt;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        Employee found = this.employeesService.findByEmail(body.email());

        if(bCrypt.matches(body.password(), found.getPassword())) {
            String accessToken = jwtTools.createToken(found);
            return accessToken;
        } else {
            throw new UnauthorisedException("Wrong credentials!");
        }
    }

}
