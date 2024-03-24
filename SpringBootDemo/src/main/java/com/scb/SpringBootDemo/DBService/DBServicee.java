package com.scb.SpringBootDemo.DBService;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.scb.SpringBootDemo.entity.Product;

import jakarta.annotation.sql.DataSourceDefinition;

@Repository
@PropertySource("classpath:DB.properties")
public class DBServicee {

	@Value("${url}")
	private String url;

	@Value("${username}")
	private String username;

	@Value("${password}")
	private String password;

	public Connection conn;

	public DBServicee() {
		try {
			System.out.println(url);
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Product", "postgres", "Kaliraj@123");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add(Product p) {

		String query = "insert into product (id,name,type,place,warranty) values(?,?,?,?,?)";
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setObject(1, p.getId());
			st.setObject(2, p.getName());
			st.setObject(3, p.getType());
			st.setObject(4, p.getPlace());
			st.setObject(5, p.getWarranty());
			System.out.println(query);
			st.execute();
			System.out.println(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ResultSet getProduct() {
		String query = "select * from product";
		PreparedStatement st;
		ResultSet obj = null;
		try {
			st = conn.prepareStatement(query);
			obj = st.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;

	}

	public void updateProduct(int id, Product p) {

		String query = "UPDATE product SET name = ?, type = ?,place= ?,warranty=? WHERE id=?";

		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setObject(1, p.getName());
			st.setObject(2, p.getType());
			st.setObject(3, p.getPlace());
			st.setObject(4, p.getWarranty());
			st.setObject(5, id);
			System.out.println(query);
			st.execute();
			System.out.println(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ResultSet getHeader() throws SQLException {
		String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setObject(1, "product");
		return statement.executeQuery();
	}

}
