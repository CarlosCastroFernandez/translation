package translation_api.translation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import translation_api.translation.filter.JWTFilterValidator;
import translation_api.translation.util.JWTUtil;

@Configuration
public class SecurityConfig {
    @Autowired
    private JWTUtil jwtUtil;
    @Value("${user.api.create.carlos}")
    private String user;
    @Value("${password.api.create.carlos}")
    private String password;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf->csrf.disable())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http->{
                    http.requestMatchers(HttpMethod.POST,"/translation/userPermission").permitAll();
                    http.requestMatchers(HttpMethod.POST,"/translation/ScannerTranslation").authenticated();
                    http.anyRequest().denyAll();

                })
                .addFilterBefore(new JWTFilterValidator(jwtUtil), UsernamePasswordAuthenticationFilter.class)

                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails usuario= User.builder()
                .username(this.user)
                .password(passwordEncoder().encode(this.password))
                .build();
        return new InMemoryUserDetailsManager(usuario);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
