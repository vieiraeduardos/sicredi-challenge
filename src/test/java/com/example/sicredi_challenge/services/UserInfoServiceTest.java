package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.clients.UserInfoClient;
import com.example.sicredi_challenge.entities.dtos.UserInfoResponse;
import com.example.sicredi_challenge.exceptions.BusinessException;
import com.example.sicredi_challenge.exceptions.ResourceNotFoundException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserInfoClient userInfoClient;

    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    void devePermitirAssociadoHabilitado() {
        when(userInfoClient.checkCpfStatus("123")).thenReturn(new UserInfoResponse("ABLE_TO_VOTE"));

        userInfoService.validateAssociateCanVote("123");

        verify(userInfoClient).checkCpfStatus("123");
    }

    @Test
    void deveRejeitarAssociadoNaoHabilitado() {
        when(userInfoClient.checkCpfStatus("123")).thenReturn(new UserInfoResponse("UNABLE_TO_VOTE"));

        assertThrows(BusinessException.class, () -> userInfoService.validateAssociateCanVote("123"));
    }

    @Test
    void deveRejeitarRespostaNulaDoServicoExterno() {
        when(userInfoClient.checkCpfStatus("123")).thenReturn(null);

        assertThrows(BusinessException.class, () -> userInfoService.validateAssociateCanVote("123"));
    }

    @Test
    void deveTraduzirCpfNaoEncontrado() {
        when(userInfoClient.checkCpfStatus("123"))
                .thenThrow(mock(FeignException.NotFound.class));

        assertThrows(ResourceNotFoundException.class,
                () -> userInfoService.validateAssociateCanVote("123"));
    }

    @Test
    void deveTraduzirFalhaDoServicoExterno() {
        when(userInfoClient.checkCpfStatus("123"))
                .thenThrow(mock(FeignException.class));

        assertThrows(BusinessException.class,
                () -> userInfoService.validateAssociateCanVote("123"));
    }
}