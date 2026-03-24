package com.ejemplo.expendedoras.dao;

import com.ejemplo.expendedoras.model.MaquinaExpendedora;

import java.util.List;
import java.util.Optional;

public interface MaquinaDao {
    void guardar(MaquinaExpendedora maquina);
    Optional<MaquinaExpendedora> buscarPorId(String id);
    List<MaquinaExpendedora> buscarTodas();
}
