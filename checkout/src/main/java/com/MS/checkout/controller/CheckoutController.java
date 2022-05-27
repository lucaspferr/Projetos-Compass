package com.MS.checkout.controller;

import com.MS.checkout.model.Payment;
import com.MS.checkout.model.Purchase;
import com.MS.checkout.model.dto.PaymentDTO;
import com.MS.checkout.model.dto.PurchaseDTO;
import com.MS.checkout.rabbit.CartMessageSender;
import com.MS.checkout.service.PaymentService;
import com.MS.checkout.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class CheckoutController{

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CartMessageSender cartMessageSender;

    @PostMapping("/payments")
    public ResponseEntity<?> createPayment(@RequestBody @Valid PaymentDTO paymentDTO){
        Payment payment = paymentService.createPayment(paymentDTO);
        return new ResponseEntity(payment, HttpStatus.CREATED);
    }

    @GetMapping("/payments")
    public ResponseEntity<?> getAllPayments(){
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity(payments, HttpStatus.OK);
    }

    @GetMapping("/payments/{payment_id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long payment_id){
        Payment payment = paymentService.getByIdPayment(payment_id);
        return new ResponseEntity(payment, HttpStatus.OK);
    }

    @PutMapping("/payments/{payment_id}")
    public ResponseEntity<?> updatePayment(@PathVariable Long payment_id, @RequestBody @Valid PaymentDTO paymentDTO){
        Payment payment = paymentService.updatePayment(payment_id, paymentDTO);
        return new ResponseEntity(payment, HttpStatus.CREATED);
    }

    @DeleteMapping("/payments/{payment_id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long payment_id){
        paymentService.deletePayment(payment_id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/purchases")
    public ResponseEntity<?> createPurchase(@RequestBody @Valid PurchaseDTO purchaseDTO){
        Purchase purchase = purchaseService.createPurchase(purchaseDTO);
        return new ResponseEntity(purchase, HttpStatus.CREATED);
    }
}
