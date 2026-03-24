package com.ejemplo.expendedoras.dao;

import com.ejemplo.expendedoras.model.MaquinaExpendedora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MaquinaDaoMemoria implements MaquinaDao {
    private final Map<String, MaquinaExpendedora> almacenamiento = new HashMap<>();

    @Override
    public void guardar(MaquinaExpendedora maquina) {
        almacenamiento.put(maquina.getId(), maquina);
    }

    @Override
    public Optional<MaquinaExpendedora> buscarPorId(String id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public List<MaquinaExpendedora> buscarTodas() {
        return new ArrayList<>(almacenamiento.values());
    }
}
