package ar.example.apichallenge.api.rest.controller;

import ar.example.apichallenge.api.rest.dto.TransactionAmountSumResponseDTO;
import ar.example.apichallenge.api.rest.dto.TransactionCreationRequestDTO;
import ar.example.apichallenge.api.rest.dto.TransactionCreationResponseDTO;
import ar.example.apichallenge.application.usecase.CreateTransactionUC;
import ar.example.apichallenge.application.usecase.FindTransactionUC;
import ar.example.apichallenge.application.usecase.SumTransactionsAmountUC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionApiController {

    private final CreateTransactionUC createTransactionUC;
    private final FindTransactionUC findTransactionUC;
    private final SumTransactionsAmountUC sumTransactionsAmountUC;

    @PutMapping(value = "/{transaction_id}")
    public ResponseEntity<TransactionCreationResponseDTO> create(@PathVariable("transaction_id") Long transactionId, @Valid @RequestBody TransactionCreationRequestDTO request) {

        createTransactionUC.create(request.to(transactionId));

        TransactionCreationResponseDTO response = new TransactionCreationResponseDTO("OK");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/types/{type}")
    public ResponseEntity<List<Long>> findByType(@PathVariable("type") String type) {

        List<Long> transactionList = findTransactionUC.findByType(type);

        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }

    @GetMapping(value = "/sum/{transaction_id}")
    public ResponseEntity<TransactionAmountSumResponseDTO> sumAmounts(@PathVariable("transaction_id") Long transactionId) {

        TransactionAmountSumResponseDTO response = new TransactionAmountSumResponseDTO(sumTransactionsAmountUC.sumAmountsRelatedByParent(transactionId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
