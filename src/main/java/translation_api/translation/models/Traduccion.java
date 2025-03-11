package translation_api.translation.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * Clase que será mapeada en las peticiones para introducir el texto que se quiere traducir
 */
public class Traduccion {
    @NotBlank(message = "No puede estar en blanco ytampoco puede ser null")
    @NotEmpty(message = "No puede estar vacío el campo text")
    private String text;

}
