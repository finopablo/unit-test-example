package edu.utn.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Factura {

    protected Integer idFactura;
    protected String numero;
    protected LocalDate fecha;
    protected Integer idCliente;
    protected Integer idLiquidacion;
    protected List<Item> items = new ArrayList<Item>();

    public Double getTotal() {
        return items.stream().mapToDouble(o -> o.getCantidad() * o.getPrecioUnitario()).sum();
    }

    public List<Item> getItems() {
        if (items == null) {
            items = new ArrayList<Item>();
        }
        return items;
    }

    public void addItem(Integer idItem, String producto, Double cantidad, Double precioUnitario) {
        this.getItems().add(Item.builder()
                .cantidad(cantidad)
                .idItem(idItem)
                .producto(producto)
                .precioUnitario(precioUnitario)
                .build());
    }
}
