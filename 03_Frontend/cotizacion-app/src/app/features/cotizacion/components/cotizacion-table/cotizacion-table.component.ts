import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CotizacionService } from '../../services/cotizacion.service';
import { Alert } from '../../../../shared/utils/swal.utils';

@Component({
  selector: 'cotizacion-table',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, RouterLink],
  templateUrl: './cotizacion-table.component.html'
})
export class CotizacionTableComponent implements OnInit {

  cotizaciones: any[] = [];
  page = 0;
  size = 10;
  totalElements = 0;
  totalPages = 0;
  paginas: number[] = [];

  constructor(
    private cotizacionService: CotizacionService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarCotizaciones();
  }

  cargarCotizaciones(): void {
    this.cotizacionService.listarTodas(this.page, this.size).subscribe({
      next: (data: any) => {
        this.cotizaciones = data.content || [];
        this.totalElements = data.totalElements || 0;
        this.totalPages = data.totalPages || 0;
        this.paginas = Array.from({ length: this.totalPages }, (_, i) => i);
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Error fetching cotizaciones', err)
    });
  }

  irAPagina(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.page = pageNumber;
      this.cargarCotizaciones();
    }
  }

  anteriorPagina(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarCotizaciones();
    }
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarCotizaciones();
    }
  }

  eliminarCotizacion(id: number): void {
    Alert.confirm(
      '¿Está seguro?',
      'Se eliminará esta cotización y todos sus detalles permanentemente.',
      () => {
        this.cotizacionService.eliminarCotizacion(id).subscribe({
          next: () => {
            Alert.success('La cotización fue eliminada correctamente.');
            this.cargarCotizaciones();
          },
          error: (err: any) => {
            console.error('Error deleting quotation', err);
            Alert.error(err.error?.message || 'Error al eliminar la cotización.');
          }
        });
      }
    );
  }
}
