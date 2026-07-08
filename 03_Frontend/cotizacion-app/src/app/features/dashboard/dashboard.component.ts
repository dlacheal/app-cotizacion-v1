import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CotizacionService } from '../cotizacion/services/cotizacion.service';

interface MonthlyData {
  month: string;
  total: number;
  approved: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  // Métricas Clave
  totalCotizado = 0;
  inversionTotal = 0;
  utilidadTotal = 0;
  margenUtilidad = 0;
  conversionRate = 0;

  // Totales por Estado
  approvedCount = 0;
  pendingCount = 0;
  rejectedCount = 0;
  totalQuotes = 0;

  // Totales por Moneda
  penTotal = 0;
  usdTotal = 0;
  penCount = 0;
  usdCount = 0;

  // Listados y Gráficos
  recentQuotes: any[] = [];
  monthlyTrend: MonthlyData[] = [];

  // Variables para trazados SVG dinámicos
  trendPath = '';
  trendAreaPath = '';
  trendPoints: { x: number; y: number; label: string; value: number }[] = [];
  
  // Alturas de barras para el gráfico de estados
  barApprovedHeight = 0;
  barPendingHeight = 0;
  barRejectedHeight = 0;
  
  // Porcentajes de Moneda
  penPercent = 0;
  usdPercent = 0;
  // SVG Donut dashes
  penStrokeDashoffset = 314.16;
  usdStrokeDashoffset = 314.16;

  constructor(
    private cotizacionService: CotizacionService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarDashboard();
  }

