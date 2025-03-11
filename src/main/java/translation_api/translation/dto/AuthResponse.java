package translation_api.translation.dto;

/**
 * DTO que se mapea dando la respuesta en la salida a la petición
 * @param username usuario
 * @param accessToken y token
 */
public record AuthResponse (String username,String accessToken) {
}
