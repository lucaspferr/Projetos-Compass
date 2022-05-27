package com.MS.checkout.service;

import com.MS.checkout.client.CatalogFeign;
import com.MS.checkout.client.UserFeign;
import com.MS.checkout.model.Purchase;
import com.MS.checkout.model.dto.*;
import com.MS.checkout.rabbit.CartMessageSender;
import com.MS.checkout.rabbit.HistoryMessageSender;
import com.MS.checkout.repository.CartRepository;
import com.MS.checkout.repository.PurchaseRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CatalogFeign catalogFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private CartMessageSender cartMessageSender;
    @Autowired
    private HistoryMessageSender historyMessageSender;

    private ModelMapper modelMapper = new ModelMapper();

    public Purchase createPurchase(PurchaseDTO purchaseDTO) {

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        LocalDate ld = LocalDate.now();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        purchaseHistory.setPaymentMethod(paymentService.paymentToDTO(purchaseDTO.getPayment_id()));
        Purchase purchase = modelMapper.map(purchaseDTO, Purchase.class);
        purchaseHistory.setProducts(quantityChecker(purchase));
        purchaseHistory.setTotal(priceCalculator(purchaseHistory));
        purchaseHistory.setUser_id(purchaseDTO.getUser_id());
        purchaseHistory.setDate(String.valueOf(ld));

        isCustomerActive(purchaseDTO.getUser_id());
        isPaymentActive(purchaseDTO.getPayment_id());

        sendMessageToCatalog(purchase);
        sendMessageToHistory(purchaseHistory);
        cartRepository.saveAll(purchase.getCart());
        return purchaseRepository.save(purchase);
    }

    public void sendMessageToHistory(PurchaseHistory purchaseHistory){
        historyMessageSender.send(purchaseHistory);
    }

    public void isCustomerActive(Long user_id) {
        if(!(userFeign.getCustomer(user_id).getActive())) throw new IllegalStateException("User with the ID "+user_id+" isn't active.");
    }

    private void isPaymentActive(Long payment_id){
        if(!(paymentService.getByIdPayment(payment_id).getStatus())) throw new IllegalStateException("Payment method not active.");
    }

    public void sendMessageToCatalog(Purchase purchase) {
        purchase.getCart().forEach(cart -> {
            cartMessageSender.send(cart);
        });
    }


    private Double priceCalculator(PurchaseHistory purchaseHistory) {
        Double prePrice = 0.00;
        for (ProductHistory productHistory : purchaseHistory.getProducts()) {
            prePrice = productHistory.getPrice()*productHistory.getQuantity() + prePrice;
        }

        String price = String.format("%.2f",(prePrice/(100.00/(100.00 - purchaseHistory.getPaymentMethod().getDiscount()))));
        price = price.replaceAll(",",".");

        return Double.parseDouble(price);
    }

    private List<ProductHistory> quantityChecker(Purchase purchase) {
        List<ProductHistory> purchases = new ArrayList<>();
        purchase.getCart().forEach(cart -> {

            VariationDTO variationDTO = catalogFeign.getVariant(cart.getVariant_id());
            ProductDTO productDTO = catalogFeign.getProduct(variationDTO.getProduct_id());
            if(!(productDTO.getActive())) throw new IllegalStateException("Product not active.");
            if(variationDTO.getQuantity() == 0) throw new IllegalStateException("Product not in stock");
            if(variationDTO.getQuantity() < cart.getQuantity()) throw new IllegalStateException("Product with insufficient quantity.");

            ProductHistory productHistory = new ProductHistory(productDTO.getName(), productDTO.getDescription(), variationDTO.getColor(), variationDTO.getSize(), variationDTO.getPrice(), cart.getQuantity());
            purchases.add(productHistory);
        });
        return purchases;
    }
}
