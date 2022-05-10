package ar.example.apichallenge.integration.api.rest.controller;

import ar.example.apichallenge.api.rest.dto.ErrorResponseDTO;
import ar.example.apichallenge.api.rest.dto.TransactionCreationRequestDTO;
import ar.example.apichallenge.api.rest.dto.TransactionCreationResponseDTO;
import ar.example.apichallenge.application.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionApiControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TransactionService service;

    @BeforeEach
    private void clearRepository() {
        ReflectionTestUtils.setField(service, "transactionList", new ArrayList<>());
    }

    @Test
    void create_withValidRequest_mustReturnResponse() {
        // Arrange
        TransactionCreationRequestDTO request = new TransactionCreationRequestDTO(20d, "type", null);
        HttpEntity<TransactionCreationRequestDTO> httpEntity = new HttpEntity<>(request);

        // Act
        ResponseEntity<TransactionCreationResponseDTO> response =
                testRestTemplate.exchange("/transactions/1", HttpMethod.PUT, httpEntity, TransactionCreationResponseDTO.class);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getStatus()).isEqualTo("OK");

    }

    @Test
    void create_withInvalidRequest_mustReturnErrorResponse() {
        // Arrange
        TransactionCreationRequestDTO request = new TransactionCreationRequestDTO(20d, null, null);
        HttpEntity<TransactionCreationRequestDTO> httpEntity = new HttpEntity<>(request);

        // Act
        ResponseEntity<ErrorResponseDTO> responseError =
                testRestTemplate.exchange("/transactions/1", HttpMethod.PUT, httpEntity, ErrorResponseDTO.class);

        // Assert
        assertThat(responseError).isNotNull();
        assertThat(responseError.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseError.getBody()).isNotNull();
        assertThat(responseError.getBody().getCode()).isEqualTo("error.invalid.argument");
        assertThat(responseError.getBody().getMessage()).isEqualTo("Invalid argument on the body sent");
    }

    @Test
    void create_withRepeatedRequestId_mustReturnErrorResponse() {
        // Arrange
        TransactionCreationRequestDTO request = new TransactionCreationRequestDTO(20d, "type", null);
        HttpEntity<TransactionCreationRequestDTO> httpEntity = new HttpEntity<>(request);
        ResponseEntity<TransactionCreationResponseDTO> responseOk =
                testRestTemplate.exchange("/transactions/1", HttpMethod.PUT, httpEntity, TransactionCreationResponseDTO.class);

        // Act
        ResponseEntity<ErrorResponseDTO> responseError =
                testRestTemplate.exchange("/transactions/1", HttpMethod.PUT, httpEntity, ErrorResponseDTO.class);

        // Assert
        assertThat(responseOk).isNotNull();
        assertThat(responseOk.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(responseError).isNotNull();
        assertThat(responseError.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseError.getBody()).isNotNull();
        assertThat(responseError.getBody().getCode()).isEqualTo("error.transaction.id.exists");
        assertThat(responseError.getBody().getMessage()).isEqualTo("Transaction with the given id already exists");

    }

}
