package ar.example.apichallenge.integration.api.rest.controller;

import ar.example.apichallenge.api.rest.dto.ErrorResponseDTO;
import ar.example.apichallenge.api.rest.dto.TransactionAmountSumResponseDTO;
import ar.example.apichallenge.api.rest.dto.TransactionCreationRequestDTO;
import ar.example.apichallenge.api.rest.dto.TransactionCreationResponseDTO;
import ar.example.apichallenge.application.service.TransactionService;
import ar.example.apichallenge.domain.model.TransactionBO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    void create_withRepeatedTransactionId_mustReturnErrorResponse() {
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

    @Test
    void create_withInvalidParentId_mustReturnErrorResponse() {
        // Arrange
        TransactionCreationRequestDTO request = new TransactionCreationRequestDTO(20d, "type", 16L);
        HttpEntity<TransactionCreationRequestDTO> httpEntity = new HttpEntity<>(request);

        // Act
        ResponseEntity<ErrorResponseDTO> responseError =
                testRestTemplate.exchange("/transactions/1", HttpMethod.PUT, httpEntity, ErrorResponseDTO.class);

        // Assert
        assertThat(responseError).isNotNull();
        assertThat(responseError.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseError.getBody()).isNotNull();
        assertThat(responseError.getBody().getCode()).isEqualTo("error.transaction.parent.not.exists");
        assertThat(responseError.getBody().getMessage()).isEqualTo("Invalid argument on the body sent");

    }

    @Test
    void findByType_withValidTypeAndTypeFound_mustReturnListOfTransactionIds() {
        // Arrange
        String searchArgument = "searchType";
        TransactionBO transaction1 = new TransactionBO(10L, 20.0d, searchArgument, null);
        TransactionBO transaction2 = new TransactionBO(20L, 20.0d, searchArgument, null);
        TransactionBO transaction3 = new TransactionBO(30L, 20.0d, "type3", 10L);
        TransactionBO transaction4 = new TransactionBO(40L, 20.0d, "type4", 30L);

        ReflectionTestUtils.setField(service, "transactionList",
                Arrays.asList(transaction1, transaction2, transaction3, transaction4));

        // Act
        ResponseEntity<List<Long>> response =
                testRestTemplate.exchange(MessageFormat.format("/transactions/types/{0}", searchArgument),
                        HttpMethod.GET, new HttpEntity<>(null), new ParameterizedTypeReference<>() {
                        });

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty().containsExactlyInAnyOrder(10L, 20L);

    }

    @Test
    void findByType_withValidTypeAndTypeNotFound_mustReturnEmptyList() {
        // Arrange
        String searchArgument = "searchType";
        TransactionBO transaction1 = new TransactionBO(1L, 20.0d, "type1", null);
        TransactionBO transaction2 = new TransactionBO(2L, 20.0d, "type2", null);

        ReflectionTestUtils.setField(service, "transactionList",
                Arrays.asList(transaction1, transaction2));

        // Act
        ResponseEntity<List<Long>> response =
                testRestTemplate.exchange(MessageFormat.format("/transactions/types/{0}", searchArgument),
                        HttpMethod.GET, new HttpEntity<>(null), new ParameterizedTypeReference<>() {
                        });

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();

    }

    @Test
    void sumAmounts_withValidTransactionIdAndParentFound_mustReturnSum() {
        // Arrange
        TransactionBO transaction1 = new TransactionBO(10L, 40.0d, "type1", null);
        TransactionBO transaction2 = new TransactionBO(20L, 30.0d, "type2", 10L);
        TransactionBO transaction3 = new TransactionBO(30L, 20.0d, "type3", 10L);
        TransactionBO transaction4 = new TransactionBO(40L, 10.0d, "type4", 30L);

        ReflectionTestUtils.setField(service, "transactionList",
                Arrays.asList(transaction1, transaction2, transaction3, transaction4));

        // Act
        ResponseEntity<TransactionAmountSumResponseDTO> response =
                testRestTemplate.exchange("/transactions/sum/20",
                        HttpMethod.GET, new HttpEntity<>(null), TransactionAmountSumResponseDTO.class);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSum()).isEqualTo(50.0d);
    }

    @Test
    void sumAmounts_withValidTransactionIdAndParentNotFound_mustReturnSum() {
        // Arrange
        TransactionBO transaction1 = new TransactionBO(10L, 40.0d, "type1", null);

        ReflectionTestUtils.setField(service, "transactionList",
                Collections.singletonList(transaction1));

        // Act
        ResponseEntity<TransactionAmountSumResponseDTO> response =
                testRestTemplate.exchange("/transactions/sum/10",
                        HttpMethod.GET, new HttpEntity<>(null), TransactionAmountSumResponseDTO.class);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSum()).isEqualTo(40.0d);
    }

    @Test
    void sumAmounts_withValidTransactionNotFound_mustReturnErrorNotFound() {

        // Act
        ResponseEntity<ErrorResponseDTO> response =
                testRestTemplate.exchange("/transactions/sum/10",
                        HttpMethod.GET, new HttpEntity<>(null), ErrorResponseDTO.class);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("error.transaction.not.found");
        assertThat(response.getBody().getMessage()).isEqualTo("Transaction with the given id couldn't be found");

    }
}
