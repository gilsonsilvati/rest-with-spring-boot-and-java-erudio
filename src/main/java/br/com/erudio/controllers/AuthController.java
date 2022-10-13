package br.com.erudio.controllers;

import br.com.erudio.controllers.swagger.AuthSwagger;
import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import br.com.erudio.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthSwagger {

    @Autowired
    AuthService authService;

    @Override
    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return ResponseEntity.ok(authService.signIn(data));
    }

    @Override
    @PutMapping("/refresh/{username}")
    public ResponseEntity refreshToken(@PathVariable String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {
        if (checkIfParamsIsNull(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        }

        return ResponseEntity.ok(authService.refreshToken(username, refreshToken));
    }

    private static boolean checkIfParamsIsNull(final AccountCredentialsVO data) {
        return Objects.isNull(data) || Objects.isNull(data.getUsername()) || Objects.isNull(data.getPassword())
                || data.getUsername().isBlank() || data.getPassword().isBlank();
    }

    private static boolean checkIfParamsIsNull(String username, String refreshToken) {
        return Objects.isNull(username) || Objects.isNull(refreshToken)
                || username.isBlank() || refreshToken.isBlank();
    }
}
