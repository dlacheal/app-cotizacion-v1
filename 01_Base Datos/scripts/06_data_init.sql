-- =============================================
-- 1. MAESTROS GENERALES (Nivel 0)
-- =============================================

-- Categorias
INSERT INTO categoria (nombre, descripcion) VALUES 
('Tecnología', 'Equipos de cómputo y periféricos'),
('Mobiliario', 'Sillas, escritorios y estantes'),
('Servicios', 'Mano de obra, instalaciones y mantenimiento'),
('Suministros', 'Papelería y útiles de oficina');

-- Proveedores
INSERT INTO proveedor (nombre, ruc, telefono, email, direccion) VALUES 
('TechData Perú SAC', '20100000001', '01-555-1000', 'ventas@techdata.pe', 'Av. Javier Prado 123, Lima'),
('Muebles El Roble', '20200000002', '01-555-2000', 'contacto@elroble.com', 'Av. Industrial 456, Callao'),
('Servicios Generales JJ', '10300000003', '999888777', 'jorge@serviciosjj.com', 'Jr. Los Olivos 789, SMP'),
('Office Depot Corp', '20400000004', '01-555-4000', 'b2b@officedepot.com', 'Av. Benavides 1010, Miraflores');

-- Clientes
INSERT INTO cliente (nombre, ruc_dni, telefono, email, direccion) VALUES 
('Minera Los Andes SA', '20555555551', '01-444-1111', 'logistica@mineraandes.com', 'Carretera Central Km 100'),
('Estudio de Abogados Diaz', '20666666662', '01-444-2222', 'admin@diazlaw.com', 'Av. Canaval y Moreyra 500, San Isidro'),
('Juan Perez (Persona Natural)', '10777777773', '987654321', 'juan.perez@gmail.com', 'Calle Los Pinos 100, Lince'),
('Constructora Global SAC', '20888888884', '01-444-3333', 'compras@global.pe', 'Av. Arequipa 2020, Lince');

-- Almacenes
INSERT INTO almacen (nombre, direccion) VALUES 
('Almacén Principal', 'Zona Industrial Ventanilla Lt. 4'),
('Tienda San Isidro', 'Av. Rivera Navarrete 700'),
('Depósito Sur', 'Villa El Salvador Sec. 3'),
('Almacén de Tránsito', 'Aeropuerto Jorge Chavez - Cargo');

-- Monedas
INSERT INTO moneda (codigo, descripcion) VALUES 
('PEN', 'Soles'),
('USD', 'Dólares Americanos');

-- Formas de Pago
INSERT INTO forma_pago (nombre, descripcion) VALUES 
('Contado', 'Pago inmediato contra entrega'),
('Crédito 15 días', 'Factura a 15 días calendario'),
('Crédito 30 días', 'Factura a 30 días calendario'),
('Transferencia 50/50', '50% adelanto, 50% al finalizar');

-- Estados de Cotización
INSERT INTO estado_cotizacion (nombre, descripcion) VALUES 
('Borrador', 'Cotización en proceso de edición'),
('Enviada', 'Enviada al cliente, esperando respuesta'),
('Aprobada', 'Cliente aceptó la cotización'),
('Rechazada', 'Cliente desestimó la propuesta');

-- =============================================
-- 2. SEGURIDAD Y PERSONAS (Nivel 1)
-- =============================================

-- Personas (Base para usuarios)
INSERT INTO Persona (DNI, NOMBRE, TELEFONO, EMAIL) VALUES 
('00000001', 'Admin Sistema', '999000111', 'admin@empresa.com'),
('40000002', 'Carlos Vendedor', '999000222', 'carlos@empresa.com'),
('40000003', 'Ana Supervisora', '999000333', 'ana@empresa.com'),
('40000004', 'Luis Logistica', '999000444', 'luis@empresa.com');

