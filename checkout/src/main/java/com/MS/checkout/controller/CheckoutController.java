package com.MS.checkout.controller;

import com.MS.checkout.model.Payment;
import com.MS.checkout.model.Purchase;
import com.MS.checkout.model.dto.PaymentDTO;
import com.MS.checkout.model.dto.PurchaseDTO;
import com.MS.checkout.rabbit.CartMessageSender;
import com.MS.checkout.service.CartService;
import com.MS.checkout.service.PaymentService;
import com.MS.checkout.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class CheckoutController{

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CartMessageSender cartMessageSender;

    @PostMapping("/payments")
    public Payment createPayment(@RequestBody @Valid PaymentDTO paymentDTO){
        return paymentService.createPayment(paymentDTO);
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments(){
        return paymentService.getAllPayments();
    }

    @GetMapping("/payments/{payment_id}")
    public Payment getPaymentById(@PathVariable Long payment_id){
        return paymentService.getByIdPayment(payment_id);
    }

    @PutMapping("/payments/{payment_id}")
    public Payment updatePayment(@PathVariable Long payment_id, @RequestBody @Valid PaymentDTO paymentDTO){
        return paymentService.updatePayment(payment_id, paymentDTO);
    }

    @PostMapping("/purchases")
    public Purchase createPurchase(@RequestBody @Valid PurchaseDTO purchaseDTO){
        return purchaseService.createPurchase(purchaseDTO);
    }
}
