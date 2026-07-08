import { Component, OnInit, ChangeDetectorRef, HostListener, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CotizacionService } from '../cotizacion/services/cotizacion.service';
import { ClienteService } from '../cliente/services/cliente.service';
import { MonedaService } from '../moneda/services/moneda.service';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './reportes.component.html'
})
export class ReportesComponent implements OnInit {

  // Inputs de Filtros
  fechaInicio = '';
  fechaFin = '';
  clienteNombreFiltro = '';
  estado = '';
  moneda = '';

  // Control del Combobox con Filtro de Cliente
  busquedaClienteQuery = '';
  mostrarDropdownCliente = false;

  // Origen de Datos
  cotizaciones: any[] = [];
  filteredCotizaciones: any[] = [];
  clientes: any[] = [];
  monedas: any[] = [];
  estadosList: any[] = [];

  // Acumulados filtrados (en Soles consolidados)
  totalCotizadoPEN = 0;
  inversionTotalPEN = 0;
  utilidadTotalPEN = 0;
  totalFiltrado = 0;

  constructor(
    private cotizacionService: CotizacionService,
    private clienteService: ClienteService,
    private monedaService: MonedaService,
    private cdr: ChangeDetectorRef,
    private elementRef: ElementRef
  ) { }

  ngOnInit(): void {
    this.inicializarFechas();
    this.cargarDatos();
  }

  inicializarFechas(): void {
    this.fechaInicio = '';
    this.fechaFin = '';
  }

