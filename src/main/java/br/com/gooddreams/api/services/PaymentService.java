package br.com.gooddreams.api.services;

import br.com.gooddreams.api.dtos.PaymentCreateDTO;
import br.com.gooddreams.api.dtos.PaymentResponseDTO;
import br.com.gooddreams.api.dtos.PaymentUpdateDTO;
import br.com.gooddreams.api.entities.Order;
import br.com.gooddreams.api.entities.Payment;
import br.com.gooddreams.api.enuns.PaymentMethod;
import br.com.gooddreams.api.enuns.PaymentStatus;
import br.com.gooddreams.api.enuns.OrderStatus;
import br.com.gooddreams.api.mappers.PaymentMapper;
import br.com.gooddreams.api.repository.OrderRepository;
import br.com.gooddreams.api.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public PaymentResponseDTO create(PaymentCreateDTO dto) {
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

        Payment payment = PaymentMapper.toEntity(dto, order);
        paymentRepository.save(payment);
        return PaymentMapper.toDTO(payment);
    }

    public List<PaymentResponseDTO> list() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toDTO)
                .toList();
    }

    public PaymentResponseDTO show(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));
        return PaymentMapper.toDTO(payment);
    }

    @Transactional
    public PaymentResponseDTO update(PaymentUpdateDTO dto) {
        Payment payment = paymentRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));
        payment.setOrder(dto.order());
        payment.setPaymentMethod(dto.paymentMethod());
        payment.setPaymentStatus(dto.paymentStatus());
        payment.setDateTimePayment(dto.dateTimePayment());
        return PaymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Transactional
    public void destroy(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado."));
        paymentRepository.delete(payment);
    }

    @Transactional
    public Payment storeOrUpdatePagamento(Order order, PaymentStatus status, PaymentMethod method, LocalDateTime dateTimePayment) {
        try {
            Optional<Payment> optPayment = paymentRepository.findByOrder(order);
            Payment payment;
            if (optPayment.isPresent()) {
                payment = optPayment.get();
                System.out.println("PaymentService: Atualizando pagamento existente id: " + payment.getId());
            } else {
                payment = new Payment();
                System.out.println("PaymentService: Criando novo pagamento para compraId: " + order.getId());
            }

            payment.setOrder(order);
            payment.setPaymentStatus(status);
            payment.setPaymentMethod(method);
            payment.setDateTimePayment(dateTimePayment);

            System.out.println("PaymentService: Tentando salvar Payment com status: " + status + " e método: " + method);
            Payment savedPayment = paymentRepository.save(payment);
            System.out.println("PaymentService: Payment salvo no contexto com ID: " + savedPayment.getId() + " e status: " + savedPayment.getPaymentStatus());


            if (order != null) {
                order.setStatus(OrderStatus.PAYED);
                order.setPaymentMethod(method);
                System.out.println("PaymentService: Tentando salvar Order " + order.getId() + " com status: " + order.getStatus() + " e método: " + order.getPaymentMethod());
                orderRepository.save(order);
                System.out.println("PaymentService: Order " + order.getId() + " salva no contexto com status: " + order.getStatus() + " e método: " + order.getPaymentMethod());
            }


            return savedPayment;
        } catch (Exception e) {
            System.err.println("PaymentService: ERRO CRÍTICO ao tentar salvar pagamento/ordem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar pagamento e ordem: " + e.getMessage(), e);
        }
    }
}