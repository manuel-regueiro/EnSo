package com.ejemplo.expendedoras.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanReposicion {
    private final List<ParadaRuta> ruta;
    private final Map<Producto, Integer> cargaDesdeBase;

    public PlanReposicion(List<ParadaRuta> ruta, Map<Producto, Integer> cargaDesdeBase) {
        this.ruta = List.copyOf(ruta);
        this.cargaDesdeBase = new HashMap<>(cargaDesdeBase);
    }

    public List<ParadaRuta> getRuta() {
        return Collections.unmodifiableList(ruta);
    }

    public Map<Producto, Integer> getCargaDesdeBase() {
        return Collections.unmodifiableMap(cargaDesdeBase);
    }
}
