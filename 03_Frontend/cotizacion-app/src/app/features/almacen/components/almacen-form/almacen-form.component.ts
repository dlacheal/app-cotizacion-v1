import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlmacenService } from '../../services/almacen.service';

@Component({
  selector: 'app-almacen-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './almacen-form.component.html'
})
export class AlmacenFormComponent implements OnInit {
  public almacenForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public almacenId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private almacenService: AlmacenService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModoEdicion();
  }

  private inicializarFormulario(): void {
    this.almacenForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      direccion: ['', [Validators.maxLength(250)]]
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.almacenId = Number(idParam);
      if (!isNaN(this.almacenId)) {
        this.isEditMode = true;
        this.cargarAlmacen(this.almacenId);
      }
    }
  }

  private cargarAlmacen(id: number): void {
    this.almacenService.getAlmacenById(id).subscribe({
      next: (almacen) => {
        this.almacenForm.patchValue({
          nombre: almacen.nombre,
          direccion: almacen.direccion || ''
        });
      },
      error: (error) => {
        console.error('Error loading warehouse', error);
        this.errorMessage = 'No se pudo cargar los datos del almacén.';
      }
    });
  }

  public onSubmit(): void {
    if (this.almacenForm.invalid) {
      this.almacenForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const payload = {
      nombre: this.almacenForm.value.nombre,
      direccion: this.almacenForm.value.direccion
    };

    const request$ = this.isEditMode && this.almacenId !== null
      ? this.almacenService.actualizarAlmacen(this.almacenId, payload)
      : this.almacenService.crearAlmacen(payload);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode 
          ? '¡Almacén actualizado exitosamente!' 
          : '¡Almacén registrado exitosamente!';

        if (!this.isEditMode) {
          this.almacenForm.reset();
          this.almacenForm.clearValidators();
          this.almacenForm.updateValueAndValidity();
        }

        setTimeout(() => {
          this.router.navigate(['/almacen']);
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || error.error || error.message || 'Error al guardar el almacén';
      }
    });
  }
}
