package fpt.gymmanagement.controller.order;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.request.order.OrderItem;
import fpt.gymmanagement.dto.request.order.OrderRequest;
import fpt.gymmanagement.dto.request.order.PaymentRequest;
import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.enums.EOrderStatus;
import fpt.gymmanagement.processor.OrderProcessor;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("orders")
@Slf4j
@Api(tags = SwaggerConfiguration.ORDER_USER_TAG)
public class OrderController {
    private final OrderProcessor processor;

    public OrderController(OrderProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getDetailByClient(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid OrderRequest request) {
        request.setStatus(EOrderStatus.PENDING);
        return ResponseEntity.ok(processor.createByUser(request));
    }

    @PutMapping("{orderId}")
    public ResponseEntity<?> update(@RequestBody @Valid OrderRequest request, @PathVariable String orderId) {
        request.setStatus(EOrderStatus.PENDING);
        return ResponseEntity.ok(processor.updateByUser(request, orderId));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateByUser(@RequestBody @Valid OrderItem request, @PathVariable String id) {
        processor.updateOrderDetail(request, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable String id) {
        processor.deleteOrderDetail(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<?> delete(@PathVariable String orderId) {
        processor.deleteById(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getMyOrders(@ModelAttribute OrderSearch search) {
        return ResponseEntity.ok(processor.getMyOrder(search));
    }

    @GetMapping("new-payment")
    public ResponseEntity<?> newPayment(@ModelAttribute PaymentRequest request) {
        return ResponseEntity.ok(processor.createPayment(request));
    }

    @GetMapping("transaction")
    public RedirectView transaction(@RequestParam(value = "vnp_ResponseCode") String responseCode,
                                    @RequestParam(value = "vnp_TxnRef") String orderId) {
        String frontendUrl = "https://gym-management-system-theta.vercel.app/ordersuccess";
        if (responseCode.equals("00")) {
            //execute update status of order COMPLETE
            processor.updateStatusOrderSuccess(orderId, true);
            // URL của trang frontend

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(frontendUrl)
                    .queryParam("transactionId", orderId)
                    .queryParam("orderSuccess", true);
            return new RedirectView(builder.toUriString());
        } else {
            //execute update status of order FAILED
            processor.updateStatusOrderSuccess(orderId, false);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(frontendUrl)
                    .queryParam("transactionId", orderId)
                    .queryParam("orderSuccess", false);
            return new RedirectView(builder.toUriString());
        }
    }
}
