import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CategoriaService } from '../../services/categoria.service';

@Component({
  selector: 'app-categoria-form',
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './categoria-form.component.html'
})
export class CategoriaFormComponent implements OnInit {
  public categoriaForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public categoriaId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private categoriaService: CategoriaService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModoEdicion();
  }

  private inicializarFormulario(): void {
    this.categoriaForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      descripcion: ['', [Validators.maxLength(250)]]
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.categoriaId = Number(idParam);
      if (!isNaN(this.categoriaId)) {
        this.isEditMode = true;
        this.cargarCategoria(this.categoriaId);
      }
    }
  }

  private cargarCategoria(id: number): void {
    this.categoriaService.getCategoriaById(id).subscribe({
      next: (categoria) => {
        this.categoriaForm.patchValue({
          nombre: categoria.nombre,
          descripcion: categoria.descripcion
        });
      },
      error: (error) => {
        console.error('Error loading category', error);
        this.errorMessage = 'No se pudo cargar los datos de la categoría.';
      }
    });
  }

  public onSubmit(): void {
    if (this.categoriaForm.invalid) {
      this.categoriaForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const categoriaRequest = this.categoriaForm.value as {
      nombre: string;
      descripcion?: string;
    };

    const request$ = this.isEditMode && this.categoriaId !== null
      ? this.categoriaService.actualizarCategoria(this.categoriaId, categoriaRequest)
      : this.categoriaService.crearCategoria(categoriaRequest);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode 
          ? '¡Categoría actualizada exitosamente!' 
          : '¡Categoría registrada exitosamente!';

        if (!this.isEditMode) {
          this.categoriaForm.reset();
          this.categoriaForm.clearValidators();
          this.categoriaForm.updateValueAndValidity();
        }

        setTimeout(() => {
          this.router.navigate(['/categoria']);
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || error.message || 'Error al guardar la categoría';
      }
    });
  }

}
