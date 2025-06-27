package br.com.gooddreams.api.controllers;

import br.com.gooddreams.api.dtos.PaymentCardDTO;
import br.com.gooddreams.api.enuns.PaymentMethod;
import br.com.gooddreams.api.enuns.PaymentStatus;
import br.com.gooddreams.api.services.PaymentService;
import br.com.gooddreams.api.repository.OrderRepository;
import br.com.gooddreams.api.entities.Order;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.payment.Payment;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/mercadopago")
public class MercadoPagoController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    OrderRepository orderRepository;

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @PostMapping("/cartao")
    public ResponseEntity<?> pagarCartao(@RequestBody PaymentCardDTO paymentCardDTO, HttpServletRequest request)
    {
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) requestId = "N/A-" + System.currentTimeMillis();
        System.out.println(String.format("[Backend - %s] MercadoPagoController: Recebendo requisição para pagamento.", requestId));

        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            PaymentClient paymentClient = new PaymentClient();

            PaymentPayerRequest payerRequest = PaymentPayerRequest.builder()
                    .email(paymentCardDTO.payer().email())
                    .build();

            PaymentCreateRequest paymentRequest = PaymentCreateRequest.builder()
                    .transactionAmount(BigDecimal.valueOf(paymentCardDTO.value()))
                    .token(paymentCardDTO.token())
                    .installments(paymentCardDTO.installments())
                    .paymentMethodId(paymentCardDTO.paymentMethodId())
                    .issuerId(paymentCardDTO.issuerId() != null ? paymentCardDTO.issuerId() : null)
                    .payer(payerRequest)
                    .build();

            Payment payment = paymentClient.create(paymentRequest);
            System.out.println(String.format("[Backend - %s] Status do pagamento: %s", requestId, payment.getStatus()));
            System.out.println(String.format("[Backend - %s] Status detail: %s", requestId, payment.getStatusDetail()));
            System.out.println(String.format("[Backend - %s] ID do pagamento: %d", requestId, payment.getId()));

            if ("approved".equalsIgnoreCase(payment.getStatus())) {
                Long orderId = paymentCardDTO.orderId();
                System.out.println(String.format("[Backend - %s] Processando pagamento aprovado para Order ID: %d", requestId, orderId));

                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Compra não encontrada com ID: " + orderId));

                paymentService.storeOrUpdatePagamento(order, PaymentStatus.PAID, PaymentMethod.CREDIT_CARD, LocalDateTime.now());
                System.out.println(String.format("[Backend - %s] Pagamento e Ordem atualizados no banco de dados.", requestId));

                return ResponseEntity.ok(Map.of(
                        "status", payment.getStatus(),
                        "id", payment.getId(),
                        "message", "Pagamento aprovado e confirmado!"
                ));
            }

            System.out.println(String.format("[Backend - %s] Pagamento pendente ou recusado. Status: %s", requestId, payment.getStatus()));
            return ResponseEntity.ok(Map.of(
                    "status", payment.getStatus(),
                    "status_detail", payment.getStatusDetail(),
                    "id", payment.getId(),
                    "message", "Pagamento pendente ou recusado."));

        } catch (MPApiException ex) {
            System.err.println(String.format("[Backend - %s] Erro da API do Mercado Pago:", requestId));
            System.err.println("Status Code: " + ex.getStatusCode());
            System.err.println("Content: " + ex.getApiResponse().getContent());
            ex.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Erro ao processar pagamento - API",
                    "statusCode", ex.getStatusCode(),
                    "error", ex.getApiResponse().getContent()
            ));
        } catch (Exception ex) {
            System.err.println(String.format("[Backend - %s] Erro INESPERADO no processamento do pagamento:", requestId));
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", "Erro interno do servidor ao processar pagamento",
                    "errorDetails", ex.getMessage()
            ));
        }
    }

    // ... (Seu método /confirmar) ...
}