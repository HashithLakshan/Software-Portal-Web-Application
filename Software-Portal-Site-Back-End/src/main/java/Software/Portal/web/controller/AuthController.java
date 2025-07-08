package Software.Portal.web.controller;

import Software.Portal.web.Security.Services.UserDetailsImpl;
import Software.Portal.web.Security.jwt.JwtUtils;
import Software.Portal.web.constant.Roles;
import Software.Portal.web.dto.authDto.*;
import Software.Portal.web.entity.*;
import Software.Portal.web.repository.RollRepository;
import Software.Portal.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RollRepository rollRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminSignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUserName(signUpRequest.getUserName())) {
                return ResponseEntity.badRequest().body(
                        new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Error", "Username is already taken!", null)
                );
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(
                        new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Error", "Email is already in use!", null)
                );
            }

            // Create new user's account
            User user = new User();
            user.setUserId(signUpRequest.getUserId());
            user.setUserName(signUpRequest.getUserName());
            user.setEmail(signUpRequest.getEmail());
            user.setRequestStatus(signUpRequest.getRequestStatus());
            user.setCommonStatus(signUpRequest.getCommonStatus());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));

            Set<Roll> roles = new HashSet<>();
            Roll userRole = rollRepository.findByRollName(Roles.valueOf(signUpRequest.getRollDto().getRollName()))
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);

            user.setRoll(roles);
            userRepository.save(user);

            return new ResponseEntity<>(new MessageResponse(HttpStatus.OK.value(), "Success", "User Registered Success", user), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Error", "User Register UnSuccess", null), HttpStatus.BAD_REQUEST);
        }
    }
}