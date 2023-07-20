package com.shopme.admin.export;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class UserExporterAbstract {

    public abstract void export(List<User> users, HttpServletResponse response) throws IOException;

    protected void setResponseHeader(HttpServletResponse response, String contentType, String extension) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = dateFormat.format(System.currentTimeMillis());
        String fileName = "users_" + timeStamp + extension;

        response.setContentType(contentType);
        String headerKey="Content-Disposition";
        String headerValue="attachment; fileName=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}
