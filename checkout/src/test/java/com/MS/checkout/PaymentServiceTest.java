package com.MS.checkout;

import com.MS.checkout.client.UserFeign;
import com.MS.checkout.model.Payment;
import com.MS.checkout.model.dto.CustomerDTO;
import com.MS.checkout.model.dto.PaymentDTO;
import com.MS.checkout.repository.PaymentRepository;
import com.MS.checkout.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserFeign userFeign;

    private Payment payment;
    private PaymentDTO paymentDTO;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        createPayments();
    }

    @Test
    void idCheckerOk(){
        when(paymentRepository.findById(1l)).thenReturn(Optional.of(payment));
        assertEquals(payment, paymentService.getByIdPayment(1l));
    }
    @Test
    void idCheckerFail(){
        assertThrows(IllegalStateException.class,() -> {paymentService.idChecker(1l);});
    }

    @Test
    void createPayment(){
        Payment newPayment = realMapper(paymentDTO);
        when(modelMapper.map(paymentDTO, Payment.class)).thenReturn(newPayment);
        when(paymentRepository.save(any())).thenReturn(newPayment);
        assertEquals(payment,paymentService.createPayment(paymentDTO));
    }


    private Payment realMapper(PaymentDTO paymentDTO){
        ModelMapper mapper = new ModelMapper();
        Payment newPayment = mapper.map(paymentDTO,Payment.class);
        newPayment.setPayment_id(1l);
        return newPayment;
    }

    private void createPayments(){
        payment = new Payment(PAYM_ID,PAYM_TYPE,PAYM_DISC,PAYM_STATUS);
        paymentDTO = new PaymentDTO(PAYM_TYPE,PAYM_DISC,PAYM_STATUS);
    }

    static final Long PAYM_ID = 1l;
    static final String PAYM_TYPE = "Test";
    static final Double PAYM_DISC = 10.00;
    static final Boolean PAYM_STATUS = true;
}
