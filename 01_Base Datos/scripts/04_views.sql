-- 1. VISTAS

-- Vista de cabecera
CREATE OR REPLACE VIEW vw_cotizacion_cabecera AS
SELECT 
    -- Identificadores y Fechas
    c.id_cotizacion,
    c.fecha,
    
    -- Datos del Cliente (Agregué RUC/DNI y Email para evitar ambigüedades)
    cli.nombre AS cliente_nombre,
    cli.ruc_dni AS cliente_documento,
    cli.email AS cliente_email,
    
    -- Datos del Vendedor/Usuario
    u.USERNAME AS vendedor_usuario,
    
    -- Datos Financieros y Moneda
    m.codigo AS moneda,
    c.sub_total,
    c.monto_igv,
    c.total_general,
    
    -- Datos de Rentabilidad (Importante para Dashboard de Gerencia)
    c.inversion_total,
    c.utilidad_bruta_total,
    
    -- Estado y Condiciones
    ec.nombre AS estado,
    COALESCE(fp.nombre, 'Por definir') AS forma_pago, -- COALESCE por si es NULL
    c.validez_dias,
    c.observaciones

FROM cotizacion c
    INNER JOIN cliente cli ON c.id_cliente = cli.id_cliente
    INNER JOIN Usuario u ON c.id_usuario = u.ID_USUARIO
    INNER JOIN moneda m ON c.id_moneda = m.id_moneda
    INNER JOIN estado_cotizacion ec ON c.id_estado = ec.id_estado
    LEFT JOIN forma_pago fp ON c.id_forma_pago = fp.id_forma_pago;

-- Vista detalle
CREATE OR REPLACE VIEW vw_cotizacion_detalle AS
SELECT 
    -- IDs de Enlace
    cd.id_detalle,
    cd.id_cotizacion,
    cd.id_producto,
    
    -- Información Visual y de Ordenamiento
    cd.numero_linea,                -- Vital para ordenar la grilla (1, 2, 3...)
    cat.nombre AS categoria,        -- Útil para agrupar en reportes
    cd.codigo_articulo,
    p.nombre AS producto_original,  -- Nombre base del catálogo
    cd.descripcion_item,            -- Nombre editado/personalizado para la cotización
    cd.unidad_medida,
    
    -- Métricas de Cantidad y Costos (Para análisis interno)
    cd.cantidad,
    cd.costo_unitario,              -- Costo del proveedor
    cd.inversion_total_inc_igv,     -- Cuánto dinero saldrá de caja para comprar esto
    
    -- Métricas de Venta y Márgenes
    cd.porcentaje_ganancia,         -- El margen aplicado
    cd.utilidad_unitaria,           -- Ganancia por unidad
    cd.precio_venta_unit,           -- Precio final al cliente (sin IGV)
    
    -- Totales Financieros de la Línea
    cd.costo_total,                 -- Costo acumulado de la línea
    cd.sub_total,                   -- Venta acumulada sin impuestos
    cd.impuesto_monto AS igv_linea, -- Monto del impuesto
    cd.total_linea,                 -- Venta final con impuestos
    cd.utilidad_bruta               -- Ganancia neta de esta línea

FROM cotizacion_detalle cd
    -- JOINs Estructurales
    INNER JOIN cotizacion c ON cd.id_cotizacion = c.id_cotizacion
    INNER JOIN producto p ON cd.id_producto = p.id_producto
    
    -- LEFT JOIN para Categoría (Por si el producto no tiene categoría asignada)
    LEFT JOIN categoria cat ON p.id_categoria = cat.id_categoria;

-- Vista inventario
CREATE OR REPLACE VIEW vw_inventario_producto_almacen AS
SELECT
    -- Identificadores (Útiles para navegar o filtrar en el sistema)
    i.id_inventario,
    p.id_producto,
    a.id_almacen,
    p.id_categoria, -- Útil para filtros rápidos (ej. "Ver stock de Gasfitería")

    -- Detalles del Producto
    p.nombre AS producto,
    p.descripcion AS producto_descripcion,
    p.precio_base,             -- Referencia del costo/precio base
    p.activo AS producto_activo, -- CRÍTICO: Para no ofrecer productos descontinuados

    -- Detalles del Almacén (Logística)
    a.nombre AS almacen,
    a.direccion AS almacen_direccion, -- Ayuda a saber dónde ir a buscarlo

    -- Métricas de Inventario
    i.stock_actual,
    
    -- Valorización (Opcional pero recomendado: Cuánto dinero hay en stock)
    CAST(i.stock_actual * p.precio_base AS NUMERIC(14,2)) AS valor_estimado_stock

FROM inventario i
    INNER JOIN producto p ON i.id_producto = p.id_producto
    INNER JOIN almacen a ON i.id_almacen = a.id_almacen;