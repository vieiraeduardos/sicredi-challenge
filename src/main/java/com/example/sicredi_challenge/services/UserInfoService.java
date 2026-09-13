package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.clients.UserInfoClient;
import com.example.sicredi_challenge.entities.dtos.UserInfoResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoClient userInfoClient;

    public void validateAssociateCanVote(String cpf) {
        try {
            UserInfoResponse response = userInfoClient.checkCpfStatus(cpf);
            if (response == null || !response.isAbleToVote()) {
                throw new RuntimeException("Associado não está habilitado para votar (UNABLE_TO_VOTE).");
            }
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("CPF do associado é inválido ou não foi encontrado.");
        } catch (Exception e) {
            if (e instanceof RuntimeException && !(e instanceof FeignException)) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Serviço de verificação de CPF indisponível ou com erro. Voto não permitido.", e);
        }
    }
}