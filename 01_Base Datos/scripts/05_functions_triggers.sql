-- 2. FUNCIONES Y TRIGGERS

-- Función A: Calcular totales de línea (detalle)
CREATE OR REPLACE FUNCTION fn_calcular_detalle_cotizacion()
RETURNS TRIGGER AS $$
BEGIN
    ------------------------------------------------------------------
    -- 1. Precio de venta unitario (Costo + Margen)
    ------------------------------------------------------------------
    NEW.precio_venta_unit :=
        ROUND(
            NEW.costo_unitario * (1 + (NEW.porcentaje_ganancia / 100.0)),
            4
        );

    ------------------------------------------------------------------
    -- 2. Utilidad unitaria
    ------------------------------------------------------------------
    NEW.utilidad_unitaria :=
        ROUND(
            NEW.precio_venta_unit - NEW.costo_unitario,
            4
        );

    ------------------------------------------------------------------
    -- 3. Totales de costo
    ------------------------------------------------------------------
    NEW.costo_total :=
        ROUND(
            NEW.cantidad * NEW.costo_unitario,
            2
        );

    ------------------------------------------------------------------
    -- 4. Subtotal de venta
    ------------------------------------------------------------------
    NEW.sub_total :=
        ROUND(
            NEW.cantidad * NEW.precio_venta_unit,
            2
        );

    ------------------------------------------------------------------
    -- 5. Utilidad bruta
    ------------------------------------------------------------------
    NEW.utilidad_bruta :=
        ROUND(
            NEW.sub_total - NEW.costo_total,
            2
        );

    ------------------------------------------------------------------
    -- 6. Impuesto
    ------------------------------------------------------------------
    NEW.impuesto_monto :=
        ROUND(
            NEW.sub_total * (NEW.impuesto_porcent / 100.0),
            2
        );

    ------------------------------------------------------------------
    -- 7. Total de línea (venta al cliente)
    ------------------------------------------------------------------
    NEW.total_linea :=
        ROUND(
            NEW.sub_total + NEW.impuesto_monto,
            2
        );

    ------------------------------------------------------------------
    -- 8. Inversión total con IGV (costo real al proveedor)
    ------------------------------------------------------------------
    NEW.inversion_total_inc_igv :=
        ROUND(
            NEW.costo_total * (1 + (NEW.impuesto_porcent / 100.0)),
            2
        );

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_calcular_detalle_cotizacion
BEFORE INSERT OR UPDATE ON cotizacion_detalle
FOR EACH ROW 
EXECUTE PROCEDURE fn_calcular_detalle_cotizacion();


-- Función B: Actualizar totales de cabecera
CREATE OR REPLACE FUNCTION fn_actualizar_totales_cotizacion(p_id_cotizacion BIGINT)
RETURNS VOID AS $$
DECLARE
    v_inversion_total        NUMERIC(14,2);
    v_utilidad_bruta_total   NUMERIC(14,2);
    v_sub_total              NUMERIC(14,2);
    v_monto_igv              NUMERIC(14,2);
    v_total_general          NUMERIC(14,2);
BEGIN
    SELECT
        COALESCE(SUM(inversion_total_inc_igv), 0),
        COALESCE(SUM(utilidad_bruta), 0),
        COALESCE(SUM(sub_total), 0),
        COALESCE(SUM(impuesto_monto), 0),
        COALESCE(SUM(total_linea), 0)
    INTO
        v_inversion_total,
        v_utilidad_bruta_total,
        v_sub_total,
        v_monto_igv,
        v_total_general
    FROM cotizacion_detalle
    WHERE id_cotizacion = p_id_cotizacion;

    UPDATE cotizacion
    SET
        inversion_total       = v_inversion_total,
        utilidad_bruta_total  = v_utilidad_bruta_total,
        sub_total             = v_sub_total,
        monto_igv             = v_monto_igv,
        total_general         = v_total_general
    WHERE id_cotizacion = p_id_cotizacion;
END;
$$ LANGUAGE plpgsql;



-- Función C: Trigger Wrapper para llamar a Función B tras cambios en detalle
CREATE OR REPLACE FUNCTION fn_trigger_actualizar_totales_cotizacion()
RETURNS TRIGGER AS $$
DECLARE
    v_id_cotizacion BIGINT;
BEGIN
    IF TG_OP = 'DELETE' THEN
        v_id_cotizacion := OLD.id_cotizacion;
    ELSE
        v_id_cotizacion := NEW.id_cotizacion;
    END IF;

    PERFORM fn_actualizar_totales_cotizacion(v_id_cotizacion);

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_actualizar_totales_cotizacion
AFTER INSERT OR UPDATE OR DELETE ON cotizacion_detalle
FOR EACH ROW
EXECUTE PROCEDURE fn_trigger_actualizar_totales_cotizacion();
