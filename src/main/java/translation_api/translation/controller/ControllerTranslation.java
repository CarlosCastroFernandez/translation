package translation_api.translation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/translation")
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

    @PostMapping("/ScannerTranslation")
    public ResponseEntity<HashMap<String,String>> traducirEsEn(@RequestBody @Valid Traduccion texto, @RequestParam String idiomaOrigen, @RequestParam String idiomaDestino){

      return translationService.translation(texto,idiomaOrigen,idiomaDestino);
    }




}
