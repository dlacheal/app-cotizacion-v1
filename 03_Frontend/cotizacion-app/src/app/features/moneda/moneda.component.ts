import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Moneda } from './models/moneda.model';
import { MonedaService } from './services/moneda.service';
import { Alert } from '../../shared/utils/swal.utils';

@Component({
  selector: 'app-moneda',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './moneda.component.html'
})
export class MonedaComponent implements OnInit {

  monedas: Moneda[] = [];
  page = 0;
  size = 10;
  totalElements = 0;
  totalPages = 0;
  paginas: number[] = [];

  constructor(
    private monedaService: MonedaService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarMonedas();
  }

  cargarMonedas(): void {
    this.monedaService.getMonedas(this.page, this.size).subscribe({
      next: (data) => {
        this.monedas = data.content;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.paginas = Array.from({ length: this.totalPages }, (_, i) => i);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching monedas', err)
    });
  }

  irAPagina(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.page = pageNumber;
      this.cargarMonedas();
    }
  }

  anteriorPagina(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarMonedas();
    }
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarMonedas();
    }
  }

  eliminarMoneda(id: number): void {
    Alert.confirm(
      '¿Está seguro?',
      'Se eliminará esta moneda permanentemente.',
      () => {
        this.monedaService.eliminarMoneda(id).subscribe({
          next: () => {
            Alert.success('La moneda fue eliminada correctamente.');
            this.cargarMonedas();
          },
          error: (err) => {
            console.error('Error deleting currency', err);
            Alert.error(err.error?.message || 'Error al eliminar la moneda.');
          }
        });
      }
    );
  }
}
