package ar.example.apichallenge.application.usecase;

import ar.example.apichallenge.application.service.TransactionService;
import ar.example.apichallenge.domain.model.TransactionBO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FindTransactionUC {

    private final TransactionService transactionService;

    /**
     * Find all transactions ids of the same type
     *
     * @param type transaction type of the transactions to find
     * @return List of transaction ids
     */
    public List<Long> findByType(String type) {
        return transactionService.findTransactionByType(type).stream()
                .map(TransactionBO::getId)
                .collect(Collectors.toList());
    }
}
