package com.MS.history.client;

import com.MS.history.model.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "customer", url = "http://localhost:8083/")
public interface UserFeign {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/users/{idCustomer}", produces = "application/json")
    UserDTO getCustomer(@PathVariable("idCustomer")Long idCustomer);
}
