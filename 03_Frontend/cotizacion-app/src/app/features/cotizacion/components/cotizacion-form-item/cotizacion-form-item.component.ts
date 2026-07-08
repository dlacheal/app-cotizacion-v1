import { Component, EventEmitter, OnInit, Output, ChangeDetectorRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CotizacionDetalle } from '../../models/cotizacionDetalle.model';
import { ProductoService } from '../../../producto/services/producto.service';
import { Producto } from '../../../producto/models/producto.model';

@Component({
  selector: 'cotizacion-form-item',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cotizacion-form-item.component.html'
})
export class CotizacionFormItemComponent implements OnInit {

  @Output() addItemEventEmitter = new EventEmitter<CotizacionDetalle>();

  private counterId = 3;

  productos: Producto[] = [];
  selectedProductoId: number | string = '';
  filtroProducto = '';
  isOpen = false;

  item: any = {
    productoNombre: '',
    cantidad: '',
    unidadMedida: 'UNIDAD',
    costoUnitario: '',
    porcentajeGanancia: '20', // Default 20%
  };

  constructor(
    private productoService: ProductoService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.productoService.getProductosActivos().subscribe({
      next: (data) => {
        this.productos = data || [];
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error loading active products', err)
    });
  }

  get productosFiltrados(): Producto[] {
    if (!this.filtroProducto) {
      return this.productos;
    }
    const term = this.filtroProducto.toLowerCase().trim();
    return this.productos.filter(p =>
      p.nombre.toLowerCase().includes(term) ||
      (p.descripcion && p.descripcion.toLowerCase().includes(term))
    );
  }

  toggleDropdown(): void {
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.filtroProducto = '';
    }
  }

  selectProducto(id: number): void {
    this.selectedProductoId = id;
    this.isOpen = false;
    this.onProductoChange();
  }

  getSelectedProductoLabel(): string {
    if (this.selectedProductoId === '') {
      return 'Elige un producto...';
    }
    const prodId = Number(this.selectedProductoId);
    const selectedProd = this.productos.find(p => p.idProducto === prodId);
    if (selectedProd) {
      return `${selectedProd.nombre} (S/. ${selectedProd.precioBase})`;
    }
    return 'Elige un producto...';
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.custom-product-dropdown')) {
      this.isOpen = false;
    }
  }

  onProductoChange(): void {
    const prodId = Number(this.selectedProductoId);
    const selectedProd = this.productos.find(p => p.idProducto === prodId);
    if (selectedProd) {
      this.item.productoNombre = selectedProd.nombre;
      this.item.costoUnitario = selectedProd.precioBase;
    } else {
      this.item.productoNombre = '';
      this.item.costoUnitario = '';
    }
    this.cdr.detectChanges();
  }

  onSubmit(): void {
    const prodId = Number(this.selectedProductoId);
    const selectedProd = this.productos.find(p => p.idProducto === prodId);
    if (!selectedProd) return;

    this.addItemEventEmitter.emit({
      idDetalle: this.counterId,
      idProducto: selectedProd.idProducto,
      productoNombre: selectedProd.nombre,
      codigoArticulo: 'PROD-' + selectedProd.idProducto,
      unidadMedida: this.item.unidadMedida,
      descripcionItem: selectedProd.descripcion || selectedProd.nombre,
      numeroLinea: this.counterId,
      cantidad: Number(this.item.cantidad),
      costoUnitario: Number(this.item.costoUnitario),
      porcentajeGanancia: Number(this.item.porcentajeGanancia),
      precioVentaUnit: 0,
      subTotal: 0,
      impuestoMonto: 0,
      totalLinea: 0
    });
    this.counterId++;
    
    // Reset form
    this.selectedProductoId = '';
    this.item = {
      productoNombre: '',
      cantidad: '',
      unidadMedida: 'UNIDAD',
      costoUnitario: '',
      porcentajeGanancia: '20'
    };
    this.cdr.detectChanges();
  }
}
