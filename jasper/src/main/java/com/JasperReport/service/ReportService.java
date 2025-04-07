package com.JasperReport.service;

import com.JasperReport.entity.Employee;
import com.JasperReport.repository.EmployeeRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private static final String REPORT_PATH = "C:\\Users\\LENOVO\\jasper123\\";  // Ensure path has a trailing slash

    @Autowired
    private EmployeeRepository employeeRepository;

    public String exportReport(String format) throws JRException {
        try {
            // Load the Jasper report template from classpath (src/main/resources)
            InputStream reportStream = getClass().getClassLoader().getResourceAsStream("emp.jrxml");

            if (reportStream == null) {
                throw new JRException("File not found: emp.jrxml in resources folder.");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Fetch data (list of employees) from the database
            List<Employee> employees = employeeRepository.findAll();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);

            // Set parameters (empty for now)
            Map<String, Object> parameters = new HashMap<>();

            // Generate the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export based on format
            if (format.equalsIgnoreCase("pdf")) {
                String pdfPath = REPORT_PATH + "employee_report.pdf";
                JasperExportManager.exportReportToPdfFile(jasperPrint, pdfPath);
                return "PDF report generated: " + pdfPath;
            } else if (format.equalsIgnoreCase("html")) {
                String htmlPath = REPORT_PATH + "employee_report.html";
                JasperExportManager.exportReportToHtmlFile(jasperPrint, htmlPath);
                return "HTML report generated: " + htmlPath;
            } else {
                return "Invalid format. Only 'pdf' or 'html' are supported.";
            }

        } catch (JRException e) {
            throw new JRException("Error generating the report: " + e.getMessage(), e);
        }
    }
}
