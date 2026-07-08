import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MetodoPago } from './models/metodo-pago.model';
import { MetodoPagoService } from './services/metodo-pago.service';
import { Alert } from '../../shared/utils/swal.utils';

@Component({
  selector: 'app-metodo-pago',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './metodo-pago.component.html'
})
export class MetodoPagoComponent implements OnInit {

  metodos: MetodoPago[] = [];
  page = 0;
  size = 10;
  totalElements = 0;
  totalPages = 0;
  paginas: number[] = [];

  constructor(
    private metodoPagoService: MetodoPagoService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarMetodos();
  }

  cargarMetodos(): void {
    this.metodoPagoService.getMetodosPago(this.page, this.size).subscribe({
      next: (data) => {
        this.metodos = data.content;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.paginas = Array.from({ length: this.totalPages }, (_, i) => i);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching payment methods', err)
    });
  }

  irAPagina(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.page = pageNumber;
      this.cargarMetodos();
    }
  }

  anteriorPagina(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarMetodos();
    }
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarMetodos();
    }
  }

  eliminarMetodo(id: number): void {
    Alert.confirm(
      '¿Está seguro?',
      'Se eliminará este método de pago permanentemente.',
      () => {
        this.metodoPagoService.eliminarMetodoPago(id).subscribe({
          next: () => {
            Alert.success('El método de pago fue eliminado correctamente.');
            this.cargarMetodos();
          },
          error: (err) => {
            console.error('Error deleting payment method', err);
            Alert.error(err.error?.message || 'Error al eliminar el método de pago.');
          }
        });
      }
    );
  }
}
