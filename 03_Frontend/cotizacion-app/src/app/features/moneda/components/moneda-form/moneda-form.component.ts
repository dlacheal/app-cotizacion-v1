import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MonedaService } from '../../services/moneda.service';

@Component({
  selector: 'app-moneda-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './moneda-form.component.html'
})
export class MonedaFormComponent implements OnInit {
  public monedaForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public monedaId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private monedaService: MonedaService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModoEdicion();
  }

  private inicializarFormulario(): void {
    this.monedaForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(10)]],
      descripcion: ['', [Validators.maxLength(50)]]
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.monedaId = Number(idParam);
      if (!isNaN(this.monedaId)) {
        this.isEditMode = true;
        this.cargarMoneda(this.monedaId);
      }
    }
  }

  private cargarMoneda(id: number): void {
    this.monedaService.getMonedaById(id).subscribe({
      next: (moneda) => {
        this.monedaForm.patchValue({
          codigo: moneda.codigo,
          descripcion: moneda.descripcion || ''
        });
      },
      error: (error) => {
        console.error('Error loading currency', error);
        this.errorMessage = 'No se pudo cargar los datos de la moneda.';
      }
    });
  }

  public onSubmit(): void {
    if (this.monedaForm.invalid) {
      this.monedaForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const payload = {
      codigo: this.monedaForm.value.codigo.toUpperCase(),
      descripcion: this.monedaForm.value.descripcion
    };

    const request$ = this.isEditMode && this.monedaId !== null
      ? this.monedaService.actualizarMoneda(this.monedaId, payload)
      : this.monedaService.crearMoneda(payload);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode 
          ? '¡Moneda actualizada exitosamente!' 
          : '¡Moneda registrada exitosamente!';

        if (!this.isEditMode) {
          this.monedaForm.reset();
          this.monedaForm.clearValidators();
          this.monedaForm.updateValueAndValidity();
        }

        setTimeout(() => {
          this.router.navigate(['/moneda']);
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || error.error || error.message || 'Error al guardar la moneda';
      }
    });
  }
}
