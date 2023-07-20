package com.shopme.admin.export;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends UserExporterAbstract {

    @Override
    public void export(List<User> users, HttpServletResponse response) throws IOException {
        setResponseHeader(response, "text/csv", ".csv");
        ICsvBeanWriter writer = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] fileHeader= {"ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
        writer.writeHeader(fileHeader);
        for(var user : users) {
            writer.write(user, fieldMapping);
        }
        writer.close();

    }
}
