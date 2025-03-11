package translation_api.translation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import translation_api.translation.dto.AuthLoginRequest;
import translation_api.translation.dto.AuthResponse;
import translation_api.translation.models.Traduccion;
import translation_api.translation.service.ServiceTranslation;
import translation_api.translation.util.JWTUtil;

import java.io.IOException;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/translation")
/**
 * Clase Controladora de peticiones
 */
public class ControllerTranslation {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserDetailsService user;
    @Autowired
    private ServiceTranslation translationService;

    @PostMapping("/userPermission")
    public ResponseEntity<AuthResponse>permisoToken(@RequestBody AuthLoginRequest authLoginRequest){
        return new ResponseEntity<>(translationService.tokenResponse(authLoginRequest.username(),authLoginRequest.password()),HttpStatus.OK);


    }

    @Operation(
            summary = "Traducción de dos idiomas diferentes",
            description = "se traducen dos idiomas teniendo en cuenta el idioma de origen y el idioma destino\n" +
                    "las posibles traducciones son muchas, pero en esta descripción solo vamos a mencionar las posibles traducciones más importantes con el idioma español.\n" +
                    "El idioma español se entiende por las sigla (es) que luego es lo que hay que poner en idiomaOrigen:" +
                    "\t- es-gl (Gallego),\t- es-aed (Avéstico, Indio antiguo)\n" +
                    "\t- es-ar (árabe), \t- es -bcl (Bikol Central)\n" +
                    "\t- es-fr (francés), \t- es-en (Inglés)\n" +
                    "\t- es-de (Alemán), " +
                    "\tes-it: (italiano), \t- es-pt (portugués).\n" +
                    "es-ru:  (ruso),"+
                    "es-ar:  (árabe), \tes-ja (japonés).\n" +
                    "es-ko:  (coreano), \tes-fi (finlandés)\n" +
                    "es-nl:  (neerlandés), \tes-sv (sueco)\n" +
                    "es-pl:  (polaco), \tes-tr (turco)\n" +
                    "es-el:  (griego), \tes-uk (ucraniano)\n" +
                    "es-hi:  (hindi), \tes-he (hebreo)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "El cuerpo del mensaje debe de llevar en formato json la palabra clave que es text y el valor el texto que queramos traducir,\n" +
                            "OJO! este valor no puede ser null o estar vacío"
            ),
            parameters ={
                    @Parameter(
                            name = "idiomaOrigen",
                            in = ParameterIn.PATH,
                            description = "Podemos poner aquí la sigla es"
                    ),
                    @Parameter(
                            name = "idiomaDestino",
                            in=ParameterIn.PATH,
                            description = "Podemos poner cualquier sigla del idioma destino y comoejemplo tenemos la descripcion de este endpoint"
                    )
            }


    )
    @PostMapping("/ScannerTranslation")
    public ResponseEntity<HashMap<String,String>> traducirEsEn(@RequestBody @Valid Traduccion texto, @RequestParam  String idiomaOrigen, @RequestParam String idiomaDestino){

      return translationService.translation(texto,idiomaOrigen,idiomaDestino);
    }




}
