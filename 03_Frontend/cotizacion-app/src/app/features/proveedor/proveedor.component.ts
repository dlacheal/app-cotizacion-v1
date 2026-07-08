import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Proveedor } from './models/proveedor.model';
import { ProveedorService } from './services/proveedor.service';

@Component({
  selector: 'app-proveedor',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './proveedor.component.html'
})
export class ProveedorComponent implements OnInit {

  proveedores: Proveedor[] = [];
  page = 0;
  size = 10;
  totalElements = 0;
  totalPages = 0;
  paginas: number[] = [];

  constructor(
    private proveedorService: ProveedorService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarProveedores();
  }

  cargarProveedores(): void {
    this.proveedorService.getProveedores(this.page, this.size).subscribe({
      next: (data) => {
        this.proveedores = data.content;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.paginas = Array.from({ length: this.totalPages }, (_, i) => i);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching proveedores', err)
    });
  }

  irAPagina(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.page = pageNumber;
      this.cargarProveedores();
    }
  }

  anteriorPagina(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarProveedores();
    }
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarProveedores();
    }
  }

  eliminarProveedor(id: number): void {
    if (confirm('¿Está seguro de que desea eliminar este proveedor?')) {
      this.proveedorService.eliminarProveedor(id).subscribe({
        next: () => {
          this.cargarProveedores();
        },
        error: (err) => {
          console.error('Error deleting supplier', err);
          alert(err.error?.message || 'Error al eliminar el proveedor.');
        }
      });
    }
  }
}
