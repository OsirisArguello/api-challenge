package ar.example.apichallenge.application.usecase;

import ar.example.apichallenge.application.exception.TransactionCreationException;
import ar.example.apichallenge.application.service.TransactionService;
import ar.example.apichallenge.domain.model.TransactionBO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateTransactionUC {

    private final TransactionService service;

    /**
     * Tries to create a transaction, validating before if there's a transaction with the same id, in
     * which case throws an exception
     *
     * @param transactionBO transaction to create
     */
    public void create(TransactionBO transactionBO) {
        Optional<TransactionBO> maybeTransaction = service.findTransaction(transactionBO.getId());

        if (maybeTransaction.isPresent())
            throw new TransactionCreationException();

        service.saveTransaction(transactionBO);
    }
}
