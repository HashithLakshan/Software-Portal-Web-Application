package Software.Portal.web.entity;

import Software.Portal.web.constant.CommonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Zoom_TimeSlots")
public class ZoomTimeSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long zoomTimeSlotId;

    @Column(name = "Slot_Open_Time")
    private String slotOpenTime;

    @Column(name = "Slot_Close_Time")
    private String slotCloseTime;

    @Enumerated
    private CommonStatus commonStatus;

}
