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

        if (transactionBO.getParentId() != null)
            validateParentExists(transactionBO.getParentId());

        Optional<TransactionBO> maybeTransaction = service.findTransactionById(transactionBO.getId());

        if (maybeTransaction.isPresent())
            throw TransactionCreationException.fromTransactionAlreadyExists();

        service.saveTransaction(transactionBO);
    }

    private void validateParentExists(Long parentId) {
        Optional<TransactionBO> maybeParent = service.findTransactionById(parentId);
        if (maybeParent.isEmpty())
            throw TransactionCreationException.fromParentDoesntExists();
    }
}
