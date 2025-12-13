-- 3. CONSTRAINTS (PK, FK, UNIQUE, CHECK)

-- Categoria
ALTER TABLE categoria ADD CONSTRAINT pk_categoria PRIMARY KEY (id_categoria);
ALTER TABLE categoria ADD CONSTRAINT uq_categoria_nombre UNIQUE (nombre);

-- Persona
ALTER TABLE Persona ADD CONSTRAINT PERSONA_PK PRIMARY KEY (ID_PERSONA);
ALTER TABLE Persona ADD CONSTRAINT UNIQUE_PERSONA_DNI UNIQUE (DNI);
ALTER TABLE Persona ADD CONSTRAINT UNIQUE_PERSONA_EMAIL UNIQUE (EMAIL);

-- Proveedor
ALTER TABLE proveedor ADD CONSTRAINT pk_proveedor PRIMARY KEY (id_proveedor);

-- Cliente
ALTER TABLE cliente ADD CONSTRAINT pk_cliente PRIMARY KEY (id_cliente);

-- Almacen
ALTER TABLE almacen ADD CONSTRAINT pk_almacen PRIMARY KEY (id_almacen);

-- Moneda
ALTER TABLE moneda ADD CONSTRAINT pk_moneda PRIMARY KEY (id_moneda);
ALTER TABLE moneda ADD CONSTRAINT uq_moneda_codigo UNIQUE (codigo);

-- Forma Pago
ALTER TABLE forma_pago ADD CONSTRAINT pk_forma_pago PRIMARY KEY (id_forma_pago);

-- Estado Cotizacion
ALTER TABLE estado_cotizacion ADD CONSTRAINT pk_estado_cotizacion PRIMARY KEY (id_estado);
ALTER TABLE estado_cotizacion ADD CONSTRAINT uq_estado_cotizacion_nombre UNIQUE (nombre);

-- Usuario
ALTER TABLE Usuario ADD CONSTRAINT USUARIO_PK PRIMARY KEY (ID_USUARIO);
ALTER TABLE Usuario ADD CONSTRAINT UNIQUE_USUARIO_USERNAME UNIQUE (USERNAME);
ALTER TABLE Usuario ADD CONSTRAINT FK_USUARIO_PERSONA FOREIGN KEY (PERSONA_ID) REFERENCES Persona(ID_PERSONA);

-- Authority
ALTER TABLE Authority ADD CONSTRAINT AUTHORITY_PK PRIMARY KEY (ID_AUTHORITY);
ALTER TABLE Authority ADD CONSTRAINT UNIQUE_AUTHORITY_USUARIO_TIPO UNIQUE (USUARIO_ID, TIPO);
ALTER TABLE Authority ADD CONSTRAINT FK_AUTHORITY_USUARIO FOREIGN KEY (USUARIO_ID) REFERENCES Usuario(ID_USUARIO);

-- Producto
ALTER TABLE producto ADD CONSTRAINT pk_producto PRIMARY KEY (id_producto);
ALTER TABLE producto ADD CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria);
ALTER TABLE producto ADD CONSTRAINT fk_producto_proveedor FOREIGN KEY (id_proveedor) REFERENCES proveedor (id_proveedor);

-- Inventario
ALTER TABLE inventario ADD CONSTRAINT pk_inventario PRIMARY KEY (id_inventario);
ALTER TABLE inventario ADD CONSTRAINT uq_inventario_producto_almacen UNIQUE (id_producto, id_almacen);
ALTER TABLE inventario ADD CONSTRAINT fk_inventario_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto);
ALTER TABLE inventario ADD CONSTRAINT fk_inventario_almacen FOREIGN KEY (id_almacen) REFERENCES almacen (id_almacen);

-- Cotizacion Cabecera
ALTER TABLE cotizacion ADD CONSTRAINT pk_cotizacion PRIMARY KEY (id_cotizacion);
ALTER TABLE cotizacion ADD CONSTRAINT fk_cotizacion_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente);
ALTER TABLE cotizacion ADD CONSTRAINT fk_cotizacion_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario);
ALTER TABLE cotizacion ADD CONSTRAINT fk_cotizacion_moneda FOREIGN KEY (id_moneda) REFERENCES moneda (id_moneda);
ALTER TABLE cotizacion ADD CONSTRAINT fk_cotizacion_forma_pago FOREIGN KEY (id_forma_pago) REFERENCES forma_pago (id_forma_pago);
ALTER TABLE cotizacion ADD CONSTRAINT fk_cotizacion_estado FOREIGN KEY (id_estado) REFERENCES estado_cotizacion (id_estado);

-- Cotizacion Detalle
ALTER TABLE cotizacion_detalle ADD CONSTRAINT pk_cotizacion_detalle PRIMARY KEY (id_detalle);
ALTER TABLE cotizacion_detalle ADD CONSTRAINT fk_detalle_cotizacion FOREIGN KEY (id_cotizacion) REFERENCES cotizacion (id_cotizacion) ON DELETE CASCADE;
ALTER TABLE cotizacion_detalle ADD CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto);
ALTER TABLE cotizacion_detalle ADD CONSTRAINT chk_detalle_cantidad_pos CHECK (cantidad > 0);
ALTER TABLE cotizacion_detalle ADD CONSTRAINT chk_detalle_impuesto_porcent CHECK (impuesto_porcent >= 0 AND impuesto_porcent <= 100);

-- 4. INDICES
CREATE INDEX idx_producto_nombre ON producto (nombre);
CREATE INDEX idx_cotizacion_fecha ON cotizacion (fecha);
CREATE INDEX idx_cotizacion_cliente ON cotizacion (id_cliente);
CREATE INDEX idx_inventario_producto ON inventario (id_producto);
CREATE INDEX idx_inventario_almacen ON inventario (id_almacen);
