package ar.example.apichallenge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransactionBO {
    private Long id;
    private Double amount;
    private String type;
    private Long parentId;
}
