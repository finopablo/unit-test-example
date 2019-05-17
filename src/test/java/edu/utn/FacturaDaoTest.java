package edu.utn;

import edu.utn.dao.ConnectionFactory;
import edu.utn.dao.FacturaDao;
import edu.utn.model.Factura;
import junit.framework.TestCase;
import org.mockito.Captor;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static edu.utn.dao.FacturaDao.QUERY_INSERTAR_FACTURA;
import static edu.utn.dao.FacturaDao.QUERY_INSERTAR_ITEM;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FacturaDaoTest extends TestCase {


    //Constantes
    private final static String NUMERO = "220";
    private final static Integer ID_CLIENTE = 1;
    private final static LocalDate EXPECTED_DATE = LocalDate.now();
    private final static Integer ID_LIQUIDACION = 1;
    private static final Integer ID_FACTURA = 999;
    private static final String PRODUCTO_ITEM_1 = "Producto 1";
    private static final String PRODUCTO_ITEM_2 = "Producto 2";
    private static final Double CANTIDAD_ITEM_1 = 5.0;
    private static final Double CANTIDAD_ITEM_2 = 1.0;
    private static final Double PRECIO_ITEM_1 = 5.0;
    private static final Double PRECIO_ITEM_2 = 1.0;


    @Mock
    Connection connnection;
    @Mock
    ResultSet rsFactura;
    @Mock
    ConnectionFactory connectionFactory;

    @Mock
    PreparedStatement preparedStatementFactura;
    @Mock
    PreparedStatement preparedStatementItem;



    private FacturaDao facturaDao;

    private Factura factura;


    // Metodo para inicializar todo en cada uno de los test sin necesidad de escribirlo en cada paso
    public void setUp() {

        try {
            initMocks(this);

            when(connectionFactory.getConnection()).thenReturn(connnection);

            when(connnection.prepareStatement(eq(QUERY_INSERTAR_FACTURA), anyInt())).thenReturn(preparedStatementFactura);
            when(connnection.prepareStatement(eq(QUERY_INSERTAR_ITEM))).thenReturn(preparedStatementItem);


            when(preparedStatementFactura.getGeneratedKeys()).thenReturn(rsFactura);
            when(rsFactura.next()).thenReturn(true);

            when(rsFactura.getInt(1)).thenReturn(ID_FACTURA);

            facturaDao = new FacturaDao(connectionFactory);

            factura = Factura.builder().numero(NUMERO).idCliente(ID_CLIENTE).fecha(EXPECTED_DATE).idLiquidacion(ID_LIQUIDACION).build();
            factura.addItem(1, PRODUCTO_ITEM_1, CANTIDAD_ITEM_1, PRECIO_ITEM_1);
            factura.addItem(1, PRODUCTO_ITEM_2, CANTIDAD_ITEM_2, PRECIO_ITEM_2);

        } catch (SQLException | ClassNotFoundException e) {
            fail("Test failed");
        }

    }


    //Testeo del happy path -> Todo funcionando correctamente
    public void testAddFacturaOK() {
        try {

            //when
            facturaDao.addFactura(factura);

            //then
            verify(preparedStatementFactura, times(1)).setString(1, NUMERO);
            verify(preparedStatementFactura, times(1)).setInt(2, ID_CLIENTE);
            verify(preparedStatementFactura, times(1)).setDate(3, Date.valueOf(EXPECTED_DATE));
            verify(preparedStatementFactura, times(1)).executeUpdate();

            verify(preparedStatementItem, times(2)).setInt(eq(1), eq(ID_FACTURA));

            verify(preparedStatementItem, times(2)).setString(eq(2), anyString());
            verify(preparedStatementItem, times(1)).setString(2, PRODUCTO_ITEM_1);
            verify(preparedStatementItem, times(1)).setString(2, PRODUCTO_ITEM_2);

            verify(preparedStatementItem, times(2)).setDouble(eq(3), anyDouble());
            verify(preparedStatementItem, times(1)).setDouble(3, CANTIDAD_ITEM_1);
            verify(preparedStatementItem, times(1)).setDouble(3, PRECIO_ITEM_1);


            verify(preparedStatementItem, times(2)).setDouble(eq(4), anyDouble());
            verify(preparedStatementItem, times(1)).setDouble(4, CANTIDAD_ITEM_2);
            verify(preparedStatementItem, times(1)).setDouble(4, PRECIO_ITEM_2);

            verify(preparedStatementItem, times(2)).execute();

        } catch (Exception e) {

            fail("Test failed " + e.getMessage());
        }
    }


    //Testeo el error -> Hay un error al insertar la factura
    public void testAddFacturaSQLExceptionOnInsertarFactura() {
        try {
            //then
            //Cuando se ejecuta executeUpdate entonces da un SQL Exception por lo tanto hay que
            // capturar ese escenario
            when(preparedStatementFactura.executeUpdate()).thenThrow(SQLException.class);

            facturaDao.addFactura(factura);

            verify(connnection, times(1)).rollback();
            verify(preparedStatementItem, times(0)).executeQuery();

        } catch (Exception e) {

            fail("Test failed " + e.getMessage());
        }
    }


    //Testeo el error -> Hay un error al insertar la factura
    public void testAddFacturaSQLExceptionOnInsertarItem() {
        try {
            //then
            when(preparedStatementItem.execute()).thenThrow(SQLException.class);

            facturaDao.addFactura(factura);

            verify(connnection, times(1)).rollback();
            verify(preparedStatementItem, times(0)).executeQuery();

        } catch (Exception e) {

            fail("Test failed " + e.getMessage());
        }
    }

}