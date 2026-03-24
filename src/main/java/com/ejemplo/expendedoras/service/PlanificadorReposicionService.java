package com.ejemplo.expendedoras.service;

import com.ejemplo.expendedoras.dao.MaquinaDao;
import com.ejemplo.expendedoras.model.Coordenada;
import com.ejemplo.expendedoras.model.MaquinaExpendedora;
import com.ejemplo.expendedoras.model.NecesidadReposicion;
import com.ejemplo.expendedoras.model.ParadaRuta;
import com.ejemplo.expendedoras.model.PlanReposicion;
import com.ejemplo.expendedoras.model.Producto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlanificadorReposicionService {
    private final MaquinaDao maquinaDao;
    private final GestorStockService gestorStockService;

    public PlanificadorReposicionService(MaquinaDao maquinaDao, GestorStockService gestorStockService) {
        this.maquinaDao = maquinaDao;
        this.gestorStockService = gestorStockService;
    }

    public PlanReposicion generarPlan(Coordenada base, Collection<String> idsMaquinas, int stockObjetivo) {
        if (base == null) {
            throw new IllegalArgumentException("La base es obligatoria");
        }
        List<MaquinaExpendedora> maquinas = idsMaquinas.stream()
                .map(id -> maquinaDao.buscarPorId(id)
                        .orElseThrow(() -> new IllegalArgumentException("No existe la máquina con id " + id)))
                .sorted(Comparator.comparingDouble(m -> m.getCoordenada().distanciaA(base)))
                .toList();

        List<ParadaRuta> ruta = new ArrayList<>();
        Map<Producto, Integer> cargaTotal = new LinkedHashMap<>();

        for (MaquinaExpendedora maquina : maquinas) {
            Map<Producto, NecesidadReposicion> necesidades = gestorStockService.obtenerNecesidadesReposicion(maquina.getId(), stockObjetivo);
            if (necesidades.isEmpty()) {
                continue;
            }
            Map<Producto, Integer> productosAReponer = new HashMap<>();
            for (NecesidadReposicion necesidad : necesidades.values()) {
                productosAReponer.put(necesidad.producto(), necesidad.cantidadAReponer());
                cargaTotal.merge(necesidad.producto(), necesidad.cantidadAReponer(), Integer::sum);
            }
            ruta.add(new ParadaRuta(maquina, productosAReponer));
        }
        return new PlanReposicion(ruta, cargaTotal);
    }

    public void ejecutarPlan(PlanReposicion plan) {
        for (ParadaRuta parada : plan.getRuta()) {
            MaquinaExpendedora maquina = maquinaDao.buscarPorId(parada.getMaquina().getId())
                    .orElseThrow(() -> new IllegalStateException("Máquina no encontrada durante la ejecución del plan"));
            for (Map.Entry<Producto, Integer> entry : parada.getProductosAReponer().entrySet()) {
                maquina.incrementarStock(entry.getKey(), entry.getValue());
            }
        }
    }
}
