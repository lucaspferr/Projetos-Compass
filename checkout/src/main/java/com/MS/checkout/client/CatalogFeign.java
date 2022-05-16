package com.MS.checkout.client;

import com.MS.checkout.model.dto.ProductDTO;
import com.MS.checkout.model.dto.VariationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "catalog", url = "http://localhost:8081/")
public interface CatalogFeign {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/variations/feign/{variant_id}", produces = "application/json")
    VariationDTO getVariant(@PathVariable("variant_id") Long variant_id);

    @RequestMapping(method = RequestMethod.GET, value = "/v1/products/{product_id}", produces = "application/json")
    ProductDTO getProduct(@PathVariable("product_id") Long product_id);

}
