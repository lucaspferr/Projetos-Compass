package com.MS.history.rabbit;

import com.MS.history.model.DTO.CheckoutHistory;
import com.MS.history.service.HistoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class HistoryReciever {


    @Autowired
    private HistoryService historyService;

    @RabbitListener(queues = "purchase.history.queue")
    public void recieve(@Payload CheckoutHistory checkoutHistory){
        try {
            historyService.postHistory(checkoutHistory);
        }catch (Exception e){
            System.out.println("Erro de envio");
        }
    }
}
