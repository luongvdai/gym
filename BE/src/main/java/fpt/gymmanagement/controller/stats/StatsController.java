package fpt.gymmanagement.controller.stats;

import fpt.gymmanagement.dto.search.OrderSearch;
import fpt.gymmanagement.dto.search.UserSearch;
import fpt.gymmanagement.processor.StatsProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stats")
public class StatsController {
    private final StatsProcessor processor;

    public StatsController(StatsProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("new-user")
    public ResponseEntity<?> getTotalNewUserByTime(UserSearch search) {
        return ResponseEntity.ok(processor.getTotalNewUserByTime(search));
    }

    @GetMapping("revenue")
    public ResponseEntity<?> getRevenueByTime(OrderSearch search) {
        return ResponseEntity.ok(processor.getRevenueByTime(search));
    }

    @GetMapping("revenue/by-year")
    public ResponseEntity<?> getRevenueByYear(@RequestParam(required = false) Integer year, @RequestParam(required = false) String branchId) {
        return ResponseEntity.ok(processor.getRevenueByYear(year, branchId));
    }

    @GetMapping("top-course")
    public ResponseEntity<?> getTopCourse(OrderSearch search) {
        return ResponseEntity.ok(processor.getTopCourse(search));
    }
}
