package com.ejemplo.expendedoras.service;

import com.ejemplo.expendedoras.dao.MaquinaDaoMemoria;
import com.ejemplo.expendedoras.model.Coordenada;
import com.ejemplo.expendedoras.model.MaquinaExpendedora;
import com.ejemplo.expendedoras.model.PlanReposicion;
import com.ejemplo.expendedoras.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EjecucionPlanReposicionIntegrationTest {

    private MaquinaDaoMemoria maquinaDao;
    private GestorStockService gestorStockService;
    private PlanificadorReposicionService planificadorReposicionService;
    private Producto agua;
    private Producto snack;

    @BeforeEach
    void setUp() {
        maquinaDao = new MaquinaDaoMemoria();
        gestorStockService = new GestorStockService(maquinaDao);
        planificadorReposicionService = new PlanificadorReposicionService(maquinaDao, gestorStockService);

        agua = new Producto("P1", "Agua", 2);
        snack = new Producto("P2", "Snack", 1);

        MaquinaExpendedora m1 = new MaquinaExpendedora("M1", "Edificio A", new Coordenada(1, 1));
        m1.establecerStock(agua, 2);
        m1.establecerStock(snack, 1);

        MaquinaExpendedora m2 = new MaquinaExpendedora("M2", "Edificio B", new Coordenada(3, 4));
        m2.establecerStock(agua, 5);
        m2.establecerStock(snack, 0);

        maquinaDao.guardar(m1);
        maquinaDao.guardar(m2);
    }

    @Test
    @DisplayName("Debe ejecutar el plan y dejar las máquinas con el stock esperado")
    void debeEjecutarPlanYActualizarStock() {
        PlanReposicion plan = planificadorReposicionService.generarPlan(new Coordenada(0, 0), List.of("M1", "M2"), 6);

        planificadorReposicionService.ejecutarPlan(plan);

        assertEquals(6, maquinaDao.buscarPorId("M1").orElseThrow().consultarStock(agua));
        assertEquals(6, maquinaDao.buscarPorId("M1").orElseThrow().consultarStock(snack));
        assertEquals(6, maquinaDao.buscarPorId("M2").orElseThrow().consultarStock(snack));
        assertEquals(5, maquinaDao.buscarPorId("M2").orElseThrow().consultarStock(agua));
    }
}
