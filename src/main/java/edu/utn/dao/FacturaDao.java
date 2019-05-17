package edu.utn.dao;

import edu.utn.model.Factura;
import edu.utn.model.Item;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Types;

@Data
public class FacturaDao {

    public static final String QUERY_INSERTAR_FACTURA = "insert into facturas(numero,id_cliente, fecha, id_liquidacion) values (?,?,?,?)";
    public static final String QUERY_INSERTAR_ITEM = "insert into items(id_factura, producto, precio_unitario, cantidad) values (?,?,?,?)";

    private Connection connection;


    public FacturaDao(ConnectionFactory connectionFactory) {
        try {
            connection = connectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void addFactura(Factura factura) throws SQLException {
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(QUERY_INSERTAR_FACTURA, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, factura.getNumero());
            statement.setInt(2, factura.getIdCliente());
            statement.setDate(3, Date.valueOf(factura.getFecha()));

            if (factura.getIdLiquidacion() != null) {
                statement.setInt(4, factura.getIdLiquidacion());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            statement.executeUpdate();


            Integer idFactura = getIdFactura(statement);

            for (Item item : factura.getItems()) {
                addItem(item, idFactura);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }

    }


    private Integer getIdFactura(Statement statement) throws SQLException {

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("No se genero un id factura");
    }


    private void addItem(Item item, int idFactura) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(QUERY_INSERTAR_ITEM);
        ps.setInt(1, idFactura);
        ps.setString(2, item.getProducto());
        ps.setDouble(3, item.getPrecioUnitario());
        ps.setDouble(4, item.getCantidad());
        ps.execute();
    }
}


