package Software.Portal.web.dto.authDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private long status_code;
    private String message_status;
    private String message;
    private Object data;
}
