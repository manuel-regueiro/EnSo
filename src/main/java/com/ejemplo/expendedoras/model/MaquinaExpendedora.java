package com.ejemplo.expendedoras.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MaquinaExpendedora {
    private final String id;
    private final String nombre;
    private final Coordenada coordenada;
    private final Map<Producto, Integer> stockPorProducto;

    public MaquinaExpendedora(String id, String nombre, Coordenada coordenada) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de la máquina no puede estar vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la máquina no puede estar vacío");
        }
        this.id = id;
        this.nombre = nombre;
        this.coordenada = Objects.requireNonNull(coordenada, "La coordenada es obligatoria");
        this.stockPorProducto = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public Map<Producto, Integer> getStockPorProducto() {
        return Collections.unmodifiableMap(stockPorProducto);
    }

    public void establecerStock(Producto producto, int cantidad) {
        validarCantidad(cantidad);
        stockPorProducto.put(Objects.requireNonNull(producto), cantidad);
    }

    public int consultarStock(Producto producto) {
        return stockPorProducto.getOrDefault(producto, 0);
    }

    public void reducirStock(Producto producto, int cantidad) {
        validarCantidad(cantidad);
        int actual = consultarStock(producto);
        if (cantidad > actual) {
            throw new IllegalArgumentException("No hay stock suficiente para realizar la operación");
        }
        stockPorProducto.put(producto, actual - cantidad);
    }

    public void incrementarStock(Producto producto, int cantidad) {
        validarCantidad(cantidad);
        int actual = consultarStock(producto);
        stockPorProducto.put(producto, actual + cantidad);
    }

    private void validarCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
    }
}
