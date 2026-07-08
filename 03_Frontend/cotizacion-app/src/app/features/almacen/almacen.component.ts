import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Almacen } from './models/almacen.model';
import { AlmacenService } from './services/almacen.service';
import { Alert } from '../../shared/utils/swal.utils';

@Component({
  selector: 'app-almacen',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './almacen.component.html'
})
export class AlmacenComponent implements OnInit {

  almacenes: Almacen[] = [];
  page = 0;
  size = 10;
  totalElements = 0;
  totalPages = 0;
  paginas: number[] = [];

  constructor(
    private almacenService: AlmacenService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarAlmacenes();
  }

  cargarAlmacenes(): void {
    this.almacenService.getAlmacenes(this.page, this.size).subscribe({
      next: (data) => {
        this.almacenes = data.content;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.paginas = Array.from({ length: this.totalPages }, (_, i) => i);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching almacenes', err)
    });
  }

  irAPagina(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.page = pageNumber;
      this.cargarAlmacenes();
    }
  }

  anteriorPagina(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarAlmacenes();
    }
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarAlmacenes();
    }
  }

  eliminarAlmacen(id: number): void {
    Alert.confirm(
      '¿Está seguro?',
      'Se eliminará este almacén permanentemente.',
      () => {
        this.almacenService.eliminarAlmacen(id).subscribe({
          next: () => {
            Alert.success('El almacén fue eliminado correctamente.');
            this.cargarAlmacenes();
          },
          error: (err) => {
            console.error('Error deleting warehouse', err);
            Alert.error(err.error?.message || 'Error al eliminar el almacén.');
          }
        });
      }
    );
  }
}
