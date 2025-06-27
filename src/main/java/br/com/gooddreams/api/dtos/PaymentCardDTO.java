package br.com.gooddreams.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentCardDTO(

        String token,
        @JsonProperty("compraId")
        Long orderId,
        Long clientId,
        @JsonProperty("payment_method_id")
        String paymentMethodId,
        @JsonProperty("issuer_id")
        String issuerId,
        Payer payer,
        Integer installments,
        @JsonProperty("transaction_amount")
        Double value
) {

    public record Payer(
            String email
    ) {
    }
}
