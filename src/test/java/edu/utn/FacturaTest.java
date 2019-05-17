package edu.utn;

import edu.utn.model.Factura;
import edu.utn.model.Item;
import junit.framework.TestCase;
import org.junit.Assert;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

public class FacturaTest extends TestCase {

    public void testAddItem() {
        Item expectedItem =
                Item.builder().
                        idItem(1).
                        cantidad(20.0).
                        precioUnitario(2.0).
                        producto("Harina").
                        build();

        Factura f = Factura.builder().
                fecha(LocalDate.of(2013, 4, 2)).
                idCliente(1).
                numero("10").
                idLiquidacion(1).
                build();

        f.addItem(1, "Harina", 20.0, 2.0);

        Assert.assertNotNull(f.getItems());
        Assert.assertEquals( 1 , f.getItems().size());
        Assert.assertEquals( expectedItem, f.getItems().get(0));

    }


    public void testGetTotalFactura() {
        //given
        Factura f = Factura.builder().
                fecha(LocalDate.of(2013, 4, 2)).
                idCliente(1).
                numero("10").
                idLiquidacion(1).
                build();

        f.addItem(1, "Harina", 20.0, 2.0);
        f.addItem(2, "Manzana", 50.0, 5.0);

        //when

        Double total =  f.getTotal();

        //then
        Assert.assertEquals(total, Double.valueOf(290.0));
    }


    public void testFake() {
        Factura factura = mock(Factura.class);
        factura.setNumero("1992");

        when(factura.getNumero()).thenReturn("223");
        when(factura.getTotal()).thenReturn(234.23);

        doNothing().when(factura).setNumero(anyString());
        factura.setNumero("223");
        System.out.println(factura.getNumero());
        System.out.println(factura.getTotal());

    }


}
