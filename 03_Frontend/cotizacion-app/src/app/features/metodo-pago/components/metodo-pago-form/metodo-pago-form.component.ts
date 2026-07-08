import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MetodoPagoService } from '../../services/metodo-pago.service';

@Component({
  selector: 'app-metodo-pago-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './metodo-pago-form.component.html'
})
export class MetodoPagoFormComponent implements OnInit {
  public metodoForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public metodoId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private metodoPagoService: MetodoPagoService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModoEdicion();
  }

  private inicializarFormulario(): void {
    this.metodoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      descripcion: ['', [Validators.maxLength(250)]]
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.metodoId = Number(idParam);
      if (!isNaN(this.metodoId)) {
        this.isEditMode = true;
        this.cargarMetodo(this.metodoId);
      }
    }
  }

  private cargarMetodo(id: number): void {
    this.metodoPagoService.getMetodoPagoById(id).subscribe({
      next: (metodo) => {
        this.metodoForm.patchValue({
          nombre: metodo.nombre,
          descripcion: metodo.descripcion || ''
        });
      },
      error: (error) => {
        console.error('Error loading payment method', error);
        this.errorMessage = 'No se pudo cargar los datos del método de pago.';
      }
    });
  }

  public onSubmit(): void {
    if (this.metodoForm.invalid) {
      this.metodoForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const payload = {
      nombre: this.metodoForm.value.nombre,
      descripcion: this.metodoForm.value.descripcion
    };

    const request$ = this.isEditMode && this.metodoId !== null
      ? this.metodoPagoService.actualizarMetodoPago(this.metodoId, payload)
      : this.metodoPagoService.crearMetodoPago(payload);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode 
          ? '¡Método de pago actualizado exitosamente!' 
          : '¡Método de pago registrado exitosamente!';

        if (!this.isEditMode) {
          this.metodoForm.reset();
          this.metodoForm.clearValidators();
          this.metodoForm.updateValueAndValidity();
        }

        setTimeout(() => {
          this.router.navigate(['/metodo-pago']);
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || error.error || error.message || 'Error al guardar el método de pago';
      }
    });
  }
}
