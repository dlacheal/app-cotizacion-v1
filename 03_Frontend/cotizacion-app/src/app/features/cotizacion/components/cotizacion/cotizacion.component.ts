import { Component, OnInit, ChangeDetectorRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

import { Cotizacion } from '../../models/cotizacion.model';
import { cotizacionData } from '../../data/cotizacion.data';
import { CotizacionService } from '../../services/cotizacion.service';
import { CotizacionViewComponent } from '../cotizacion-view/cotizacion-view.component';
import { ListItemComponent } from '../list-item/list-item.component';
import { CotizacionView2Component } from '../cotizacion-view2/cotizacion-view2.component';
import { CotizacionDetalle } from '../../models/cotizacionDetalle.model';
import { CotizacionFormItemComponent } from '../cotizacion-form-item/cotizacion-form-item.component';
import { AuthService } from '../../../../core/services/auth.service';
import { MonedaService } from '../../../moneda/services/moneda.service';
import { MetodoPagoService } from '../../../metodo-pago/services/metodo-pago.service';
import { ClienteService } from '../../../cliente/services/cliente.service';
import { Moneda } from '../../../moneda/models/moneda.model';
import { MetodoPago } from '../../../metodo-pago/models/metodo-pago.model';
import { Cliente } from '../../../cliente/models/cliente.model';
import { Alert } from '../../../../shared/utils/swal.utils';

@Component({
  selector: 'app-cotizacion',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CotizacionViewComponent,
    CotizacionView2Component,
    ListItemComponent,
    CotizacionFormItemComponent,
  ],
  templateUrl: './cotizacion.component.html'
})
export class CotizacionComponent implements OnInit {

  cotizacion: Cotizacion = cotizacionData;
  monedas: Moneda[] = [];
  metodosPago: MetodoPago[] = [];
  estados: any[] = [];
  clientes: Cliente[] = [];
  selectedClienteId: number | string = '';
  isSaving = false;
  isNueva = false;
  filtroCliente = '';
  isOpen = false;

