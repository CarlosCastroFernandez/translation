package translation_api.translation.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import translation_api.translation.util.JWTUtil;

import java.io.IOException;

/**
 * Clase personalizada de la cadena de filtros de seguridad para gestionar el token
 */
public class JWTFilterValidator extends OncePerRequestFilter {
    private JWTUtil jwtUtil;
  public JWTFilterValidator(JWTUtil jwtUtil){
      this.jwtUtil=jwtUtil;
  }

    /**
     *
     * @param request cuerpo de la petición
     * @param response respuesta
     * @param filterChain o cadena de filtros
     * @throws ServletException
     * @throws IOException
     * en este metodo que heredamos es donde gestionaos la validación de nuestro token en peticiones que tengan como autorización Authorization Bearer
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String token=request.getHeader(HttpHeaders.AUTHORIZATION);

        if(token!=null&&token.startsWith("Bearer")){
            token= token.substring(token.indexOf("Bearer ")+"Bearer ".length());
            DecodedJWT validateToken=jwtUtil.validateToken(token);
            String username=jwtUtil.extractUserName(validateToken);
            SecurityContext context= SecurityContextHolder.getContext();
            Authentication auth=new UsernamePasswordAuthenticationToken(username,null,null);
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request,response);
    }
}