-- Usuarios (Password simulado, en producción usar BCrypt)
INSERT INTO Usuario (USERNAME, PASSWORD, PERSONA_ID) VALUES 
('admin', '$2a$10$EixZaYVK1.zL5v.x.E/5/O...', 1), -- ID Persona 1
('vendedor1', '$2a$10$EixZaYVK1.zL5v.x.E/5/O...', 2), -- ID Persona 2
('supervisor', '$2a$10$EixZaYVK1.zL5v.x.E/5/O...', 3), -- ID Persona 3
('logistica', '$2a$10$EixZaYVK1.zL5v.x.E/5/O...', 4); -- ID Persona 4

-- Roles / Authority
INSERT INTO Authority (TIPO, USUARIO_ID) VALUES 
('ROLE_ADMIN', 1),
('ROLE_SELLER', 2),
('ROLE_MANAGER', 3),
('ROLE_USER', 4);

-- =============================================
-- 3. NEGOCIO: PRODUCTOS E INVENTARIO (Nivel 2)
-- =============================================

-- Productos
INSERT INTO producto (nombre, descripcion, id_categoria, id_proveedor, precio_base, activo) VALUES 
('Laptop Dell Latitude', 'Core i7, 16GB RAM, 512 SSD', 1, 1, 3500.00, TRUE), -- ID 1
('Silla Ergonómica Gerencial', 'Malla negra, soporte lumbar', 2, 2, 450.00, TRUE),  -- ID 2
('Instalación de Red (Punto)', 'Cableado estructurado Cat6', 3, 3, 120.00, TRUE),   -- ID 3
('Papel Bond A4 75gr', 'Caja por 10 millares', 4, 4, 180.00, TRUE);                 -- ID 4

-- Inventario (Stock inicial)
INSERT INTO inventario (id_producto, id_almacen, stock_actual) VALUES 
(1, 1, 10.00), -- 10 Laptops en Almacén Principal
(1, 2, 2.00),  -- 2 Laptops en Tienda San Isidro
(2, 1, 50.00), -- 50 Sillas en Almacén Principal
(4, 3, 100.00); -- 100 Cajas de papel en Depósito Sur
-- Nota: El servicio (ID 3) no suele tener stock físico, o es infinito.

-- =============================================
-- 4. TRANSACCIONAL: COTIZACIONES (Nivel 3)
-- =============================================

-- COTIZACION 1: Una venta de Laptops (Aprobada)
INSERT INTO cotizacion (id_cliente, id_usuario, id_moneda, id_forma_pago, id_estado, validez_dias, observaciones, inversion_total, utilidad_bruta_total, sub_total, monto_igv, total_general) 
VALUES 
(1, 2, 1, 3, 3, 15, 'Entrega inmediata en oficinas del cliente', 7000.00, 2800.00, 9800.00, 1764.00, 11564.00);
-- Explicación lógica Cot 1: 
-- 2 Laptops. Costo c/u 3500. Venta c/u 4900 (40% margen). 
-- Subtotal: 9800. IGV: 1764. Total: 11564.

-- COTIZACION 2: Mobiliario (Borrador)
INSERT INTO cotizacion (id_cliente, id_usuario, id_moneda, id_forma_pago, id_estado, validez_dias, observaciones, inversion_total, utilidad_bruta_total, sub_total, monto_igv, total_general) 
VALUES 
(2, 2, 1, 1, 1, 7, 'Pendiente confirmar color de sillas', 4500.00, 1800.00, 6300.00, 1134.00, 7434.00);
-- Explicación lógica Cot 2:
-- 10 Sillas. Costo c/u 450. Venta c/u 630.

-- COTIZACION 3: Servicios (Enviada)
INSERT INTO cotizacion (id_cliente, id_usuario, id_moneda, id_forma_pago, id_estado, validez_dias, observaciones, inversion_total, utilidad_bruta_total, sub_total, monto_igv, total_general) 
VALUES 
(3, 2, 2, 1, 2, 30, 'Servicio a todo costo', 600.00, 900.00, 1500.00, 270.00, 1770.00);
-- Explicación lógica Cot 3:
-- 5 Puntos de red. Costo c/u 120. Venta c/u 300 (Alto margen por servicio).

