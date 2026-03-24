package com.ejemplo.expendedoras.model;

import java.util.Objects;

public class Producto {
    private final String id;
    private final String nombre;
    private final int umbralReposicion;

    public Producto(String id, String nombre, int umbralReposicion) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del producto no puede estar vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        if (umbralReposicion < 0) {
            throw new IllegalArgumentException("El umbral de reposición no puede ser negativo");
        }
        this.id = id;
        this.nombre = nombre;
        this.umbralReposicion = umbralReposicion;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getUmbralReposicion() {
        return umbralReposicion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto producto)) return false;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
