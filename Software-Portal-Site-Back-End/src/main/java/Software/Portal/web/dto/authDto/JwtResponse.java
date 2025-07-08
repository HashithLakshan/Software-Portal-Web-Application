package Software.Portal.web.dto.authDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String token;
    private String username;
    private String email;
    private List<String> roles;
}
