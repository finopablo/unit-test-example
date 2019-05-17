package edu.utn.dao;

import lombok.Builder;
import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Data
@Builder
public class ConnectionFactory {


    public  Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/facturacion", "root", "a");

    }
}
