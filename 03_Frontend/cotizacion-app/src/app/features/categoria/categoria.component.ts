import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Categoria } from './models/categoria.model';
import { CategoriaService } from './services/categoria.service';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './categoria.component.html'
})
export class CategoriaComponent implements OnInit {

  categorias: Categoria[] = [];
  page = 0;
  size = 10;
  totalElements = 0;
  totalPages = 0;
  paginas: number[] = [];

  constructor(
    private categoriaService: CategoriaService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.categoriaService.getCategorias(this.page, this.size).subscribe({
      next: (data) => {
        this.categorias = data.content;
        this.totalElements = data.totalElements;
        this.totalPages = data.totalPages;
        this.paginas = Array.from({ length: this.totalPages }, (_, i) => i);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching categorias', err)
    });
  }

  irAPagina(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.page = pageNumber;
      this.cargarCategorias();
    }
  }

  anteriorPagina(): void {
    if (this.page > 0) {
      this.page--;
      this.cargarCategorias();
    }
  }

  siguientePagina(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargarCategorias();
    }
  }

  eliminarCategoria(id: number): void {
    if (confirm('¿Está seguro de que desea eliminar esta categoría?')) {
      this.categoriaService.eliminarCategoria(id).subscribe({
        next: () => {
          this.cargarCategorias();
        },
        error: (err) => {
          console.error('Error deleting category', err);
          alert(err.error?.message || 'Error al eliminar la categoría.');
        }
      });
    }
  }
}