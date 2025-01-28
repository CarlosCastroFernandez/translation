package translation_api.translation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;
import translation_api.translation.dto.AuthLoginRequest;
import translation_api.translation.dto.AuthResponse;
import translation_api.translation.models.Traduccion;
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
    @Value("${api.token}")
    private String apiToken;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserDetailsService user;

    @PostMapping("/userPermission")
    public ResponseEntity<AuthResponse>permisoToken(@RequestBody AuthLoginRequest authLoginRequest){
        return new ResponseEntity<>(authResponse(authLoginRequest.username(),authLoginRequest.password()),HttpStatus.OK);


    }

    @PostMapping("/ScannerTranslation")
    public ResponseEntity<HashMap<String,String>> traducirEsEn(@RequestBody Traduccion texto, @RequestParam String idiomaOrigen, @RequestParam String idiomaDestino){

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HashMap<String,String>respMapa=new HashMap<>();
        String respuesta="";
        String respuestaInfo="";
        HttpResponse<String> response=null;
        if (!texto.getText().isEmpty()||texto.getText()!=null){
            HashMap<String,String> mapaJson=new HashMap<>();
            mapaJson.put("inputs", texto.getText());
            ObjectMapper mapper=new ObjectMapper();
            try {
                do {
                    String formatJson = mapper.writeValueAsString(mapaJson);
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(formatJson));
                    response = getRespuesta(idiomaOrigen, idiomaDestino, formatJson);
                    respuestaInfo = response.body();
                    respuestaInfo = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(respuestaInfo);
                    System.out.println(respuestaInfo);
                }while (respuestaInfo.contains("estimated_time")|| respuestaInfo.contains("error"));


                if (response.statusCode()==200){
                    JsonNode nodo=mapper.readTree(response.body());
                     respuesta=nodo.get(0).get("translation_text").asText();
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        respMapa.put("traduccion",respuesta);

        return new ResponseEntity<>((respuesta.equals("")?new HashMap<>(Map.of("Bad Connection","Mala conexión espera 20 segundos")):respMapa), HttpStatus.OK);
    }

    private HttpResponse<String> getRespuesta(String idiomaOrigen, String idiomaDestino, String formatJson) throws IOException, InterruptedException {
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create("https://api-inference.huggingface.co/models/Helsinki-NLP/opus-mt-"+ idiomaOrigen +"-"+ idiomaDestino))
                .header("Authorization","Bearer "+apiToken)
                .header("Content-Type","Application/json")
                .POST(HttpRequest.BodyPublishers.ofString(formatJson, StandardCharsets.UTF_8))
                .build();

        HttpClient client=HttpClient.newHttpClient();
        HttpResponse<String> response=client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    private AuthResponse authResponse(String username, String password){

        UserDetails cargaUser=user.loadUserByUsername(username);
        Boolean codificadoPassword=passwordEncoder.matches(password, cargaUser.getPassword());
        String userGuardado=cargaUser.getUsername();
        String passwordGuardado=cargaUser.getPassword();

        if (userGuardado.equals(username)&&codificadoPassword){
            String token=jwtUtil.createToken(new UsernamePasswordAuthenticationToken(userGuardado,null,null));
            return new AuthResponse(username,token);

        }
        return null;


    }
}
