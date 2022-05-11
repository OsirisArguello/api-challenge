package ar.example.apichallenge.application.usecase;

import ar.example.apichallenge.application.exception.TransactionAmountSumException;
import ar.example.apichallenge.application.service.TransactionService;
import ar.example.apichallenge.domain.model.TransactionBO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SumTransactionsAmountUC {
    private final TransactionService transactionService;

    /**
     * Sums all amounts of the transaction related by parent with the transaction with the given id
     *
     * @param transactionId the transaction id to find to sum all related transactions by parent id
     * @return the sum of all the amounts
     */
    public Double sumAmountsRelatedByParent(Long transactionId) {
        Optional<TransactionBO> maybeTransaction = transactionService.findTransactionById(transactionId);
        if (maybeTransaction.isEmpty())
            throw new TransactionAmountSumException();

        TransactionBO transaction = maybeTransaction.get();

        //If there's no parent the sum is only the amount of the transaction
        if (transaction.getParentId() == null)
            return transaction.getAmount();

        return transactionService.findTransactionsByParentId(transaction.getParentId()).stream()
                .mapToDouble(TransactionBO::getAmount).sum();
    }
}
