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

public class JWTFilterValidator extends OncePerRequestFilter {
    private JWTUtil jwtUtil;
  public JWTFilterValidator(JWTUtil jwtUtil){
      this.jwtUtil=jwtUtil;
  }

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
