package br.com.gooddreams.api.mappers;

import br.com.gooddreams.api.dtos.PaymentCreateDTO;
import br.com.gooddreams.api.dtos.PaymentResponseDTO;
import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.entities.Payment;

public class PaymentMapper {

    public static Payment toEntity(PaymentCreateDTO dto, Order order) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(dto.paymentMethod());
        payment.setPaymentStatus(dto.paymentStatus());
        payment.setDateTimePayment(dto.dateTimePayment());
        return payment;
    }

    public static PaymentResponseDTO toDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getOrder(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getDateTimePayment()
        );
    }
}
