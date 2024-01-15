package fpt.gymmanagement.processor;

import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.CourseDTO;
import fpt.gymmanagement.dto.search.CourseSearch;
import fpt.gymmanagement.entity.Branch;
import fpt.gymmanagement.entity.Course;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.service.BranchService;
import fpt.gymmanagement.service.CourseService;
import fpt.gymmanagement.service.CourseTypeService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.CourseTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import fpt.gymmanagement.utils.excel.CommonExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseProcessor extends CommonProcessor<CourseService, CourseTransform, Course, CourseDTO, String> {
    private final BranchService branchService;
    private final CourseTypeService courseTypeService;
    private final UserService userService;

    protected CourseProcessor(CourseService service, CourseTransform transform,
                              BranchService branchService, CourseTypeService courseTypeService,
                              UserService userService) {
        super(service, transform);
        this.branchService = branchService;
        this.courseTypeService = courseTypeService;
        this.userService = userService;
    }

    public Page<CourseDTO> getPage(CourseSearch search) {
        var page = service.getPage(search);
        List<CourseDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    @Override
    public CourseDTO saveOrUpdate(CourseDTO courseDTO, String courseId) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        Branch branch = branchService.getOrElseThrow(courseDTO.getBranchId());
        this.checkPermission(user, branch.getId());
        if (courseId != null) {
            Course course = service.getOrElseThrow(courseId);
            this.checkPermission(user, course.getBranchId());
        }
        courseTypeService.getOrElseThrow(courseDTO.getCourseTypeId());
        return super.saveOrUpdate(courseDTO, courseId);
    }

    @Override
    public void deleteById(String courseId) {
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        Course course = service.getOrElseThrow(courseId);
        this.checkPermission(user, course.getBranchId());
        course.setIsActive(true);
        super.deleteById(courseId);
    }

    private void checkPermission(User user, String branchId) {
        if (CommonAppUtils.isAdmin()) return;
        if (!user.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền tạo khóa học!!");
        }
    }

    public void exportToExcel(CourseSearch search, HttpServletResponse response) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nameFile = "records_course_" + LocalDate.now().format(dateFormatter) + ".xlsx";
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + nameFile;

        response.setHeader(headerKey, headerValue);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/octet-stream");
        List<CourseDTO> listOfRecords = service.getPage(search)
                .getContent().stream().map(transform::toDto).collect(Collectors.toList());
        exportToExcel(listOfRecords, response);
    }


    public void exportToExcel(List<CourseDTO> courseDTOS, HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            OutputStream os = response.getOutputStream();
            CommonExcel styleContainer = CommonExcel.defaultStyle(workbook);
            Sheet sheet = workbook.createSheet("Course");
            List<String> headers = CommonExcel.generateHeadersOfCourse();

            Row currentRow = sheet.createRow(0);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
            currentRow.setHeight((short) 500);
            Cell currentCell = currentRow.createCell(0);
            currentCell.setCellStyle(styleContainer.getTitleStyle());
            currentCell.setCellValue("DANH SÁCH KHÓA HỌC");

            int lastRow = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(lastRow);

            // Create header row
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(styleContainer.getHeaderStyle());
                cell.setCellValue(headers.get(i));
            }

            // Create data rows
            lastRow++;
            List<List<Object>> dataExport = this.convertData(courseDTOS);
            for (int i = 0; i < dataExport.size(); i++) {
                row = sheet.createRow(i + lastRow);
                List<Object> valueFields = dataExport.get(i);
                for (int x = 0; x < valueFields.size(); x++) {
                    Cell cell = row.createCell(x);
                    cell.setCellStyle(styleContainer.getBorderStyle());
                    CommonExcel.setValue(valueFields.get(x), cell);
                }
            }
            //AUTO SIZE
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(os);
            os.close();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Xuất file excel thất bại!!");
        }
    }

    private List<List<Object>> convertData(List<CourseDTO> dtos) {
        List<List<Object>> data = new ArrayList<>();
        for (CourseDTO dto : dtos) {
            List<Object> objects = new ArrayList<>();
            objects.add(dto.getId());
            objects.add(dto.getTitle());
            if (dto.getIsActive()) {
                objects.add("Hoạt động");
            } else {
                objects.add("Không hoạt động");
            }
            objects.add(dto.getPrice().toString());
            objects.add(dto.getType().getName());
            objects.add(dto.getBranch().getName());
            data.add(objects);
        }
        return data;
    }
}
