-- 2. TABLES

-- Maestros
CREATE TABLE categoria (
    id_categoria   BIGINT NOT NULL DEFAULT nextval('seq_categoria_auto_incr'),
    nombre         VARCHAR(100) NOT NULL,
    descripcion    TEXT
);

CREATE TABLE Persona (
    ID_PERSONA     INTEGER NOT NULL DEFAULT NEXTVAL('SEQ_PERSONA_AUTO_INCR'::REGCLASS),
    DNI            VARCHAR(20) NOT NULL,
    NOMBRE         VARCHAR(150) NOT NULL,
    TELEFONO       VARCHAR(20),
    EMAIL          VARCHAR(100)
);

CREATE TABLE proveedor (
    id_proveedor   BIGINT NOT NULL DEFAULT nextval('seq_proveedor_auto_incr'),
    nombre         VARCHAR(150) NOT NULL,
    ruc            VARCHAR(20),
    telefono       VARCHAR(30),
    email          VARCHAR(150),
    direccion      TEXT
);

CREATE TABLE cliente (
    id_cliente     BIGINT NOT NULL DEFAULT nextval('seq_cliente_auto_incr'::REGCLASS),
    nombre         VARCHAR(150) NOT NULL,
    ruc_dni        VARCHAR(20),
    telefono       VARCHAR(30),
    email          VARCHAR(150),
    direccion      TEXT
);

CREATE TABLE almacen (
    id_almacen     BIGINT NOT NULL DEFAULT nextval('seq_almacen_auto_incr'::REGCLASS),
    nombre         VARCHAR(100) NOT NULL,
    direccion      TEXT
);

CREATE TABLE moneda (
    id_moneda      BIGINT NOT NULL DEFAULT nextval('seq_moneda_auto_incr'::REGCLASS),
    codigo         VARCHAR(10) NOT NULL,
    descripcion    VARCHAR(50)
);

CREATE TABLE forma_pago (
    id_forma_pago  BIGINT NOT NULL DEFAULT nextval('seq_forma_pago_auto_incr'::REGCLASS),
    nombre         VARCHAR(100) NOT NULL,
    descripcion    TEXT
);

CREATE TABLE estado_cotizacion (
    id_estado      BIGINT NOT NULL DEFAULT nextval('seq_estado_cotizacion_auto_incr'::REGCLASS),
    nombre         VARCHAR(50) NOT NULL,
    descripcion    TEXT
);

-- Seguridad
CREATE TABLE Usuario (
    ID_USUARIO     INTEGER NOT NULL DEFAULT NEXTVAL('SEQ_USUARIO_AUTO_INCR'::REGCLASS),
    USERNAME       VARCHAR(50) NOT NULL,
    PASSWORD       VARCHAR(255) NOT NULL,
    ENABLE         BOOLEAN DEFAULT TRUE NOT NULL,
    PERSONA_ID     INT NOT NULL
);

CREATE TABLE Authority (
    ID_AUTHORITY   INTEGER NOT NULL DEFAULT NEXTVAL('SEQ_AUTHORITY_AUTO_INCR'::REGCLASS),
    TIPO           VARCHAR(50) NOT NULL,
    USUARIO_ID     INT NOT NULL
);

