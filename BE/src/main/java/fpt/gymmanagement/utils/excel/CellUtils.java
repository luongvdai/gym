package fpt.gymmanagement.utils.excel;

import org.apache.poi.ss.usermodel.*;

public class CellUtils {
    private static final String FONT_FAMILY = "Times New Roman";

    private CellUtils() {
    }

    public static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFront = workbook.createFont();
        titleFront.setBold(true);
        titleFront.setFontHeightInPoints((short) 18);
        titleFront.setFontName(FONT_FAMILY);
        titleStyle.setFont(titleFront);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        return titleStyle;
    }

    public static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 13);
        headerFont.setFontName(FONT_FAMILY);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.LEFT);
        return headerCellStyle;
    }

    public static CellStyle createBorderStyle(Workbook workbook) {
        CellStyle cellStyleCellBorder = workbook.createCellStyle();
        cellStyleCellBorder.setBorderTop(BorderStyle.THIN);
        cellStyleCellBorder.setBorderBottom(BorderStyle.THIN);
        cellStyleCellBorder.setBorderLeft(BorderStyle.THIN);
        cellStyleCellBorder.setBorderRight(BorderStyle.THIN);

        Font contentFront = workbook.createFont();
        contentFront.setFontHeightInPoints((short) 13);
        contentFront.setFontName(FONT_FAMILY);
        cellStyleCellBorder.setFont(contentFront);
        cellStyleCellBorder.setAlignment(HorizontalAlignment.LEFT);
        return cellStyleCellBorder;
    }
}
