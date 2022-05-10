package ar.example.apichallenge.api.rest.controller;

import ar.example.apichallenge.api.rest.dto.TransactionCreationRequestDTO;
import ar.example.apichallenge.api.rest.dto.TransactionCreationResponseDTO;
import ar.example.apichallenge.application.usecase.CreateTransactionUC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionApiController {

    private final CreateTransactionUC createTransactionUC;

    @PutMapping(value = "/{transaction_id}")
    public ResponseEntity<TransactionCreationResponseDTO> create(@PathVariable("transaction_id") Long transactionId, @Valid @RequestBody TransactionCreationRequestDTO request) {

        createTransactionUC.create(request.to(transactionId));

        TransactionCreationResponseDTO response = new TransactionCreationResponseDTO("OK");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