-- Negocio
CREATE TABLE producto (
    id_producto     BIGINT NOT NULL DEFAULT nextval('seq_producto_auto_incr'::REGCLASS),
    nombre          VARCHAR(150) NOT NULL,
    descripcion     TEXT,
    id_categoria    BIGINT,
    id_proveedor    BIGINT,
    precio_base     NUMERIC(12,2) NOT NULL DEFAULT 0,
    activo          BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE inventario (
    id_inventario   BIGINT NOT NULL DEFAULT nextval('seq_inventario_auto_incr'::REGCLASS),
    id_producto     BIGINT NOT NULL,
    id_almacen      BIGINT NOT NULL,
    stock_actual    NUMERIC(14,2) NOT NULL DEFAULT 0
);

/*
CREATE TABLE cotizacion (
    id_cotizacion       BIGINT NOT NULL DEFAULT nextval('seq_cotizacion_auto_incr'::REGCLASS),
    fecha               TIMESTAMP NOT NULL DEFAULT NOW(),
    id_cliente          BIGINT NOT NULL,
    id_usuario          INTEGER NOT NULL,
    id_moneda           BIGINT NOT NULL,
    id_forma_pago       BIGINT,
    id_estado           BIGINT NOT NULL,
    validez_dias        INT NOT NULL DEFAULT 7,
    observaciones       TEXT,
    total_bruto         NUMERIC(14,2) NOT NULL DEFAULT 0,
    total_impuestos     NUMERIC(14,2) NOT NULL DEFAULT 0,
    total_neto          NUMERIC(14,2) NOT NULL DEFAULT 0
);
*/

CREATE TABLE cotizacion (
    id_cotizacion       BIGINT NOT NULL DEFAULT nextval('seq_cotizacion_auto_incr'::REGCLASS),
    fecha               TIMESTAMP NOT NULL DEFAULT NOW(),
    id_cliente          BIGINT NOT NULL,
    id_usuario          INTEGER NOT NULL,
    id_moneda           BIGINT NOT NULL,
    id_forma_pago       BIGINT,
    id_estado           BIGINT NOT NULL,
    validez_dias        INT NOT NULL DEFAULT 7,
    observaciones       TEXT,
    
    -- Campos ajustados según el Footer de la Imagen
    inversion_total     NUMERIC(14,2) NOT NULL DEFAULT 0, -- "Inversion Total Inc IGV" (Costos acumulados)
    utilidad_bruta_total NUMERIC(14,2) NOT NULL DEFAULT 0, -- "Utilidad Bruta Total"
    sub_total           NUMERIC(14,2) NOT NULL DEFAULT 0, -- "Sub Total" (Suma de ventas sin impuesto)
    monto_igv           NUMERIC(14,2) NOT NULL DEFAULT 0, -- "IGV"
    total_general       NUMERIC(14,2) NOT NULL DEFAULT 0 -- "TOTAL" (SubTotal + IGV)    

);

/*
CREATE TABLE cotizacion_detalle (
    id_detalle          BIGINT NOT NULL DEFAULT nextval('seq_cotizacion_detalle_auto_incr'::REGCLASS),
    id_cotizacion       BIGINT NOT NULL,
    id_producto         BIGINT NOT NULL,
    descripcion_item    TEXT,
    cantidad            NUMERIC(12,2) NOT NULL,
    precio_unitario     NUMERIC(12,2) NOT NULL,
    porcentaje_desc     NUMERIC(5,2) NOT NULL DEFAULT 0,
    impuesto_porcent    NUMERIC(5,2) NOT NULL DEFAULT 18,
    subtotal            NUMERIC(14,2) NOT NULL,
    total_linea         NUMERIC(14,2) NOT NULL
);
*/

-- DETALLE (Items)
CREATE TABLE cotizacion_detalle (
    id_detalle              BIGINT NOT NULL DEFAULT nextval('seq_cotizacion_detalle_auto_incr'::REGCLASS),
    id_cotizacion           BIGINT NOT NULL,
    id_producto             BIGINT NOT NULL,
    
    --  NUEVO: Relación con Proveedor (Vital para saber de dónde sale el Costo)
    id_proveedor            BIGINT, 

    -- Campos visuales de la Grilla
    numero_linea            INT NOT NULL,                     -- Orden (1, 2, 3...)
    codigo_articulo         VARCHAR(50),                      -- Ej: '00.001'
    descripcion_item        TEXT,                             
    unidad_medida           VARCHAR(20),                      -- Ej: 'RL', 'UND'
    
    -- Métricas de Cantidad
    cantidad                NUMERIC(12,2) NOT NULL DEFAULT 1, 

    --  NUEVO: Campos para la lógica de Costos y Margen (Según tu imagen)
    costo_unitario          NUMERIC(12,4) NOT NULL DEFAULT 0, -- "Cost.Unit Prov"
    porcentaje_ganancia     NUMERIC(12,4) NOT NULL DEFAULT 0, -- "Margen Gan." (Ej: 30.00)
    
    -- Precios Unitarios Calculados
    utilidad_unitaria       NUMERIC(12,4) NOT NULL DEFAULT 0, -- "Util.Unit.V.Vta" (Precio - Costo)
    precio_venta_unit       NUMERIC(12,4) NOT NULL DEFAULT 0, -- "Val. Unit" (Costo * (1 + Margen))

    -- Totals Calculados por Fila
    costo_total             NUMERIC(14,2) NOT NULL DEFAULT 0, -- "Costo Total" (Cantidad * Costo Unit)
    sub_total               NUMERIC(14,2) NOT NULL DEFAULT 0, -- "Sub Total" (Cantidad * V.Venta)
    utilidad_bruta          NUMERIC(14,2) NOT NULL DEFAULT 0, -- "Utilidad Bruta" (Total Venta - Total Costo)

    --  NUEVO: Dato financiero para Compras (Según tu imagen "Inversion Inc IGV")
    inversion_total_inc_igv NUMERIC(14,2) NOT NULL DEFAULT 0, -- Cuánto debes pagar al proveedor con IGV incluido

    -- Impuestos (Cálculo final hacia el cliente)
    impuesto_porcent        NUMERIC(5,2) NOT NULL DEFAULT 18,
    impuesto_monto          NUMERIC(14,2) NOT NULL DEFAULT 0, 
    total_linea             NUMERIC(14,2) NOT NULL DEFAULT 0  -- SubTotal + Impuesto
);