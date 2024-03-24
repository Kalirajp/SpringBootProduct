package com.scb.SpringBootDemo.controller;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scb.SpringBootDemo.entity.Product;
import com.scb.SpringBootDemo.productservice.ProductService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ProductController {

	@Autowired
	ProductService service;

	@PostMapping("/productAdd")
	public void product(@RequestBody Product p) {
		service.addProduct(p);
	}

	@GetMapping("/product")
	public List<Product> meth() throws SQLException {
		System.out.println(service.getAllProduct());
		return service.getAllProduct();

	}

	// this need update product
	@PostMapping("/productUpdate/{id}")
	public void product(@PathVariable int id, @RequestBody Product p) {
		System.out.println(id);
		service.updateProduct(id, p);
	}

	@GetMapping("/fetchAllAndExport")
	public void fetchAllAndExport(HttpServletResponse response)
			throws SQLException, IOException {

		HSSFWorkbook workbook = service.generateExcel();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=ExportDump.xls");
		workbook.write(response.getOutputStream());
		workbook.close();

	}
	
	@PostMapping("/exportDump")
	public void importDumpForDB() {
		
	}

}