  toggleDropdown(): void {
    if (!this.isNueva) return;
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.filtroCliente = '';
    }
  }

  selectCliente(id: number | string): void {
    this.selectedClienteId = id;
    this.isOpen = false;
    this.onClienteChange();
  }

  getSelectedClienteLabel(): string {
    if (this.selectedClienteId === '') {
      return 'Elige un cliente de la lista...';
    }
    if (this.selectedClienteId === 'nuevo') {
      return '+ Registrar Nuevo Cliente (Manual)';
    }
    const cliId = Number(this.selectedClienteId);
    const selectedCli = this.clientes.find(c => c.idCliente === cliId);
    if (selectedCli) {
      return `${selectedCli.nombre} (${selectedCli.rucDni})`;
    }
    return 'Elige un cliente de la lista...';
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.custom-select-dropdown')) {
      this.isOpen = false;
    }
  }

  get clientesFiltrados(): Cliente[] {
    if (!this.filtroCliente) {
      return this.clientes;
    }
    const term = this.filtroCliente.toLowerCase().trim();
    return this.clientes.filter(c =>
      c.nombre.toLowerCase().includes(term) ||
      (c.rucDni && c.rucDni.toLowerCase().includes(term))
    );
  }

  constructor(
    private cotizacionService: CotizacionService,
    private monedaService: MonedaService,
    private metodoPagoService: MetodoPagoService,
    private clienteService: ClienteService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarComboboxes();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam && idParam !== 'nueva') {
      this.isNueva = false;
      const id = Number(idParam);
      this.cotizacionService.obtenerCotizacion(id).subscribe({
        next: (data: any) => {
          this.cotizacion = data;
          this.cotizacionService.cotizacion = data;
          this.resolveSelectedClienteId();
          this.cdr.detectChanges();
        },
        error: (err: any) => console.error('Error loading cotizacion from backend', err)
      });
    } else {
      this.isNueva = true;
      this.cotizacionService.listarTodasTodos().subscribe({
        next: (list: any[]) => {
          const ids = list.map(c => Number(c.idCotizacion || 0));
          const maxId = ids.length > 0 ? Math.max(...ids) : 0;
          const newId = maxId + 1;

          this.cotizacion = {
            idCotizacion: newId,
            fecha: new Date().toISOString().substring(0, 19).replace('T', ' '),
            clienteNombre: '',
            clienteRucDni: '',
            clienteTelefono: '',
            clienteEmail: '',
            clienteDireccion: '',
            usuarioUsername: this.authService.nombrePersona() || this.authService.username() || 'admin',
            monedaCodigo: '',
            monedaDescripcion: '',
            formaPagoNombre: '',
            estadoNombre: 'Borrador',
            validezDias: 15,
            observaciones: '',
            inversionTotal: 0,
            utilidadBrutaTotal: 0,
            subTotal: 0,
            montoIgv: 0,
            totalGeneral: 0,
            detalles: []
          };
          this.cotizacionService.cotizacion = this.cotizacion;
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Error fetching list to determine next ID', err);
          this.cotizacion = this.cotizacionService.getCotizacion();
          this.cdr.detectChanges();
        }
      });
    }
  }

  cargarComboboxes(): void {
    // 1. Cargar Monedas
    this.monedaService.getMonedasTodas().subscribe({
      next: (data: Moneda[]) => {
        this.monedas = data || [];
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Error loading currencies for combobox', err)
    });

    // 2. Cargar Métodos de Pago
    this.metodoPagoService.getMetodosPagoTodos().subscribe({
      next: (data: MetodoPago[]) => {
        this.metodosPago = data || [];
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Error loading payment methods for combobox', err)
    });

    // 3. Cargar Estados
    this.cotizacionService.getEstados().subscribe({
      next: (data: any[]) => {
        this.estados = data || [];
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Error loading quotation states for combobox', err)
    });

    // 4. Cargar Clientes
    this.clienteService.getClientesTodos().subscribe({
      next: (data: Cliente[]) => {
        this.clientes = data || [];
        if (!this.isNueva) {
          this.resolveSelectedClienteId();
        }
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Error loading clients list', err)
    });
  }

  resolveSelectedClienteId(): void {
    if (this.cotizacion && this.clientes.length > 0) {
      const foundCli = this.clientes.find(c => c.rucDni === this.cotizacion.clienteRucDni);
      if (foundCli) {
        this.selectedClienteId = foundCli.idCliente;
      } else {
        this.selectedClienteId = 'nuevo';
      }
    }
  }

  onClienteChange(): void {
    if (this.selectedClienteId === 'nuevo' || this.selectedClienteId === '') {
      this.cotizacion.clienteNombre = '';
      this.cotizacion.clienteRucDni = '';
      this.cotizacion.clienteTelefono = '';
      this.cotizacion.clienteEmail = '';
      this.cotizacion.clienteDireccion = '';
    } else {
      const cliId = Number(this.selectedClienteId);
      const selectedCli = this.clientes.find(c => c.idCliente === cliId);
      if (selectedCli) {
        this.cotizacion.clienteNombre = selectedCli.nombre;
        this.cotizacion.clienteRucDni = selectedCli.rucDni;
        this.cotizacion.clienteTelefono = selectedCli.telefono || '';
        this.cotizacion.clienteEmail = selectedCli.email || '';
        this.cotizacion.clienteDireccion = selectedCli.direccion || '';
      }
    }
    this.cdr.detectChanges();
  }

  onRemoveItem(id: number) {
    this.cotizacion = this.cotizacionService.removeDetalle(id);
  }

  addItem(item: CotizacionDetalle) {
    this.cotizacion = this.cotizacionService.addItem(item);
  }

  guardarCotizacion(): void {
    if (!this.cotizacion.clienteNombre || !this.cotizacion.clienteRucDni) {
      Alert.warning('Debe ingresar la Razón Social y RUC/DNI del Cliente.');
      return;
    }
    if (!this.cotizacion.monedaCodigo) {
      Alert.warning('Debe seleccionar una Moneda.');
      return;
    }
    if (!this.cotizacion.formaPagoNombre) {
      Alert.warning('Debe seleccionar una Forma de Pago.');
      return;
    }
    if (this.cotizacion.detalles.length === 0) {
      Alert.warning('Debe agregar al menos un producto a la cotización.');
      return;
    }

    this.isSaving = true;

    // Resolve client
    if (this.selectedClienteId === 'nuevo' || this.selectedClienteId === '') {
      const newCli = {
        nombre: this.cotizacion.clienteNombre,
        rucDni: this.cotizacion.clienteRucDni,
        telefono: this.cotizacion.clienteTelefono,
        email: this.cotizacion.clienteEmail,
        direccion: this.cotizacion.clienteDireccion
      };
      this.clienteService.crearCliente(newCli).subscribe({
        next: (cli: Cliente) => {
          this.procederAGuardarCotizacion(cli.idCliente);
        },
        error: (err: any) => {
          console.error('Error creating client', err);
          Alert.error('Error al registrar el nuevo cliente: ' + (err.error?.message || err.message));
          this.isSaving = false;
        }
      });
    } else {
      this.procederAGuardarCotizacion(Number(this.selectedClienteId));
    }
  }

  private procederAGuardarCotizacion(idCliente: number): void {
    const selectedMon = this.monedas.find(m => m.codigo === this.cotizacion.monedaCodigo);
    if (!selectedMon) {
      Alert.error('Error: No se pudo resolver la moneda seleccionada.');
      this.isSaving = false;
      return;
    }

    const selectedFP = this.metodosPago.find(fp => fp.nombre === this.cotizacion.formaPagoNombre);
    if (!selectedFP) {
      Alert.error('Error: No se pudo resolver la forma de pago seleccionada.');
      this.isSaving = false;
      return;
    }

    const selectedEst = this.estados.find(est => est.nombre === this.cotizacion.estadoNombre);

    const detallesDTO = this.cotizacion.detalles.map((d, index) => ({
      idProducto: d.idProducto,
      idProveedor: null,
      cantidad: d.cantidad,
      costoUnitario: d.costoUnitario,
      porcentajeGanancia: d.porcentajeGanancia,
      descripcionItem: d.descripcionItem || d.productoNombre,
      numeroLinea: index + 1
    }));

    const createDTO = {
      idCliente: idCliente,
      idMoneda: selectedMon.idMoneda,
      idFormaPago: selectedFP.idFormaPago,
      idEstado: selectedEst ? selectedEst.idEstado : null,
      idUsuario: null, // Resuelto por JWT token en el backend
      validezDias: Number(this.cotizacion.validezDias),
      observaciones: this.cotizacion.observaciones,
      detalles: detallesDTO
    };

    this.cotizacionService.crearCotizacion(createDTO).subscribe({
      next: () => {
        Alert.success('¡Cotización registrada con éxito!');
        this.isSaving = false;
        this.router.navigate(['/cotizacion']);
      },
      error: (err: any) => {
        console.error('Error creating quotation', err);
        Alert.error('Error al registrar la cotización: ' + (err.error?.message || err.message));
        this.isSaving = false;
      }
    });
  }

  actualizarCotizacion(): void {
    if (!this.cotizacion.clienteNombre || !this.cotizacion.clienteRucDni) {
      Alert.warning('Debe ingresar la Razón Social y RUC/DNI del Cliente.');
      return;
    }
    if (!this.cotizacion.monedaCodigo) {
      Alert.warning('Debe seleccionar una Moneda.');
      return;
    }
    if (!this.cotizacion.formaPagoNombre) {
      Alert.warning('Debe seleccionar una Forma de Pago.');
      return;
    }
    if (this.cotizacion.detalles.length === 0) {
      Alert.warning('Debe agregar al menos un producto a la cotización.');
      return;
    }

    this.isSaving = true;

    // Resolve client
    if (this.selectedClienteId === 'nuevo' || this.selectedClienteId === '') {
      const newCli = {
        nombre: this.cotizacion.clienteNombre,
        rucDni: this.cotizacion.clienteRucDni,
        telefono: this.cotizacion.clienteTelefono,
        email: this.cotizacion.clienteEmail,
        direccion: this.cotizacion.clienteDireccion
      };
      this.clienteService.crearCliente(newCli).subscribe({
        next: (cli: Cliente) => {
          this.procederAActualizarCotizacion(cli.idCliente);
        },
        error: (err: any) => {
          console.error('Error creating client', err);
          Alert.error('Error al registrar el nuevo cliente: ' + (err.error?.message || err.message));
          this.isSaving = false;
        }
      });
    } else {
      this.procederAActualizarCotizacion(Number(this.selectedClienteId));
    }
  }

  private procederAActualizarCotizacion(idCliente: number): void {
    const selectedMon = this.monedas.find(m => m.codigo === this.cotizacion.monedaCodigo);
    if (!selectedMon) {
      Alert.error('Error: No se pudo resolver la moneda seleccionada.');
      this.isSaving = false;
      return;
    }

    const selectedFP = this.metodosPago.find(fp => fp.nombre === this.cotizacion.formaPagoNombre);
    if (!selectedFP) {
      Alert.error('Error: No se pudo resolver la forma de pago seleccionada.');
      this.isSaving = false;
      return;
    }

    const selectedEst = this.estados.find(est => est.nombre === this.cotizacion.estadoNombre);
    if (!selectedEst) {
      Alert.error('Error: No se pudo resolver el estado seleccionado.');
      this.isSaving = false;
      return;
    }

    const detallesDTO = this.cotizacion.detalles.map((d, index) => ({
      idProducto: d.idProducto,
      idProveedor: null,
      cantidad: d.cantidad,
      costoUnitario: d.costoUnitario,
      porcentajeGanancia: d.porcentajeGanancia,
      descripcionItem: d.descripcionItem || d.productoNombre,
      numeroLinea: index + 1
    }));

    const updateDTO = {
      idCliente: idCliente,
      idMoneda: selectedMon.idMoneda,
      idFormaPago: selectedFP.idFormaPago,
      idEstado: selectedEst.idEstado,
      idUsuario: null,
      validezDias: Number(this.cotizacion.validezDias),
      observaciones: this.cotizacion.observaciones,
      detalles: detallesDTO
    };

    const id = this.cotizacion.idCotizacion;
    this.cotizacionService.actualizarCotizacion(id, updateDTO).subscribe({
      next: () => {
        Alert.success('¡Cotización actualizada con éxito!');
        this.isSaving = false;
        this.router.navigate(['/cotizacion']);
      }
    });
  }

  cargarImagen(src: string): Promise<HTMLImageElement> {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.src = src;
      img.onload = () => resolve(img);
      img.onerror = (err) => reject(err);
    });
  }

  async generarPdfInterno(): Promise<void> {
    const doc = new jsPDF();
    const currencySym = this.cotizacion.monedaCodigo === 'USD' ? '$' : 'S/.';

    // 1. Header/Logo Section (Minimalist, White background, red separator line)
    let logoImg: HTMLImageElement | null = null;
    try {
      logoImg = await this.cargarImagen('logo.png');
    } catch (e) {
      console.warn('Could not load logo for internal PDF', e);
    }

    if (logoImg) {
      doc.addImage(logoImg, 'PNG', 15, 10, 45, 22.5); // x=15, y=10, 2:1 aspect ratio
    } else {
      // Fallback text if logo fails
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(18);
      doc.setTextColor(43, 48, 59);
      doc.text('LOST TIME SOLUTION', 15, 22);
    }

    doc.setTextColor(43, 48, 59); // Charcoal color
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(18);
    doc.text('COTIZACIÓN INTERNA', 120, 20);

    // Red Accent ID
    doc.setTextColor(211, 47, 47); // Red accent
    doc.setFontSize(12);
    doc.text(`ID: 000${this.cotizacion.idCotizacion}`, 120, 26);

    // Date
    doc.setTextColor(100, 100, 100);
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.text(`Fecha: ${this.cotizacion.fecha}`, 120, 31);

    // Separator line under header
    doc.setDrawColor(211, 47, 47); // Red line
    doc.setLineWidth(0.8);
    doc.line(15, 36, 195, 36);

    // 2. Info Section (Parallel Columns)
    doc.setTextColor(43, 48, 59);
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(10);

    // Left border company info
    doc.setDrawColor(43, 48, 59);
    doc.setLineWidth(0.5);
    doc.line(15, 42, 15, 82);

    doc.text('LOST TIME SOLUTION E.I.R.L.', 18, 47);
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.setTextColor(80, 80, 80);
    doc.text('RUC: 20603903481', 18, 52);
    doc.text('Mz G LT. 13 Urb. SAN ISIDRO - TRUJILLO', 18, 57);
    doc.text('Tlf: 044 269318 / 948295080', 18, 62);
    doc.text('Email: jrodriguez.LTS@hotmail.com', 18, 67);
    doc.text(`Atendido por: ${this.cotizacion.usuarioUsername}`, 18, 72);

    // Left border client info
    doc.line(115, 42, 115, 82);

    doc.setFont('helvetica', 'bold');
    doc.setFontSize(10);
    doc.setTextColor(43, 48, 59);
    doc.text('DATOS DEL CLIENTE', 118, 47);
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.setTextColor(80, 80, 80);
    doc.text(this.cotizacion.clienteNombre || 'Sin Nombre', 118, 52);
    doc.text(`RUC/DNI: ${this.cotizacion.clienteRucDni}`, 118, 57);
    doc.text(`Email: ${this.cotizacion.clienteEmail || '-'}`, 118, 62);
    doc.text(`Dirección: ${this.cotizacion.clienteDireccion || '-'}`, 118, 67);

    // Separator line under info
    doc.setDrawColor(224, 224, 224);
    doc.setLineWidth(0.3);
    doc.line(15, 86, 195, 86);

    // 3. Commercial Info
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.setTextColor(80, 80, 80);
    doc.text(`Forma de Pago: ${this.cotizacion.formaPagoNombre}`, 15, 91);
    doc.text(`Moneda: ${this.cotizacion.monedaDescripcion || this.cotizacion.monedaCodigo}`, 75, 91);
    doc.text(`Validez de Oferta: ${this.cotizacion.validezDias} días`, 140, 91);

    // 4. Details Table
    const headers = [['ID', 'Nombre / Descripción', 'Cant.', 'UM', 'Costo Unit.', 'Gan. %', 'Venta Unit.', 'Subtotal', 'IGV', 'Monto']];
    const data = this.cotizacion.detalles.map(d => {
      const cost = d.costoUnitario;
      const margin = d.porcentajeGanancia;
      const quantity = d.cantidad;
      const ventaUnit = cost * (1 + margin / 100);
      const subtotal = ventaUnit * quantity;
      const igv = subtotal * 0.18;
      const total = subtotal + igv;

      return [
        d.idProducto,
        d.descripcionItem || d.productoNombre,
        quantity,
        d.unidadMedida || 'UNIDAD',
        `${currencySym} ${cost.toFixed(2)}`,
        `${margin}%`,
        `${currencySym} ${ventaUnit.toFixed(2)}`,
        `${currencySym} ${subtotal.toFixed(2)}`,
        `${currencySym} ${igv.toFixed(2)}`,
        `${currencySym} ${total.toFixed(2)}`
      ];
    });

    autoTable(doc, {
      head: headers,
      body: data,
      startY: 96,
      theme: 'striped',
      headStyles: { fillColor: [43, 48, 59], textColor: [255, 255, 255], fontStyle: 'bold', fontSize: 8 },
      styles: { fontSize: 8, cellPadding: 3 },
      alternateRowStyles: { fillColor: [248, 249, 250] },
      columnStyles: {
        1: { cellWidth: 40 },
      }
    });

    // 5. Financial Summary Blocks
    const finalY = (doc as any).lastAutoTable.finalY + 10;

    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(80, 80, 80);

    doc.text(`Inversión Total:`, 115, finalY);
    doc.text(`${currencySym} ${this.cotizacion.inversionTotal.toFixed(2)}`, 165, finalY);

    doc.text(`Utilidad Bruta:`, 115, finalY + 5);
    doc.text(`${currencySym} ${this.cotizacion.utilidadBrutaTotal.toFixed(2)}`, 165, finalY + 5);

    doc.text(`Subtotal:`, 115, finalY + 10);
    doc.text(`${currencySym} ${this.cotizacion.subTotal.toFixed(2)}`, 165, finalY + 10);

    doc.text(`IGV Total (18%):`, 115, finalY + 15);
    doc.text(`${currencySym} ${this.cotizacion.montoIgv.toFixed(2)}`, 165, finalY + 15);

    // Total General Box
    doc.setDrawColor(211, 47, 47); // Red border
    doc.setLineWidth(0.5);
    doc.setFillColor(248, 249, 250); // Gray background
    doc.rect(110, finalY + 18, 85, 10, 'FD');

    doc.setTextColor(43, 48, 59);
    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    doc.text(`Total General:`, 115, finalY + 24);
    doc.text(`${currencySym} ${this.cotizacion.totalGeneral.toFixed(2)}`, 165, finalY + 24);

    // Watermark (Centered on the page, drawn after tables so it overlays the row backgrounds)
    if (logoImg) {
      try {
        if (typeof (doc as any).GState === 'function') {
          doc.saveGraphicsState();
          const gState = new (doc as any).GState({ opacity: 0.06 });
          doc.setGState(gState);
          doc.addImage(logoImg, 'PNG', 45, 118.5, 120, 60);
          doc.restoreGraphicsState();
        }
      } catch (e) {
        console.warn('Failed to add watermark to internal PDF', e);
      }
    }

    // Signature (Centered, 5 line breaks below Total General)
    try {
      const firmaImg = await this.cargarImagen('firma.png');
      doc.addImage(firmaImg, 'PNG', 82.5, finalY + 45, 45, 22.5);
    } catch (e) {
      console.warn('Could not load signature image for internal PDF', e);
    }

    // Save document
    doc.save(`Cotizacion_Interna_000${this.cotizacion.idCotizacion}.pdf`);
  }

  async generarPdfCliente(): Promise<void> {
    const doc = new jsPDF();
    const currencySym = this.cotizacion.monedaCodigo === 'USD' ? '$' : 'S/.';

    // 1. Header/Logo Section (Minimalist, White background, red separator line)
    let logoImg: HTMLImageElement | null = null;
    try {
      logoImg = await this.cargarImagen('logo.png');
    } catch (e) {
      console.warn('Could not load logo for client PDF', e);
    }

    if (logoImg) {
      doc.addImage(logoImg, 'PNG', 15, 10, 45, 22.5); // x=15, y=10, 2:1 aspect ratio
    } else {
      doc.setFont('helvetica', 'bold');
      doc.setFontSize(18);
      doc.setTextColor(43, 48, 59);
      doc.text('LOST TIME SOLUTION', 15, 22);
    }

    doc.setTextColor(43, 48, 59); // Charcoal color
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(18);
    doc.text('COTIZACIÓN COMERCIAL', 120, 20);

    // Red Accent ID
    doc.setTextColor(211, 47, 47); // Red accent
    doc.setFontSize(12);
    doc.text(`Nro: 000${this.cotizacion.idCotizacion}`, 120, 26);

    // Date
    doc.setTextColor(100, 100, 100);
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.text(`Fecha de Emisión: ${this.cotizacion.fecha}`, 120, 31);

    // Separator line under header
    doc.setDrawColor(211, 47, 47); // Red line
    doc.setLineWidth(0.8);
    doc.line(15, 36, 195, 36);

    // 2. Info Section (Parallel Columns)
    doc.setTextColor(43, 48, 59);
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(10);

    // Left border company info
    doc.setDrawColor(43, 48, 59);
    doc.setLineWidth(0.5);
    doc.line(15, 42, 15, 82);

    doc.text('DE: LOST TIME SOLUTION E.I.R.L.', 18, 47);
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.setTextColor(80, 80, 80);
    doc.text('RUC: 20603903481', 18, 52);
    doc.text('Mz G LT. 13 Urb. SAN ISIDRO - TRUJILLO', 18, 57);
    doc.text('Tlf: 044 269318 / 948295080', 18, 62);
    doc.text('Email: jrodriguez.LTS@hotmail.com', 18, 67);
    doc.text(`Atendido por: ${this.cotizacion.usuarioUsername}`, 18, 72);

    // Left border client info
    doc.line(115, 42, 115, 82);

    doc.setFont('helvetica', 'bold');
    doc.setFontSize(10);
    doc.setTextColor(43, 48, 59);
    doc.text('PARA:', 118, 47);
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.setTextColor(80, 80, 80);
    doc.text(this.cotizacion.clienteNombre || 'Sin Nombre', 118, 52);
    doc.text(`RUC/DNI: ${this.cotizacion.clienteRucDni}`, 118, 57);
    doc.text(`Email: ${this.cotizacion.clienteEmail || '-'}`, 118, 62);
    doc.text(`Dirección: ${this.cotizacion.clienteDireccion || '-'}`, 118, 67);

    // Separator line under info
    doc.setDrawColor(224, 224, 224);
    doc.setLineWidth(0.3);
    doc.line(15, 86, 195, 86);

    // 3. Commercial Info
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.setTextColor(80, 80, 80);
    doc.text(`Forma de Pago: ${this.cotizacion.formaPagoNombre}`, 15, 91);
    doc.text(`Moneda: ${this.cotizacion.monedaDescripcion || this.cotizacion.monedaCodigo}`, 75, 91);
    doc.text(`Validez de Oferta: ${this.cotizacion.validezDias} días`, 140, 91);

    // 4. Details Table
    const headers = [['ID', 'Nombre / Descripción', 'Cantidad', 'UM', 'Precio Venta Unit.', 'Subtotal', 'IGV', 'Monto']];
    const data = this.cotizacion.detalles.map(d => {
      const cost = d.costoUnitario;
      const margin = d.porcentajeGanancia;
      const quantity = d.cantidad;
      const ventaUnit = cost * (1 + margin / 100);
      const subtotal = ventaUnit * quantity;
      const igv = subtotal * 0.18;
      const total = subtotal + igv;

      return [
        d.idProducto,
        d.descripcionItem || d.productoNombre,
        quantity,
        d.unidadMedida || 'UNIDAD',
        `${currencySym} ${ventaUnit.toFixed(2)}`,
        `${currencySym} ${subtotal.toFixed(2)}`,
        `${currencySym} ${igv.toFixed(2)}`,
        `${currencySym} ${total.toFixed(2)}`
      ];
    });

    autoTable(doc, {
      head: headers,
      body: data,
      startY: 96,
      theme: 'striped',
      headStyles: { fillColor: [43, 48, 59], textColor: [255, 255, 255], fontStyle: 'bold' },
      styles: { fontSize: 8, cellPadding: 3 },
      alternateRowStyles: { fillColor: [248, 249, 250] },
      columnStyles: {
        1: { cellWidth: 40 },
      }
    });

    // 5. Financial Summary Blocks
    const finalY = (doc as any).lastAutoTable.finalY + 10;

    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(80, 80, 80);

    doc.text(`Subtotal:`, 115, finalY);
    doc.text(`${currencySym} ${this.cotizacion.subTotal.toFixed(2)}`, 165, finalY);

    doc.text(`IGV Total (18%):`, 115, finalY + 5);
    doc.text(`${currencySym} ${this.cotizacion.montoIgv.toFixed(2)}`, 165, finalY + 5);

    // Total General Box
    doc.setDrawColor(211, 47, 47); // Red border
    doc.setLineWidth(0.5);
    doc.setFillColor(248, 249, 250); // Gray background
    doc.rect(110, finalY + 8, 85, 10, 'FD');

    doc.setTextColor(43, 48, 59);
    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    doc.text('Total a Pagar:', 115, finalY + 14);
    doc.text(`${currencySym} ${this.cotizacion.totalGeneral.toFixed(2)}`, 165, finalY + 14);

    // Watermark (Centered on the page, drawn after tables so it overlays the row backgrounds)
    if (logoImg) {
      try {
        if (typeof (doc as any).GState === 'function') {
          doc.saveGraphicsState();
          const gState = new (doc as any).GState({ opacity: 0.06 });
          doc.setGState(gState);
          doc.addImage(logoImg, 'PNG', 45, 118.5, 120, 60);
          doc.restoreGraphicsState();
        }
      } catch (e) {
        console.warn('Failed to add watermark to client PDF', e);
      }
    }

    // Signature (Centered, 5 line breaks below Total a Pagar)
    try {
      const firmaImg = await this.cargarImagen('firma.png');
      doc.addImage(firmaImg, 'PNG', 82.5, finalY + 35, 45, 22.5);
    } catch (e) {
      console.warn('Could not load signature image for client PDF', e);
    }

    // Save document
    doc.save(`Cotizacion_Cliente_000${this.cotizacion.idCotizacion}.pdf`);
  }
}
