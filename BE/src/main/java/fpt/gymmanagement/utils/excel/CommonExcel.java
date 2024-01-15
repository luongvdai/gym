package fpt.gymmanagement.utils.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommonExcel {
    private CellStyle headerStyle;
    private CellStyle titleStyle;
    private CellStyle borderStyle;

    public CommonExcel(CellStyle headerStyle,
                       CellStyle titleStyle,
                       CellStyle borderStyle) {
        this.headerStyle = headerStyle;
        this.titleStyle = titleStyle;
        this.borderStyle = borderStyle;
    }

    public static CommonExcel defaultStyle(Workbook workbook) {
        CellStyle headerCellStyle = CellUtils.createHeaderStyle(workbook);
        CellStyle titleStyle = CellUtils.createTitleStyle(workbook);
        CellStyle borderStyle = CellUtils.createBorderStyle(workbook);
        return new CommonExcel(headerCellStyle, titleStyle, borderStyle);
    }

    public static void setValue(Object value, Cell cell) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

    }

    public static List<String> generateHeadersOfDevice() {
        List<String> result = new ArrayList<>();
        result.add("ID");
        result.add("Name");
        result.add("Image URL");
        result.add("Range Maintain");
        result.add("Status");
        result.add("Type");
        return result;
    }

    public static List<String> generateHeadersOfUser() {
        List<String> result = new ArrayList<>();
        result.add("ID");
        result.add("Username");
        result.add("FullName");
        result.add("Email");
        result.add("PhoneNumber");
        result.add("Address");
        result.add("Role");
        result.add("Gender");
        result.add("DateOfBirth");
        result.add("Active");
        result.add("Type");
        result.add("Branch");
        return result;
    }

    public static List<String> generateHeadersOfCourse() {
        List<String> result = new ArrayList<>();
        result.add("ID");
        result.add("Title");
        result.add("IsActive");
        result.add("Price");
        result.add("CourseType");
        result.add("Branch");
        return result;
    }
}