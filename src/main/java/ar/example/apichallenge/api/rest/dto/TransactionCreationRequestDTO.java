package ar.example.apichallenge.api.rest.dto;

import ar.example.apichallenge.domain.model.TransactionBO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TransactionCreationRequestDTO {
    @NotNull
    private Double amount;
    @NotEmpty
    private String type;
    @JsonProperty("parent_id")
    private Long parentId;

    public TransactionBO to(Long transactionId){
        return new TransactionBO(transactionId, this.getAmount(), this.getType(), this.getParentId());
    }
}
