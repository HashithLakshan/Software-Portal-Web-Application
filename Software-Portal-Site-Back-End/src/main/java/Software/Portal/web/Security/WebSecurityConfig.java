package Software.Portal.web.Security;

import Software.Portal.web.Security.Services.UserDetailsServiceImpl;
import Software.Portal.web.Security.jwt.AuthEntryPointJwt;
import Software.Portal.web.Security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/Category/allCategories/{commonStatus}").permitAll()
                        .requestMatchers("/api/SystemProfile/getAllActiveProfilesPendingAndApprovedSuper").permitAll()
                        .requestMatchers("/api/systemProfileVideo/{systemProfileId}").permitAll()
                        .requestMatchers("/api/SystemProfileImages/ll/{systemProfilesId}").permitAll()
                        .requestMatchers("/api/SystemProfile/getAllSearching").permitAll()
                        .requestMatchers("/api/SystemProfile/getAllProfilesWithSliderBar").permitAll()
                        .requestMatchers("/api/SystemProfile/getAllSearching").permitAll()
                        .requestMatchers("/api/systemProfileChips/getAllOneProfileChips/{systemProfilesId}/{commonStatus}").permitAll()
                        .requestMatchers("/api/systemFeatures/getAllFeaturesinOneProfile/{systemProfilesId}/{commonStatus}").permitAll()
                        .requestMatchers("/api/SystemProfile/getByIdSystem/{systemProfileId}").permitAll()
                        .requestMatchers("/api/issuesMessages/save").permitAll()
                        .requestMatchers("/product/v1/**").permitAll()
                        .requestMatchers("/api/pdf/upload").permitAll()
                        .requestMatchers("/api/zoom/savePending").permitAll()
                        .requestMatchers("/api/timeSlots/getAll/{commonStatus}").permitAll()
                        .requestMatchers("/api/Category/getByIdCat/{categoryId}").permitAll()
                        .requestMatchers("/api/SystemProfile/getAllActiveProfilesPendingAndApprovedCategory").permitAll()
                        .requestMatchers("/api/subCategory/getAllSub/{commonStatus}").permitAll()




                                .requestMatchers("/api/Category/**").hasRole("ADMIN")
                                .requestMatchers("api/employee/**").hasRole("ADMIN")
                                .requestMatchers("/image/**").hasRole("ADMIN")
                                .requestMatchers("/product/v1/**").hasRole("ADMIN")
                                .requestMatchers("/api/pdf/**").hasRole("ADMIN")
                                .requestMatchers("/api/systemFeatures/**").hasRole("ADMIN")
                                .requestMatchers("/api/issuesMessages/**").hasRole("ADMIN")
                                .requestMatchers("/api/systemProfileChips/**").hasRole("ADMIN")
                                .requestMatchers("/api/zoom/**").hasRole("ADMIN")
                                .requestMatchers("/api/timeSlots/**").hasRole("ADMIN")
                                .requestMatchers("/api/answerMessages/**").hasRole("ADMIN")
                                .requestMatchers("/api/SystemProfile/**").hasRole("ADMIN")
                                .requestMatchers("/api/SystemProfileImages/**").hasRole("ADMIN")
                                .requestMatchers("/api/systemProfileVideo/**").hasRole("ADMIN")
                                .requestMatchers("/api/user/**").hasRole("ADMIN")

                                .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ GLOBAL CORS CONFIGURATION (REQUIRED FOR SECURITY)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
