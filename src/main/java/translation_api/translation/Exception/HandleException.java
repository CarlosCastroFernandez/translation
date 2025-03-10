package translation_api.translation.Exception;

import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HashMap<String,String> exception(MethodArgumentNotValidException error){
        HashMap<String,String>mapa=new HashMap<>();
        error.getBindingResult().getFieldErrors().stream().forEach(errors->{
            mapa.put(errors.getField(),errors.getDefaultMessage());
        });

        return mapa;
    }


}
