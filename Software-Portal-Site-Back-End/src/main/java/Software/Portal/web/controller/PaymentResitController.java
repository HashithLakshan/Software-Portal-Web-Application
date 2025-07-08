package Software.Portal.web.controller;

import Software.Portal.web.entity.PaymentResit;
import Software.Portal.web.service.serviceImpl.PaymentResitService;
import Software.Portal.web.utill.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin
public class PaymentResitController {

    private  final PaymentResitService paymentResitService;

    @Autowired
    public PaymentResitController(PaymentResitService paymentResitService) {
        this.paymentResitService = paymentResitService;
    }

    @PostMapping("/upload")
    public CommonResponse uploadPdf(@RequestParam("file") MultipartFile file,@RequestParam
            ("systemProfileId") String systemProfileId,@RequestParam ("perchaseId") String perchaseId
            ,@RequestParam ("requestStatus") String requestStatus) {
       return paymentResitService.uploadPdf(file,systemProfileId,perchaseId,requestStatus);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateStatus")
    public CommonResponse updateStatus(@RequestParam String id,@RequestParam  String actionDo) {
        return paymentResitService.updateStatus(id,actionDo);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        Optional<PaymentResit> pdfFileOptional = paymentResitService.getPdf(id);

        if (pdfFileOptional.isPresent()) {
            PaymentResit paymentResit = pdfFileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                            paymentResit.getFileName() + "\"")
                    .body(paymentResit.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/filtered")
    public CommonResponse getFilteredPayment(
            @RequestParam(required = false) String commonStatus,
            @RequestParam(required = false) String requestStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return paymentResitService.getFilteredPayments(commonStatus, requestStatus, page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailId")
    public CommonResponse getDetailsInId(@RequestParam String commonStatus, @RequestParam String requestStatus,
                                         @RequestParam String id) {
        return paymentResitService.getDetailsInId(commonStatus,requestStatus,id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailPerchaseId")
    public CommonResponse getDetailsInPerchaseId(@RequestParam String commonStatus,
                                                 @RequestParam String requestStatus, @RequestParam String perchaseId) {
        return paymentResitService.getDetailsInPerchaseId(commonStatus,requestStatus,perchaseId);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getDetailsAllDateFilter")
    public CommonResponse getDetailsAllDate(@RequestParam (required = false) String fromDate,
                                            @RequestParam (required = false) String toDate,
                                            @RequestParam (required = false) String commonStatus,
                                            @RequestParam (required = false) String requestStatus,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        return paymentResitService.getDetailsAllDateFilter(fromDate,toDate,commonStatus,requestStatus,page,size);
    }
}
