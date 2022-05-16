package com.MS.checkout.rabbit;

import com.MS.checkout.model.Cart;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "cart.exchange";
    private static final String ROUTINGKEY_NAME = "purchase.cart.routingkey";


    public void send(Cart cart){
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTINGKEY_NAME,cart);
    }
}
