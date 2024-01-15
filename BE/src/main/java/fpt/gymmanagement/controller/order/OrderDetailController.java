package fpt.gymmanagement.controller.order;

import fpt.gymmanagement.common.CommonCUDController;
import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.OrderDetailDTO;
import fpt.gymmanagement.dto.search.OrderDetailSearch;
import fpt.gymmanagement.processor.OrderDetailProcessor;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("order-details")
@Slf4j
@Api(tags = SwaggerConfiguration.ORDER_DETAIL_USER_TAG)
public class OrderDetailController extends CommonCUDController<OrderDetailProcessor, OrderDetailDTO, String> {
    protected OrderDetailController(OrderDetailProcessor processor) {
        super(processor);
    }

    @GetMapping
    public ResponseEntity<?> getPage(@ModelAttribute OrderDetailSearch search) {
        return ResponseEntity.ok(processor.getPage(search));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

}
