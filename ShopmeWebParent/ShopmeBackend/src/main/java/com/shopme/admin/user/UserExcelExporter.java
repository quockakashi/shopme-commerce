package com.shopme.admin.user;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends UserExporterAbstract{
    private XSSFWorkbook workbook;

    public UserExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    private void createHeaderLine(XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        headerStyle.setFont(font);

        createCell(headerStyle, row, 0, "User ID");
        createCell(headerStyle, row, 1, "E-mail");
        createCell(headerStyle, row, 2, "First Name");
        createCell(headerStyle, row, 3, "Last Name");
        createCell(headerStyle, row, 4, "Roles");
        createCell(headerStyle, row, 5, "Enabled");
    }

    private void createDateLines(XSSFSheet sheet, List<User> users) {
        int pos = 1;
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for(var user : users) {
            var row = sheet.createRow(pos);
            createCell(style, row, 0, user.getId().toString());
            createCell(style, row, 1, user.getEmail());
            createCell(style, row, 2, user.getFirstName());
            createCell(style, row, 3, user.getLastName());
            createCell(style, row, 4, user.getEnabled().toString());
            createCell(style, row, 5, user.getRoles().toString());
            pos++;
        }
    }

    private void createCell(XSSFCellStyle style, XSSFRow row, int pos, String value) {
        XSSFCell cell = row.createCell(pos);
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }
    @Override
    public void export(List<User> users, HttpServletResponse response) throws IOException {
        setResponseHeader(response, "application/octet-stream", ".xlsx");

        try(var outStream = response.getOutputStream()) {
            XSSFSheet sheet = workbook.createSheet("users");
            createHeaderLine(sheet);
            createDateLines(sheet, users);
            workbook.write(outStream);
        }
    }
}
