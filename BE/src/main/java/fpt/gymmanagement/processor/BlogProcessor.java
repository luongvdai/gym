package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.BlogDTO;
import fpt.gymmanagement.dto.UserDTO;
import fpt.gymmanagement.dto.search.BlogSearch;
import fpt.gymmanagement.entity.Blog;
import fpt.gymmanagement.entity.Branch;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.service.BlogService;
import fpt.gymmanagement.service.BlogTypeService;
import fpt.gymmanagement.service.BranchService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.BlogTransform;
import fpt.gymmanagement.transform.UserTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BlogProcessor extends CommonProcessor<BlogService, BlogTransform, Blog, BlogDTO, String> {
    private final BranchService branchService;
    private final UserService userService;
    private final BlogTypeService blogTypeService;

    private final UserTransform userTransform;


    protected BlogProcessor(BlogService service, BlogTransform transform,
                            BranchService branchService, UserService userService, BlogTypeService blogTypeService, UserTransform userTransform) {
        super(service, transform);
        this.branchService = branchService;
        this.userService = userService;
        this.blogTypeService = blogTypeService;
        this.userTransform = userTransform;
    }

    public Page<BlogDTO> getPage(BlogSearch search) {
        var page = service.getPage(search);
        List<BlogDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    public List<UserDTO> getUserManageAdminMentor(String branchId) {
        var users = userService.getManageBlogByBranch(branchId);
        return users.stream().map(userTransform::toDto).collect(Collectors.toList());
    }

    @Override
    public BlogDTO saveOrUpdate(BlogDTO blogDTO, String id) {
        blogTypeService.getOrElseThrow(blogDTO.getTypeId());//check type
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        this.checkPermission(user, blogDTO.getBranchId());//có quyền thực thi ở blog này
        if (id != null) {
            Blog blog = service.getOrElseThrow(id);
            this.checkMentor(user, blog);
            BeanUtils.copyProperties(blogDTO, blog, "id", "userId", "createdDate");
            blog.setUpdatedDate(new Date().getTime());
            return transform.toDto(service.saveOrUpdate(blog, id));
        }
        blogDTO.setUserId(user.getId());
        return super.saveOrUpdate(blogDTO, null);
    }

    @Override
    public void deleteById(String id) {
        Blog blog = service.getOrElseThrow(id);
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        this.checkPermission(user, blog.getBranchId());
        this.checkMentor(user, blog);
        service.deleteById(id);
    }

    private void checkPermission(User user, String branchId) {
        if (CommonAppUtils.isAdmin()) return;
        Branch branch = branchService.getOrElseThrow(branchId);
        if (!user.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Bạn không có quyền thực thi với blog cho chi nhánh " + branch.getName() + "!!");
        }
    }

    private void checkMentor(User user, Blog blog) {
        if (user.getRole().getPosition().equals("MENTOR")) {
            if (!blog.getUserId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể thực thi hành động lên blog không phải mình tạo!!");
            }
        }
    }
}
