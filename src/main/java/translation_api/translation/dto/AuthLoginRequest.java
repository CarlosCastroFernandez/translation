package translation_api.translation.dto;

/**
 * Login donde hay que meter un username y una contraseña
 * @param username
 * @param password
 */
public record AuthLoginRequest (String username, String password) {
}