  formatDate(date: Date): string {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  cargarDatos(): void {
    // 1. Cargar Clientes
    this.clienteService.getClientesTodos().subscribe({
      next: (cliData) => {
        this.clientes = cliData || [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.warn('Error al cargar clientes, inyectando mock...');
        this.clientes = [
          { idCliente: 1, nombre: 'Ozinca Perú S.A.C' },
          { idCliente: 2, nombre: 'Farmacia Magistral S.A.' },
          { idCliente: 3, nombre: 'Minera Las Bambas' },
          { idCliente: 4, nombre: 'Constructora Galilea' },
          { idCliente: 5, nombre: 'Luz del Sur S.A.A.' },
          { idCliente: 6, nombre: 'Sodimac Perú S.A.' },
          { idCliente: 7, nombre: 'Alicorp S.A.A.' },
          { idCliente: 8, nombre: 'Pacasmayo Cementos' }
        ];
        this.cdr.detectChanges();
      }
    });

    // 2. Cargar Monedas de la BD
    this.monedaService.getMonedasTodas().subscribe({
      next: (monData) => {
        this.monedas = monData || [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.warn('Error al cargar monedas, inyectando mock...');
        this.monedas = [
          { idMoneda: 1, codigo: 'PEN', descripcion: 'Soles' },
          { idMoneda: 2, codigo: 'USD', descripcion: 'Dólares' }
        ];
        this.cdr.detectChanges();
      }
    });

    // 3. Cargar Estados de Cotización de la BD
    this.cotizacionService.getEstados().subscribe({
      next: (estData) => {
        this.estadosList = estData || [];
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.warn('Error al cargar estados, inyectando mock...');
        this.estadosList = [
          { idEstado: 1, nombre: 'Aprobada' },
          { idEstado: 2, nombre: 'Borrador' },
          { idEstado: 3, nombre: 'Rechazada' },
          { idEstado: 4, nombre: 'Enviada' }
        ];
        this.cdr.detectChanges();
      }
    });

    // 4. Cargar Cotizaciones
    this.cotizacionService.listarTodasTodos().subscribe({
      next: (cotData) => {
        if (cotData && cotData.length > 0) {
          this.cotizaciones = cotData;
        } else {
          this.cotizaciones = this.getMockCotizaciones();
        }
        this.aplicarFiltros();
      },
      error: (err) => {
        console.warn('Error al cargar cotizaciones, inyectando mock...');
        this.cotizaciones = this.getMockCotizaciones();
        this.aplicarFiltros();
      }
    });
  }

  getMockCotizaciones(): any[] {
    return [
      { idCotizacion: 101, clienteId: 1, clienteNombre: 'Ozinca Perú S.A.C', fecha: '2026-07-01T10:00:00', totalGeneral: 12000, inversionTotal: 8500, utilidadBrutaTotal: 3500, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' },
      { idCotizacion: 102, clienteId: 2, clienteNombre: 'Farmacia Magistral S.A.', fecha: '2026-07-02T11:30:00', totalGeneral: 4500, inversionTotal: 3100, utilidadBrutaTotal: 1400, estadoNombre: 'Rechazada', monedaCodigo: 'PEN' },
      { idCotizacion: 103, clienteId: 3, clienteNombre: 'Minera Las Bambas', fecha: '2026-07-03T09:15:00', totalGeneral: 28000, inversionTotal: 21000, utilidadBrutaTotal: 7000, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' },
      { idCotizacion: 104, clienteId: 4, clienteNombre: 'Constructora Galilea', fecha: '2026-07-04T14:45:00', totalGeneral: 8500, inversionTotal: 6000, utilidadBrutaTotal: 2500, estadoNombre: 'Borrador', monedaCodigo: 'USD' },
      { idCotizacion: 105, clienteId: 5, clienteNombre: 'Luz del Sur S.A.A.', fecha: '2026-07-05T16:00:00', totalGeneral: 15400, inversionTotal: 11200, utilidadBrutaTotal: 4200, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' },
      { idCotizacion: 106, clienteId: 6, clienteNombre: 'Sodimac Perú S.A.', fecha: '2026-07-06T10:20:00', totalGeneral: 3200, inversionTotal: 2400, utilidadBrutaTotal: 800, estadoNombre: 'Aprobada', monedaCodigo: 'USD' },
      { idCotizacion: 107, clienteId: 7, clienteNombre: 'Alicorp S.A.A.', fecha: '2026-07-07T13:10:00', totalGeneral: 9800, inversionTotal: 7200, utilidadBrutaTotal: 2600, estadoNombre: 'Borrador', monedaCodigo: 'PEN' },
      { idCotizacion: 108, clienteId: 8, clienteNombre: 'Pacasmayo Cementos', fecha: '2026-07-08T15:40:00', totalGeneral: 18500, inversionTotal: 13500, utilidadBrutaTotal: 5000, estadoNombre: 'Aprobada', monedaCodigo: 'PEN' }
    ];
  }

  aplicarFiltros(): void {
    const TC = 3.75;
    
    this.filteredCotizaciones = this.cotizaciones.filter(c => {
      // Filtro de fecha (comparación directa de cadenas yyyy-MM-dd)
      if (c.fecha) {
        const dateStr = c.fecha.substring(0, 10);
        if (this.fechaInicio && dateStr < this.fechaInicio) return false;
        if (this.fechaFin && dateStr > this.fechaFin) return false;
      }

      // Filtro de Cliente (búsqueda de texto predictiva/parcial)
      if (this.clienteNombreFiltro) {
        const query = this.clienteNombreFiltro.toLowerCase();
        const cliName = (c.clienteNombre || '').toLowerCase();
        if (!cliName.includes(query)) return false;
      }

      // Filtro de Estado
      if (this.estado) {
        const estNormalizado = c.estadoNombre?.toUpperCase() || '';
        const filNormalizado = this.estado.toUpperCase();
        if (!estNormalizado.includes(filNormalizado)) return false;
      }

      // Filtro de Moneda
      if (this.moneda) {
        if (c.monedaCodigo !== this.moneda) return false;
      }

      return true;
    });

    // Calcular acumulados consolidados
    let totalAcumuladoPEN = 0;
    let inversionAcumuladaPEN = 0;
    let utilidadAcumuladaPEN = 0;

    this.filteredCotizaciones.forEach(c => {
      const isUsd = c.monedaCodigo === 'USD';
      const factor = isUsd ? TC : 1;

      totalAcumuladoPEN += (c.totalGeneral || 0) * factor;
      inversionAcumuladaPEN += (c.inversionTotal || 0) * factor;
      utilidadAcumuladaPEN += (c.utilidadBrutaTotal || 0) * factor;
    });

    this.totalCotizadoPEN = Math.round(totalAcumuladoPEN * 100) / 100;
    this.inversionTotalPEN = Math.round(inversionAcumuladaPEN * 100) / 100;
    this.utilidadTotalPEN = Math.round(utilidadAcumuladaPEN * 100) / 100;
    this.totalFiltrado = this.filteredCotizaciones.length;

    this.cdr.detectChanges();
  }

  limpiarFiltros(): void {
    this.inicializarFechas();
    this.clienteNombreFiltro = '';
    this.busquedaClienteQuery = '';
    this.mostrarDropdownCliente = false;
    this.estado = '';
    this.moneda = '';
    this.aplicarFiltros();
  }

  toggleDropdownCliente(): void {
    this.mostrarDropdownCliente = !this.mostrarDropdownCliente;
    if (this.mostrarDropdownCliente) {
      this.busquedaClienteQuery = '';
    }
  }

  seleccionarCliente(nombre: string): void {
    this.clienteNombreFiltro = nombre;
    this.mostrarDropdownCliente = false;
    this.aplicarFiltros();
  }

  get clientesFiltrados(): any[] {
    if (!this.busquedaClienteQuery) return this.clientes;
    const query = this.busquedaClienteQuery.toLowerCase();
    return this.clientes.filter(c => (c.nombre || '').toLowerCase().includes(query));
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.mostrarDropdownCliente = false;
      this.cdr.detectChanges();
    }
  }

  getClienteNombre(idCliente: any): string {
    if (!idCliente) return 'Todos';
    const cli = this.clientes.find(c => c.idCliente === Number(idCliente));
    return cli ? cli.nombre : 'Todos';
  }

  exportarPDF(): void {
    const doc = new jsPDF();

    // 1. Cabecera Corporativa
    doc.setFillColor(15, 23, 42); // Navy Slate
    doc.rect(0, 0, 210, 40, 'F');

    doc.setTextColor(255, 255, 255);
    doc.setFont("helvetica", "bold");
    doc.setFontSize(20);
    doc.text("REPORTE EJECUTIVO DE COTIZACIONES", 14, 18);

    doc.setFont("helvetica", "normal");
    doc.setFontSize(10);
    doc.text(`Fecha de Emisión: ${new Date().toLocaleDateString()} | Generado por: Antigravity-live`, 14, 25);
    
    // Parámetros de filtros aplicados en cabecera
    const nombreCliente = this.clienteNombreFiltro || 'Todos';
    const textFiltros = `Filtros: Fechas: [${this.fechaInicio || 'Inicio'} a ${this.fechaFin || 'Fin'}] | Cliente: ${nombreCliente} | Estado: ${this.estado || 'Todos'} | Moneda: ${this.moneda || 'Todas'}`;
    doc.setFontSize(9);
    doc.text(textFiltros, 14, 32);

    // 2. Tabla de Datos
    const tableData = this.filteredCotizaciones.map(c => [
      `#${c.idCotizacion}`,
      new Date(c.fecha).toLocaleDateString(),
      c.clienteNombre || 'Sin Cliente',
      c.monedaCodigo || 'PEN',
      c.totalGeneral ? c.totalGeneral.toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : '0.00',
      c.estadoNombre || 'Pendiente'
    ]);

    autoTable(doc, {
      head: [['ID', 'Fecha', 'Cliente', 'Moneda', 'Total', 'Estado']],
      body: tableData,
      startY: 48,
      theme: 'grid',
      headStyles: { fillColor: [15, 23, 42], textColor: [255, 255, 255], fontStyle: 'bold' },
      styles: { fontSize: 9, cellPadding: 3 },
      columnStyles: {
        0: { cellWidth: 20 },
        1: { cellWidth: 25 },
        2: { cellWidth: 70 },
        3: { cellWidth: 20 },
        4: { cellWidth: 30, halign: 'right' },
        5: { cellWidth: 25 }
      }
    });

    // 3. Resumen y Pie
    const finalY = (doc as any).lastAutoTable.finalY + 12;
    
    // Dibujar caja de totales
    doc.setFillColor(248, 250, 252); // light slate background
    doc.setDrawColor(226, 232, 240); // borders
    doc.rect(14, finalY, 182, 32, 'DF');

    doc.setTextColor(30, 41, 59); // dark text
    doc.setFont("helvetica", "bold");
    doc.setFontSize(11);
    doc.text("Resumen Financiero Acumulado (Soles Consolidados T.C. 3.75):", 18, finalY + 8);
    
    doc.setFont("helvetica", "normal");
    doc.setFontSize(10);
    doc.text(`Total Cotizado: S/. ${this.totalCotizadoPEN.toLocaleString('es-PE', { minimumFractionDigits: 2 })}`, 18, finalY + 17);
    doc.text(`Inversión Total: S/. ${this.inversionTotalPEN.toLocaleString('es-PE', { minimumFractionDigits: 2 })}`, 18, finalY + 25);
    
    doc.setFont("helvetica", "bold");
    doc.setTextColor(5, 150, 105); // emerald green
    doc.text(`Utilidad Estimada: S/. ${this.utilidadTotalPEN.toLocaleString('es-PE', { minimumFractionDigits: 2 })}`, 110, finalY + 21);

    // Guardar archivo
    doc.save(`reporte_cotizaciones_${new Date().toISOString().substring(0, 10)}.pdf`);
  }

  exportarCSV(): void {
    // Generar formato CSV tabulado por ";" compatible con Excel español
    let csvContent = '\uFEFF'; // BOM para asegurar codificación UTF-8 en Excel
    csvContent += 'ID;Fecha;Cliente;Moneda;Total;Estado\r\n';

    this.filteredCotizaciones.forEach(c => {
      const fechaStr = c.fecha ? new Date(c.fecha).toLocaleDateString() : '';
      const fila = `#${c.idCotizacion};"${fechaStr}";"${c.clienteNombre || ''}";"${c.monedaCodigo || 'PEN'}";"${c.totalGeneral || 0}";"${c.estadoNombre || ''}"`;
      csvContent += fila + '\r\n';
    });

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute("download", `reporte_cotizaciones_${new Date().toISOString().substring(0, 10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url); // Liberar recurso de memoria
  }
}
