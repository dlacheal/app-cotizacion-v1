import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProveedorService } from '../../services/proveedor.service';

@Component({
  selector: 'app-proveedor-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './proveedor-form.component.html'
})
export class ProveedorFormComponent implements OnInit {
  public proveedorForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public proveedorId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private proveedorService: ProveedorService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModoEdicion();
  }

  private inicializarFormulario(): void {
    this.proveedorForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
      ruc: ['', [Validators.required, Validators.pattern('^[0-9]{11}$')]],
      telefono: ['', [Validators.maxLength(30)]],
      email: ['', [Validators.email, Validators.maxLength(150)]],
      direccion: ['', [Validators.maxLength(250)]]
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.proveedorId = Number(idParam);
      if (!isNaN(this.proveedorId)) {
        this.isEditMode = true;
        this.cargarProveedor(this.proveedorId);
      }
    }
  }

  private cargarProveedor(id: number): void {
    this.proveedorService.getProveedorById(id).subscribe({
      next: (proveedor) => {
        this.proveedorForm.patchValue({
          nombre: proveedor.nombre,
          ruc: proveedor.ruc,
          telefono: proveedor.telefono,
          email: proveedor.email,
          direccion: proveedor.direccion
        });
      },
      error: (error) => {
        console.error('Error loading supplier', error);
        this.errorMessage = 'No se pudo cargar los datos del proveedor.';
      }
    });
  }

  public onSubmit(): void {
    if (this.proveedorForm.invalid) {
      this.proveedorForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const proveedorRequest = this.proveedorForm.value;

    const request$ = this.isEditMode && this.proveedorId !== null
      ? this.proveedorService.actualizarProveedor(this.proveedorId, proveedorRequest)
      : this.proveedorService.crearProveedor(proveedorRequest);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode 
          ? '¡Proveedor actualizado exitosamente!' 
          : '¡Proveedor registrado exitosamente!';

        if (!this.isEditMode) {
          this.proveedorForm.reset();
          this.proveedorForm.clearValidators();
          this.proveedorForm.updateValueAndValidity();
        }

        setTimeout(() => {
          this.router.navigate(['/proveedor']);
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || error.message || 'Error al guardar el proveedor';
      }
    });
  }
}
