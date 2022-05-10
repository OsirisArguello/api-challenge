package ar.example.apichallenge.application.service;

import ar.example.apichallenge.domain.model.TransactionBO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private List<TransactionBO> transactionList = new ArrayList<>();

    public void saveTransaction(TransactionBO transactionBO){
        transactionList.add(transactionBO);
    }

    public Optional<TransactionBO> findTransaction(Long transactionId){
        return transactionList.stream().filter( t -> transactionId.equals(t.getId())).findAny();
    }
}
