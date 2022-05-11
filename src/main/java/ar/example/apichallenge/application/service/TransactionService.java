package ar.example.apichallenge.application.service;

import ar.example.apichallenge.domain.model.TransactionBO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private List<TransactionBO> transactionList = new ArrayList<>();

    public void saveTransaction(TransactionBO transactionBO) {
        transactionList.add(transactionBO);
    }

    public Optional<TransactionBO> findTransactionById(Long transactionId) {
        return transactionList.stream().filter(t -> transactionId.equals(t.getId())).findAny();
    }

    public List<TransactionBO> findTransactionByType(String type) {
        return transactionList.stream().filter(t -> type.equals(t.getType()))
                .collect(Collectors.toList());
    }

    public List<TransactionBO> findTransactionsByParentId(Long parentId) {
        return transactionList.stream().filter(t -> parentId.equals(t.getParentId()))
                .collect(Collectors.toList());
    }
}
