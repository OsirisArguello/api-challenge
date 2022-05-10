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

    public List<Long> findByType(String type) {
        return transactionService.findTransactionByType(type).stream()
                .map(TransactionBO::getId)
                .collect(Collectors.toList());
    }
}
