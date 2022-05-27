package com.MS.checkout.service;

import com.MS.checkout.model.Payment;
import com.MS.checkout.model.dto.PaymentDTO;
import com.MS.checkout.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    ModelMapper modelMapper = new ModelMapper();

    public Payment createPayment(PaymentDTO paymentDTO){
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public Payment idChecker(Long payment_id){
        Payment payment = paymentRepository.findById(payment_id).orElseThrow(() -> new IllegalStateException("Payment method with the ID "+payment_id+" doesn't exist."));
        return payment;
    }

    public PaymentDTO paymentToDTO(Long payment_id){
        Payment payment = idChecker(payment_id);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public Payment getByIdPayment(Long payment_id){
        return idChecker(payment_id);
    }

    public void deletePayment(Long payment_id){
        Payment payment = idChecker(payment_id);
        paymentRepository.delete(payment);
    }

    @Transactional
    public Payment updatePayment(Long payment_id, PaymentDTO paymentDTO) {
        Payment payment = idChecker(payment_id);
        payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setPayment_id(payment_id);
        return paymentRepository.save(payment);
    }

}
