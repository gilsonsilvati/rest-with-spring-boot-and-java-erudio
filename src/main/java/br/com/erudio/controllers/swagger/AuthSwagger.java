package br.com.erudio.controllers.swagger;

import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication Endpoint")
public interface AuthSwagger {


    @Operation(summary = "Authenticates a user and returns a token")
    ResponseEntity signIn(AccountCredentialsVO data);
}
