package xyz.mowrish.springpaymentqr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PaymentRequest {

    private String merchantUpiId;
    private String merchantName;
    private Long amount;

    @JsonIgnore
    private String transactionRemarks;

}
