package Software.Portal.web.controller;

import Software.Portal.web.dto.PaymentDto;
import Software.Portal.web.entity.Payment;
import Software.Portal.web.service.serviceImpl.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/v1")
@CrossOrigin
public class PaymentGateWayController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentGateWayController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/checkout")
    public ResponseEntity<PaymentDto> checkoutProducts(@RequestBody Payment payment) {
        PaymentDto paymentDto = paymentService.checkoutProducts(payment);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paymentDto);
    }
}
