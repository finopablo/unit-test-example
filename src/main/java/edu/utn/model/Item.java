package edu.utn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Item {
    private Integer idItem;
    private String producto;
    private Double precioUnitario;
    private Double cantidad;

    void aa() {
        int a = idItem + 1;
    }
}
