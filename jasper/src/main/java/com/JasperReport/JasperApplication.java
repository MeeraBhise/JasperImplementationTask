package com.JasperReport;


import com.JasperReport.entity.Employee;
import com.JasperReport.repository.EmployeeRepository;
import com.JasperReport.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
@RestController
public class JasperApplication {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ReportService reportService;

	@GetMapping("/getEmployees")
	public List<Employee> getEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping("/report/{format}")
	public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException {
		return reportService.exportReport(format);
	}
	public static void main(String[] args)
	{
		SpringApplication.run(JasperApplication.class, args);
	}
}