-- COTIZACION 4: Suministros (Rechazada)
INSERT INTO cotizacion (id_cliente, id_usuario, id_moneda, id_forma_pago, id_estado, validez_dias, observaciones, inversion_total, utilidad_bruta_total, sub_total, monto_igv, total_general) 
VALUES 
(4, 3, 1, 2, 4, 7, 'Precio muy alto según cliente', 3600.00, 360.00, 3960.00, 712.80, 4672.80);

-- =============================================
-- 5. DETALLE DE COTIZACIONES (Nivel 4)
-- =============================================
-- Nota: Los IDs de cotización se asumen 1, 2, 3, 4 por el orden de inserción anterior.

-- Detalles Cotizacion 1 (2 Laptops)
INSERT INTO cotizacion_detalle (
    id_cotizacion, id_producto, id_proveedor, numero_linea, codigo_articulo, descripcion_item, unidad_medida, 
    cantidad, costo_unitario, porcentaje_ganancia, utilidad_unitaria, precio_venta_unit, 
    costo_total, sub_total, utilidad_bruta, inversion_total_inc_igv, impuesto_porcent, impuesto_monto, total_linea
) VALUES (
    1, 1, 1, 1, 'DELL-001', 'Laptop Dell Latitude i7', 'UND', 
    2.00, 3500.00, 40.00, 1400.00, 4900.00, 
    7000.00, 9800.00, 2800.00, 8260.00, 18.00, 1764.00, 11564.00
);

-- Detalles Cotizacion 2 (10 Sillas)
INSERT INTO cotizacion_detalle (
    id_cotizacion, id_producto, id_proveedor, numero_linea, codigo_articulo, descripcion_item, unidad_medida, 
    cantidad, costo_unitario, porcentaje_ganancia, utilidad_unitaria, precio_venta_unit, 
    costo_total, sub_total, utilidad_bruta, inversion_total_inc_igv, impuesto_porcent, impuesto_monto, total_linea
) VALUES (
    2, 2, 2, 1, 'SILLA-GER', 'Silla Ergonómica Gerencial', 'UND', 
    10.00, 450.00, 40.00, 180.00, 630.00, 
    4500.00, 6300.00, 1800.00, 5310.00, 18.00, 1134.00, 7434.00
);

-- Detalles Cotizacion 3 (5 Puntos de Red)
INSERT INTO cotizacion_detalle (
    id_cotizacion, id_producto, id_proveedor, numero_linea, codigo_articulo, descripcion_item, unidad_medida, 
    cantidad, costo_unitario, porcentaje_ganancia, utilidad_unitaria, precio_venta_unit, 
    costo_total, sub_total, utilidad_bruta, inversion_total_inc_igv, impuesto_porcent, impuesto_monto, total_linea
) VALUES (
    3, 3, 3, 1, 'SERV-RED', 'Instalación de Punto de Red', 'PTO', 
    5.00, 120.00, 150.00, 180.00, 300.00, 
    600.00, 1500.00, 900.00, 708.00, 18.00, 270.00, 1770.00
);

-- Detalles Cotizacion 4 (20 Cajas de Papel)
INSERT INTO cotizacion_detalle (
    id_cotizacion, id_producto, id_proveedor, numero_linea, codigo_articulo, descripcion_item, unidad_medida, 
    cantidad, costo_unitario, porcentaje_ganancia, utilidad_unitaria, precio_venta_unit, 
    costo_total, sub_total, utilidad_bruta, inversion_total_inc_igv, impuesto_porcent, impuesto_monto, total_linea
) VALUES (
    4, 4, 4, 1, 'PAP-004', 'Papel Bond A4 75gr Caja x 10', 'CAJA', 
    20.00, 180.00, 10.00, 18.00, 198.00, 
    3600.00, 3960.00, 360.00, 4248.00, 18.00, 712.80, 4672.80
);