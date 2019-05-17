package edu.utn;

import edu.utn.dao.ConnectionFactory;
import edu.utn.dao.FacturaDao;
import edu.utn.model.Factura;
import edu.utn.model.Item;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class App
{

    private App()
    {

    }

    public static void main( String[] args )
    {

        FacturaDao facturaDao = new FacturaDao(ConnectionFactory.builder().build());
        try {
            List<Item> items =  List.of( Item.builder().precioUnitario(2.44).cantidad(22.0).producto("Producto1").build(), Item.builder().precioUnitario(1.40).cantidad(2.0).producto("Producto2").build());
            facturaDao.addFactura(Factura.builder().numero("222").idCliente(1).fecha(LocalDate.now()).idLiquidacion(null).items(items).build());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
