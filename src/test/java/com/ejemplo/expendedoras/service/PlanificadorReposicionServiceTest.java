package com.ejemplo.expendedoras.service;

import com.ejemplo.expendedoras.dao.MaquinaDao;
import com.ejemplo.expendedoras.model.Coordenada;
import com.ejemplo.expendedoras.model.MaquinaExpendedora;
import com.ejemplo.expendedoras.model.NecesidadReposicion;
import com.ejemplo.expendedoras.model.PlanReposicion;
import com.ejemplo.expendedoras.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanificadorReposicionServiceTest {

    @Mock
    private MaquinaDao maquinaDao;

    @Mock
    private GestorStockService gestorStockService;

    @InjectMocks
    private PlanificadorReposicionService planificadorReposicionService;

    private Producto agua;
    private Producto refresco;
    private MaquinaExpendedora maquinaCercana;
    private MaquinaExpendedora maquinaLejana;

    @BeforeEach
    void setUp() {
        agua = new Producto("P1", "Agua", 2);
        refresco = new Producto("P2", "Refresco", 1);

        maquinaCercana = new MaquinaExpendedora("M1", "Biblioteca", new Coordenada(1, 1));
        maquinaLejana = new MaquinaExpendedora("M2", "Pabellón", new Coordenada(10, 10));
    }

    @Test
    @DisplayName("Debe generar una ruta ordenada por proximidad y calcular la carga total")
    void debeGenerarRutaOrdenadaYCalcularCarga() {
        when(maquinaDao.buscarPorId("M1")).thenReturn(Optional.of(maquinaCercana));
        when(maquinaDao.buscarPorId("M2")).thenReturn(Optional.of(maquinaLejana));

        when(gestorStockService.obtenerNecesidadesReposicion("M1", 8)).thenReturn(Map.of(
                agua, new NecesidadReposicion(agua, 2, 6)
        ));
        when(gestorStockService.obtenerNecesidadesReposicion("M2", 8)).thenReturn(Map.of(
                agua, new NecesidadReposicion(agua, 1, 7),
                refresco, new NecesidadReposicion(refresco, 0, 8)
        ));

        PlanReposicion plan = planificadorReposicionService.generarPlan(new Coordenada(0, 0), List.of("M2", "M1"), 8);

        assertEquals(2, plan.getRuta().size());
        assertEquals("M1", plan.getRuta().get(0).getMaquina().getId());
        assertEquals("M2", plan.getRuta().get(1).getMaquina().getId());
        assertEquals(13, plan.getCargaDesdeBase().get(agua));
        assertEquals(8, plan.getCargaDesdeBase().get(refresco));

        var orden = inOrder(maquinaDao, gestorStockService);
        orden.verify(maquinaDao).buscarPorId("M2");
        orden.verify(maquinaDao).buscarPorId("M1");
        verify(gestorStockService, times(1)).obtenerNecesidadesReposicion("M1", 8);
        verify(gestorStockService, times(1)).obtenerNecesidadesReposicion("M2", 8);
    }

    @Test
    @DisplayName("Debe ignorar máquinas sin necesidades de reposición")
    void debeIgnorarMaquinasSinNecesidades() {
        when(maquinaDao.buscarPorId("M1")).thenReturn(Optional.of(maquinaCercana));
        when(gestorStockService.obtenerNecesidadesReposicion("M1", 5)).thenReturn(Map.of());

        PlanReposicion plan = planificadorReposicionService.generarPlan(new Coordenada(0, 0), List.of("M1"), 5);

        assertTrue(plan.getRuta().isEmpty());
        assertTrue(plan.getCargaDesdeBase().isEmpty());
    }
}
