package fpt.gymmanagement.processor;

import com.google.zxing.WriterException;
import fpt.gymmanagement.common.CommonProcessor;
import fpt.gymmanagement.dto.DeviceDTO;
import fpt.gymmanagement.dto.search.DeviceSearch;
import fpt.gymmanagement.entity.Branch;
import fpt.gymmanagement.entity.Device;
import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.service.BranchService;
import fpt.gymmanagement.service.DeviceService;
import fpt.gymmanagement.service.DeviceTypeService;
import fpt.gymmanagement.service.UserService;
import fpt.gymmanagement.transform.DeviceTransform;
import fpt.gymmanagement.utils.CommonAppUtils;
import fpt.gymmanagement.utils.QRGenerate.QRCodeGenerator;
import fpt.gymmanagement.utils.excel.CommonExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeviceProcessor extends CommonProcessor<DeviceService, DeviceTransform, Device, DeviceDTO, String> {

    private final DeviceTypeService typeService;
    private final BranchService branchService;
    private final UserService userService;

    protected DeviceProcessor(DeviceService service, DeviceTransform transform,
                              DeviceTypeService typeService, BranchService branchService,
                              UserService userService) {
        super(service, transform);
        this.typeService = typeService;
        this.branchService = branchService;
        this.userService = userService;
    }

    public Page<DeviceDTO> getPage(DeviceSearch search) {
        if (!CommonAppUtils.isAdmin()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            search.setBranchId(user.getBranchId());
        }
        var page = service.getPage(search);
        List<DeviceDTO> content = page.getContent().stream().map(transform::toDto).collect(Collectors.toList());
        return new PageImpl<>(content, PageRequest.of(search.getPage(), search.getSize()),
                page.getTotalElements());
    }

    @Override
    public DeviceDTO saveOrUpdate(DeviceDTO deviceDTO, String id) {
        typeService.getOrElseThrow(deviceDTO.getTypeId());
        User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
        if (!CommonAppUtils.isAdmin()) {
            this.checkPermission(user, deviceDTO.getBranchId());
        }
        //check input
        if (id != null) {
            Device device = service.getOrElseThrow(id);
            BeanUtils.copyProperties(deviceDTO, device, "id", "userId", "createdDate");
            device.setUpdatedDate(new Date().getTime());
            return transform.toDto(service.saveOrUpdate(device, id));
        }
        deviceDTO.setUserId(user.getId());
        return super.saveOrUpdate(deviceDTO, null);
    }

    //NOTE: CHƯA PHÂN QUYỀN
    public void generateAndSaveQRCodeForDevice(DeviceDTO deviceDTO) {
        try {
            // Tạo chuỗi thông tin để lưu vào mã QR
            String qrCodeData = "Name:" + deviceDTO.getName() + "\n" +
                    "Price:" + deviceDTO.getPrice() + "\n" +
                    "RangMaintain:" + deviceDTO.getRangeMaintain();

            // Tạo mã QR từ chuỗi thông tin và lưu vào imageQR
            String qrCodeBase64 = QRCodeGenerator.generateBase64QR(qrCodeData);
            deviceDTO.setImageQR(qrCodeBase64);

            // Cập nhật giá trị của imageQR
            Device device = service.getOrElseThrow(deviceDTO.getId());
            device.setImageQR(qrCodeBase64);
            service.saveOrUpdate(device, deviceDTO.getId());
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public DeviceDTO getDeviceByName(String deviceName) {
        Device device = service.getDeviceByName(deviceName);
        if (device == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thiết bị có tên: " + deviceName);
        }
        return transform.toDto(device);
    }

    @Override
    public void deleteById(String s) {
        Device device = service.getOrElseThrow(s);
        if(!CommonAppUtils.isAdmin()) {
            User user = userService.getByUsername(CommonAppUtils.getCurrentAccount());
            this.checkPermission(user, device.getBranchId());
        }
        super.deleteById(s);
    }

    private void checkPermission(User user, String branchId) {
        Branch branch = branchService.getOrElseThrow(branchId);
        if (!user.getBranchId().equals(branchId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn không có quyền thực thi với device cho chi nhánh " + branch.getName() + "!!");
        }
    }


    //NOTE: CHƯA PHÂN QUYỀN
    public void exportToExcel(DeviceSearch search, HttpServletResponse response) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String nameFile = "records_devices_" + LocalDate.now().format(dateFormatter) + ".xlsx";
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + nameFile;

        response.setHeader(headerKey, headerValue);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/octet-stream");
        List<DeviceDTO> listOfRecords = service.getPage(search)
                .getContent().stream().map(transform::toDto).collect(Collectors.toList());
        exportToExcel(listOfRecords, response);
    }


    public void exportToExcel(List<DeviceDTO> devices, HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            OutputStream os = response.getOutputStream();
            CommonExcel styleContainer = CommonExcel.defaultStyle(workbook);
            Sheet sheet = workbook.createSheet("Devices");
            List<String> headers = CommonExcel.generateHeadersOfDevice();

            Row currentRow = sheet.createRow(0);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));
            currentRow.setHeight((short) 500);
            Cell currentCell = currentRow.createCell(0);
            currentCell.setCellStyle(styleContainer.getTitleStyle());
            currentCell.setCellValue("DANH SÁCH THIẾT BỊ");

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
            List<List<Object>> dataExport = this.convertData(devices);
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

    private List<List<Object>> convertData(List<DeviceDTO> dtos) {
        List<List<Object>> data = new ArrayList<>();
        for (DeviceDTO dto : dtos) {
            List<Object> objects = new ArrayList<>();
            objects.add(dto.getId());
            objects.add(dto.getName());
            objects.add(dto.getImageUrl());
            objects.add(dto.getRangeMaintain());
            objects.add(dto.getStatus());
            objects.add(dto.getType().getName());
            data.add(objects);
        }
        return data;
    }
}