  cargarDashboard(): void {
    this.cotizacionService.listarTodasTodos().subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.procesarDatos(data);
        } else {
          this.procesarDatosMock();
        }
      },
      error: (err) => {
        console.warn('Backend offline o sin datos. Cargando datos demo...');
        this.procesarDatosMock();
      }
    });
  }

  procesarDatos(cotizaciones: any[]): void {
    // 1. Limpieza y ordenación inicial (Recientes: primeras 5 ordenadas por ID desc)
    const ordenadas = [...cotizaciones].sort((a, b) => b.idCotizacion - a.idCotizacion);
    this.recentQuotes = ordenadas.slice(0, 5);
    this.totalQuotes = cotizaciones.length;

    // Tipo de cambio simulado para consolidación
    const TC = 3.75;

    // Reset de variables
    let totalAcumuladoPEN = 0;
    let inversionAcumuladaPEN = 0;
    let utilidadAcumuladaPEN = 0;
    
    this.approvedCount = 0;
    this.pendingCount = 0;
    this.rejectedCount = 0;
    
    this.penTotal = 0;
    this.usdTotal = 0;
    this.penCount = 0;
    this.usdCount = 0;

    // Agrupación mensual inicial
    const meses = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    const trendMap = new Map<string, { total: number; approved: number }>();
    meses.forEach(m => trendMap.set(m, { total: 0, approved: 0 }));

    // 2. Iterar sobre todas las cotizaciones
    cotizaciones.forEach(c => {
      const isUsd = c.monedaCodigo === 'USD';
      const factor = isUsd ? TC : 1;
      
      const totalPEN = (c.totalGeneral || 0) * factor;
      const invPEN = (c.inversionTotal || 0) * factor;
      const utilPEN = (c.utilidadBrutaTotal || 0) * factor;

      totalAcumuladoPEN += totalPEN;
      inversionAcumuladaPEN += invPEN;
      utilidadAcumuladaPEN += utilPEN;

      // Monedas
      if (isUsd) {
        this.usdTotal += c.totalGeneral || 0;
        this.usdCount++;
      } else {
        this.penTotal += c.totalGeneral || 0;
        this.penCount++;
      }

      // Estados
      const estado = (c.estadoNombre || '').toUpperCase();
      if (estado === 'APROBADA' || estado === 'APROBADO') {
        this.approvedCount++;
      } else if (estado === 'RECHAZADA' || estado === 'RECHAZADO' || estado === 'VENCIDA') {
        this.rejectedCount++;
      } else {
        this.pendingCount++;
      }

      // Procesar fecha para tendencia mensual
      if (c.fecha) {
        const fechaObj = new Date(c.fecha);
        const mesNombre = meses[fechaObj.getMonth()];
        if (trendMap.has(mesNombre)) {
          const current = trendMap.get(mesNombre)!;
          current.total += totalPEN;
          if (estado === 'APROBADA' || estado === 'APROBADO') {
            current.approved += totalPEN;
          }
        }
      }
    });

    // 3. Asignación de KPIs
    this.totalCotizado = Math.round(totalAcumuladoPEN * 100) / 100;
    this.inversionTotal = Math.round(inversionAcumuladaPEN * 100) / 100;
    this.utilidadTotal = Math.round(utilidadAcumuladaPEN * 100) / 100;
    this.margenUtilidad = this.totalCotizado > 0 ? Math.round((this.utilidadTotal / this.totalCotizado) * 100) : 0;
    this.conversionRate = this.totalQuotes > 0 ? Math.round((this.approvedCount / this.totalQuotes) * 100) : 0;

    // 4. Formatear datos de tendencia
    this.monthlyTrend = meses.map(m => ({
      month: m,
      total: Math.round(trendMap.get(m)!.total),
      approved: Math.round(trendMap.get(m)!.approved)
    }));

    this.dibujarGraficos();
    this.cdr.detectChanges();
  }

  procesarDatosMock(): void {
    // Datos mockup simulados realistas para demostración
    const mockCotizaciones = [
      { idCotizacion: 101, clienteNombre: 'Ozinca Perú S.A.C', fecha: '2026-01-10T10:00:00', totalGeneral: 12000, inversionTotal: 8500, utilidadBrutaTotal: 3500, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' },
      { idCotizacion: 102, clienteNombre: 'Farmacia Magistral S.A.', fecha: '2026-02-15T11:30:00', totalGeneral: 4500, inversionTotal: 3100, utilidadBrutaTotal: 1400, estadoNombre: 'Rechazada', monedaCodigo: 'PEN' },
      { idCotizacion: 103, clienteNombre: 'Minera Las Bambas', fecha: '2026-03-05T09:15:00', totalGeneral: 28000, inversionTotal: 21000, utilidadBrutaTotal: 7000, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' },
      { idCotizacion: 104, clienteNombre: 'Constructora Galilea', fecha: '2026-04-20T14:45:00', totalGeneral: 8500, inversionTotal: 6000, utilidadBrutaTotal: 2500, estadoNombre: 'Borrador', monedaCodigo: 'USD' },
      { idCotizacion: 105, clienteNombre: 'Luz del Sur S.A.A.', fecha: '2026-05-18T16:00:00', totalGeneral: 15400, inversionTotal: 11200, utilidadBrutaTotal: 4200, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' },
      { idCotizacion: 106, clienteNombre: 'Sodimac Perú S.A.', fecha: '2026-06-22T10:20:00', totalGeneral: 3200, inversionTotal: 2400, utilidadBrutaTotal: 800, estadoNombre: 'Aprobada', monedaCodigo: 'USD' },
      { idCotizacion: 107, clienteNombre: 'Alicorp S.A.A.', fecha: '2026-07-02T13:10:00', totalGeneral: 9800, inversionTotal: 7200, utilidadBrutaTotal: 2600, estadoNombre: 'Borrador', monedaCodigo: 'PEN' },
      { idCotizacion: 108, clienteNombre: 'Pacasmayo Cementos', fecha: '2026-07-05T15:40:00', totalGeneral: 18500, inversionTotal: 13500, utilidadBrutaTotal: 5000, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' }
    ];

    this.procesarDatos(mockCotizaciones);
  }

  dibujarGraficos(): void {
    // --- 1. Gráfico de Línea SVG (Tendencia Mensual) ---
    // Dimensiones de la caja de gráfico: width=480, height=180
    const maxVal = Math.max(...this.monthlyTrend.map(d => d.total), 5000);
    const chartHeight = 140; // Altura efectiva
    const chartWidth = 440;
    const paddingLeft = 30;
    const paddingTop = 20;

    this.trendPoints = this.monthlyTrend.map((d, index) => {
      const x = paddingLeft + (index * (chartWidth / 11));
      const y = paddingTop + chartHeight - ((d.total / maxVal) * chartHeight);
      return { x, y, label: d.month, value: d.total };
    });

    // Construir trazado para la línea (curva de tendencia)
    let path = '';
    let areaPath = '';

    if (this.trendPoints.length > 0) {
      // Línea normal
      path = `M ${this.trendPoints[0].x} ${this.trendPoints[0].y}`;
      for (let i = 1; i < this.trendPoints.length; i++) {
        // Trazado cúbico/suavizado simplificado
        const cpX1 = this.trendPoints[i - 1].x + 15;
        const cpY1 = this.trendPoints[i - 1].y;
        const cpX2 = this.trendPoints[i].x - 15;
        const cpY2 = this.trendPoints[i].y;
        path += ` C ${cpX1} ${cpY1}, ${cpX2} ${cpY2}, ${this.trendPoints[i].x} ${this.trendPoints[i].y}`;
      }
      this.trendPath = path;

      // Área con degradado (cierra el trazado abajo)
      const lastPt = this.trendPoints[this.trendPoints.length - 1];
      const firstPt = this.trendPoints[0];
      const bottomY = paddingTop + chartHeight;
      areaPath = `${path} L ${lastPt.x} ${bottomY} L ${firstPt.x} ${bottomY} Z`;
      this.trendAreaPath = areaPath;
    }

    // --- 2. Gráfico de Barras SVG (Estados) ---
    const maxCount = Math.max(this.approvedCount, this.pendingCount, this.rejectedCount) || 1;
    this.barApprovedHeight = Math.round((this.approvedCount / maxCount) * 80);
    this.barPendingHeight = Math.round((this.pendingCount / maxCount) * 80);
    this.barRejectedHeight = Math.round((this.rejectedCount / maxCount) * 80);

    // --- 3. Gráfico Donut SVG (Distribución Monedas) ---
    const totalCountMonedas = this.penCount + this.usdCount || 1;
    this.penPercent = Math.round((this.penCount / totalCountMonedas) * 100);
    this.usdPercent = Math.round((this.usdCount / totalCountMonedas) * 100);

    // Circunferencia del Donut de radio 50 es 2 * Math.PI * 50 = 314.16
    const totalCircumference = 314.16;
    this.penStrokeDashoffset = totalCircumference - (totalCircumference * (this.penPercent / 100));
    // La segunda porción se gira mediante CSS transform: rotate() o directamente acumulando
    this.usdStrokeDashoffset = totalCircumference - (totalCircumference * (this.usdPercent / 100));
  }
}
