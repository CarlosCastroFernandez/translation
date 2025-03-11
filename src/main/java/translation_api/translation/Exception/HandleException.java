package translation_api.translation.Exception;

import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * Clase personalizada para capturar errores de las validaciones
 */
@RestControllerAdvice
public class HandleException {
    /**
     * Metodo que es capaz de desplegarse cuando salta el error MrethodArgumentNotValidException y la mapeamos para que salga de una forma más entendible y sabiendo que campo hemos fallado
     * @param error
     * @return devuelve un mapa de todos los campos que están mal con un mensaje
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HashMap<String,String> exception(MethodArgumentNotValidException error){
        HashMap<String,String>mapa=new HashMap<>();
        error.getBindingResult().getFieldErrors().stream().forEach(errors->{
            mapa.put(errors.getField(),errors.getDefaultMessage());
        });

        return mapa;
    }


}
