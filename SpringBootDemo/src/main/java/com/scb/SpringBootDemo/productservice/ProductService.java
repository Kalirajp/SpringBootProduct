package com.scb.SpringBootDemo.productservice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
import com.scb.SpringBootDemo.DBService.DBServicee;
import com.scb.SpringBootDemo.entity.Product;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ProductService {

	List<Product> list = new ArrayList<>();

	@Autowired
	DBServicee db;

	@Autowired
	Product prod;

	public void addProduct(Product p) {

		db.add(p);
	}

	public List<Product> getAllProduct() throws SQLException {

		List<Product> list = new ArrayList<>();

		ResultSet product = db.getProduct();
		while (product.next()) {
			System.out.println("Test " + " " + product);
			// Product prod=new Product();

			prod.setId(product.getInt("id"));
			prod.setName(product.getString("name"));
			prod.setType(product.getString("type"));
			prod.setPlace(product.getString("place"));
			prod.setWarranty(product.getInt("warranty"));
			list.add(prod);

		}
		return list;
	}

	public Product getProduct(String name) {
		Product filter = list.stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst().get();
		return filter;
	}

	public List<Product> getProductWithMatchingText(String string) {
		Predicate<Product> predi = new Predicate<Product>() {

			@Override
			public boolean test(Product t) {
				return t.getName().contains(string) || t.getPlace().contains(string) || t.getType().contains(string);
			}
		};
		return list.stream().filter(predi).collect(Collectors.toList());
	}

	public List<Product> searchByPlace(String string) {
		return list.stream().filter(n -> n.getPlace().equalsIgnoreCase(string)).collect(Collectors.toList());

	}

	public List<Product> findOutOfWarrantyProduct() {

		return list.stream().filter(n -> n.getWarranty() < Year.now().getValue()).collect(Collectors.toList());

	}

	public void updateProduct(int id, Product p) {

		db.updateProduct(id, p);
	}

	public HSSFWorkbook generateExcel() throws SQLException, IOException {

		ResultSet product = db.getProduct();

		HSSFWorkbook eb = new HSSFWorkbook();
		HSSFSheet sheet = eb.createSheet("ProductDetails");
		HSSFRow row = sheet.createRow(0);

		ResultSet header = db.getHeader();
		int cellNum_header = 0;
		while (header.next()) {
			System.out.println(header.getString(1));
			HSSFCell cell2 = row.createCell(cellNum_header);
			cell2.setCellValue(header.getString(1));
			cellNum_header++;
		}

		System.out.println("After setting getter row count is :  " + row.getRowNum());
		System.out.println(cellNum_header);
		int rowNum = row.getRowNum();
		while (product.next()) {
			HSSFRow row2 = sheet.createRow(rowNum + 1);
			for (int i = 0; i < cellNum_header; i++) {
				System.out.println(product.getObject(i + 1) instanceof Integer);
				if (product.getObject(i + 1) instanceof Integer) {
					row2.createCell(i).setCellValue(product.getInt(i + 1));
				} else {
					row2.createCell(i).setCellValue(product.getString(i + 1));
				}

			}
			rowNum++;
		}

		return eb;
	}

}
