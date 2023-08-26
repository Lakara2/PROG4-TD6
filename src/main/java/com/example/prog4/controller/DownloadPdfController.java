package com.example.prog4.controller;

import com.example.prog4.controller.mapper.EmployeeMapper;
import com.example.prog4.model.Employee;
import com.example.prog4.service.EmployeeService;
import com.example.prog4.service.ExportPdfService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class DownloadPdfController {

    @Autowired
    private ExportPdfService exportPdfService;
    private EmployeeMapper employeeMapper;
    private EmployeeService employeeService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/download/{eId}")
    public void download(HttpServletResponse response, @PathVariable String eId) throws IOException {
        Employee employee = employeeMapper.toView(employeeService.getOne(eId));
        Map<String, Object> data = new HashMap<>();
        data.put("employee", employee);
        ByteArrayInputStream exportedData = exportPdfService.exportReceiptPdf("employee_form", data);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=thymeleaf.pdf");
        IOUtils.copy(exportedData, response.getOutputStream());
    }
}
