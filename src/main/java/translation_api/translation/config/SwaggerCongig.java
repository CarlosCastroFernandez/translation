package translation_api.translation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
info = @Info(
        title = "TRADUCTOR PRO",
        description = "Traductor para gran cantidad de lenguajes" ,
        version = "V1",
        contact = @Contact(
                name = "Carlos Castro",
                email = "carlos.casfernan@gmail.com"


        )


),
        servers = {
        @Server(
                        url = "http://localhost:8080/",
                        description = "desarrollo"
                ),
        @Server(
                url = "https://translation-qj5s.onrender.com/",
                description = "pre-producción"
        )
        },
        security = @SecurityRequirement(
            name = "Security Token"
        )



)
@SecurityScheme(
        name = "Security Token",
        description = "Acces Token For my API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)

public class SwaggerCongig {
}
