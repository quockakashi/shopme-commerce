package com.shopme.admin.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends UserExporterAbstract {
    @Override
    public void export(List<User> users, HttpServletResponse response) throws IOException {
        setResponseHeader(response, "application/pdf", ".pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(66, 103, 178);
        font.setSize(18);
        font.isBold();
        Paragraph paragraph = new Paragraph("All Users List", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        document.add(paragraph);
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        createTable(table, users);
        document.add(table);
        document.close();
    }

    private void createTable(PdfPTable table, List<User> users) {
        createTableHeader(table);
        createTableLines(table, users);
    }

    private void createTableLines(PdfPTable table, List<User> users) {
        for(var user : users) {
            table.addCell(user.getId().toString());
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(user.getEnabled().toString());
        }
    }

    private void createTableHeader(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(14);
        font.setColor(Color.white);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(66, 103, 178));
        cell.setPadding(5);
        cell.setPhrase(new Phrase("User ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);
    }

}

