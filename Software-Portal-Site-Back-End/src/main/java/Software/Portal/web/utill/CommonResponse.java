package Software.Portal.web.utill;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonResponse {

    private boolean status = false;

    private List<String> errorMessages = new ArrayList<>();

    private List<Object> payload = null;

    private List<Object> pages = null;

    private String commonMessage;

   }