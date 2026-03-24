# Proyecto Maven: gestión de máquinas expendedoras (github)

Proyecto de ejemplo para prácticas de Ingeniería del Software orientadas a pruebas.

## Incluye

- Modelo de dominio para máquinas, productos y rutas de reposición.
- Persistencia simulada en memoria mediante DAO.
- Servicio de gestión de stock.
- Servicio de planificación de reposición por proximidad.
- Tests unitarios con JUnit 5.
- Tests con Mockito para aislar colaboradores.

## Ejecutar tests

```bash
mvn test
```

## Estructura principal

- `GestorStockService`: consulta inventario, registra ventas y detecta necesidades de reposición.
- `PlanificadorReposicionService`: genera planes de reposición y ejecuta la recarga de stock.
