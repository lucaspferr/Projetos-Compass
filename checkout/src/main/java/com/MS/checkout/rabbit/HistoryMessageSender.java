package com.MS.checkout.rabbit;

import com.MS.checkout.model.Cart;
import com.MS.checkout.model.dto.PurchaseHistory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "history.exchange";
    private static final String ROUTINGKEY_NAME = "purchase.history.routingkey";


    public void send(PurchaseHistory purchaseHistory){
        rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTINGKEY_NAME,purchaseHistory);
    }
}
