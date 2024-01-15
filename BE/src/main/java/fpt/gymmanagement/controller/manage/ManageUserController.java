package fpt.gymmanagement.controller.manage;

import fpt.gymmanagement.config.SwaggerConfiguration;
import fpt.gymmanagement.dto.request.user.UserRequest;
import fpt.gymmanagement.dto.search.UserSearch;
import fpt.gymmanagement.processor.UserProcessor;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("manage/users")
@RequiredArgsConstructor
@Api(tags = SwaggerConfiguration.MANAGE_USER_TAG)
public class ManageUserController {
    private final UserProcessor processor;

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(processor.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@ModelAttribute UserSearch search) {
        return ResponseEntity.ok(processor.getManageUsers(search));
    }

    @GetMapping("export-to-excel")
    public void exportToExcel(@ModelAttribute UserSearch search, HttpServletResponse response) {
        processor.exportToExcel(search, response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(processor.modifyUserIntoSystem(request, null));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@RequestBody @Valid UserRequest request, @PathVariable String id) {
        return ResponseEntity.ok(processor.modifyUserIntoSystem(request, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        processor.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("active/{id}")
    public ResponseEntity<?> active(@PathVariable String id) {
        processor.activeById(id);
        return ResponseEntity.ok().build();
    }
}
