package com.ejemplo.expendedoras.service;

import com.ejemplo.expendedoras.dao.MaquinaDaoMemoria;
import com.ejemplo.expendedoras.model.Coordenada;
import com.ejemplo.expendedoras.model.MaquinaExpendedora;
import com.ejemplo.expendedoras.model.NecesidadReposicion;
import com.ejemplo.expendedoras.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GestorStockServiceTest {

    private MaquinaDaoMemoria maquinaDao;
    private GestorStockService gestorStockService;
    private Producto agua;
    private Producto snacks;

    @BeforeEach
    void setUp() {
        maquinaDao = new MaquinaDaoMemoria();
        gestorStockService = new GestorStockService(maquinaDao);

        agua = new Producto("P1", "Agua", 2);
        snacks = new Producto("P2", "Snacks", 1);

        MaquinaExpendedora maquina = new MaquinaExpendedora("M1", "Facultad Norte", new Coordenada(0, 0));
        maquina.establecerStock(agua, 5);
        maquina.establecerStock(snacks, 1);
        maquinaDao.guardar(maquina);
    }

    @Test
    @DisplayName("Debe consultar el inventario de una máquina")
    void debeConsultarInventario() {
        Map<Producto, Integer> inventario = gestorStockService.consultarInventario("M1");

        assertEquals(5, inventario.get(agua));
        assertEquals(1, inventario.get(snacks));
    }

    @Test
    @DisplayName("Debe actualizar el stock tras una venta")
    void debeActualizarStockTrasVenta() {
        gestorStockService.registrarVenta("M1", agua, 2);

        Map<Producto, Integer> inventario = gestorStockService.consultarInventario("M1");
        assertEquals(3, inventario.get(agua));
    }

    @Test
    @DisplayName("Debe detectar los productos que necesitan reposición")
    void debeDetectarNecesidadesDeReposicion() {
        Map<Producto, NecesidadReposicion> necesidades = gestorStockService.obtenerNecesidadesReposicion("M1", 6);

        assertTrue(necesidades.containsKey(snacks));
        assertEquals(1, necesidades.get(snacks).stockActual());
        assertEquals(5, necesidades.get(snacks).cantidadAReponer());
        assertTrue(!necesidades.containsKey(agua));
    }

    @Test
    @DisplayName("Debe fallar si se intenta vender más stock del disponible")
    void debeFallarSiNoHayStockSuficiente() {
        assertThrows(IllegalArgumentException.class,
                () -> gestorStockService.registrarVenta("M1", snacks, 3));
    }
}
