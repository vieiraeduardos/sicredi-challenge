package com.example.sicredi_challenge.clients;

import com.example.sicredi_challenge.entities.dtos.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userInfoClient", url = "${app.sicredi-user-info.url:https://user-info.herokuapp.com}")
public interface UserInfoClient {

    @GetMapping("/users/{cpf}")
    UserInfoResponse checkCpfStatus(@PathVariable("cpf") String cpf);
}