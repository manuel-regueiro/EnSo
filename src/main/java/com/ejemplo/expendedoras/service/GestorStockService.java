package com.ejemplo.expendedoras.service;

import com.ejemplo.expendedoras.dao.MaquinaDao;
import com.ejemplo.expendedoras.model.MaquinaExpendedora;
import com.ejemplo.expendedoras.model.NecesidadReposicion;
import com.ejemplo.expendedoras.model.Producto;

import java.util.LinkedHashMap;
import java.util.Map;

public class GestorStockService {
    private final MaquinaDao maquinaDao;

    public GestorStockService(MaquinaDao maquinaDao) {
        this.maquinaDao = maquinaDao;
    }

    public Map<Producto, Integer> consultarInventario(String idMaquina) {
        return obtenerMaquina(idMaquina).getStockPorProducto();
    }

    public void registrarVenta(String idMaquina, Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser positiva");
        }
        MaquinaExpendedora maquina = obtenerMaquina(idMaquina);
        maquina.reducirStock(producto, cantidad);
    }

    public Map<Producto, NecesidadReposicion> obtenerNecesidadesReposicion(String idMaquina, int stockObjetivo) {
        if (stockObjetivo < 0) {
            throw new IllegalArgumentException("El stock objetivo no puede ser negativo");
        }
        MaquinaExpendedora maquina = obtenerMaquina(idMaquina);
        Map<Producto, NecesidadReposicion> resultado = new LinkedHashMap<>();

        for (Map.Entry<Producto, Integer> entry : maquina.getStockPorProducto().entrySet()) {
            Producto producto = entry.getKey();
            int stockActual = entry.getValue();
            if (stockActual <= producto.getUmbralReposicion()) {
                int cantidadAReponer = Math.max(0, stockObjetivo - stockActual);
                resultado.put(producto, new NecesidadReposicion(producto, stockActual, cantidadAReponer));
            }
        }
        return resultado;
    }

    private MaquinaExpendedora obtenerMaquina(String idMaquina) {
        return maquinaDao.buscarPorId(idMaquina)
                .orElseThrow(() -> new IllegalArgumentException("No existe la máquina con id " + idMaquina));
    }
}
