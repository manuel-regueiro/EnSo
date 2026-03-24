package com.ejemplo.expendedoras.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParadaRuta {
    private final MaquinaExpendedora maquina;
    private final Map<Producto, Integer> productosAReponer;

    public ParadaRuta(MaquinaExpendedora maquina, Map<Producto, Integer> productosAReponer) {
        this.maquina = maquina;
        this.productosAReponer = new HashMap<>(productosAReponer);
    }

    public MaquinaExpendedora getMaquina() {
        return maquina;
    }

    public Map<Producto, Integer> getProductosAReponer() {
        return Collections.unmodifiableMap(productosAReponer);
    }
}
