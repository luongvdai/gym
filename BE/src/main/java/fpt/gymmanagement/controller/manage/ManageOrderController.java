package fpt.gymmanagement.controller.manage;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.request.order.ApproveOrderRequest;
import fpt.gymmanagement.dto.request.order.AssignRequest;
import fpt.gymmanagement.dto.request.order.OrderByStaffRequest;
import fpt.gymmanagement.dto.request.order.OrderItem;
import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.enums.EOrderStatus;
import fpt.gymmanagement.processor.OrderProcessor;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("manage/orders")
@Api(tags = SwaggerConfiguration.MANAGE_ORDER_TAG)
public class ManageOrderController {
    private final OrderProcessor processor;

    public ManageOrderController(OrderProcessor processor) {
        this.processor = processor;
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute OrderSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

    @PostMapping("create-for-user")
    public ResponseEntity<?> createForUser(@RequestBody @Valid OrderByStaffRequest request) {
        request.setStatus(EOrderStatus.COMPLETE);
        return ResponseEntity.ok(processor.createByStaff(request));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateForUser(@RequestBody @Valid OrderItem request, @PathVariable String id) {
        processor.updateOrderDetail(request, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("order-detail/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable String id) {
        processor.deleteOrderDetail(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("action")
    public ResponseEntity<?> approveOrReject(@RequestBody @Valid ApproveOrderRequest request) {
        return ResponseEntity.ok(processor.approveOrReject(request));
    }

    @PostMapping("assign")
    public ResponseEntity<?> assignStaff(@RequestBody @Valid AssignRequest request) {
        processor.assignStaffToOrders(request);
        return ResponseEntity.ok().build();
    }

    //API lấy những người approve
    @GetMapping("user-manage")
    public ResponseEntity<?> getUserManage(@RequestParam(required = false) String branchId) {
        return ResponseEntity.ok(processor.getUserManage(branchId));
    }

    @GetMapping("user-assign")
    public ResponseEntity<?> getUserAssign(@RequestParam(required = false) String branchId) {
        return ResponseEntity.ok(processor.getUserAssign(branchId));
    }
}
