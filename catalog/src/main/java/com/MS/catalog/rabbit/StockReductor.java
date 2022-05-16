package com.MS.catalog.rabbit;

import com.MS.catalog.model.DTO.CartDTO;
import com.MS.catalog.model.Variation;
import com.MS.catalog.repository.VariationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class StockReductor {

    @Autowired
    VariationRepository variationRepository;

    @RabbitListener(queues = "purchase.cart.queue")
    public void receive(@Payload CartDTO cartDTO){
        Variation variation = variationRepository.findByVariant_id(cartDTO.getVariant_id());

        int posQuantity = variation.getQuantity() - cartDTO.getQuantity();
        variation.setQuantity(posQuantity);

        variationRepository.save(variation);

        System.out.println("Estoque reduzido.");
    }
}
