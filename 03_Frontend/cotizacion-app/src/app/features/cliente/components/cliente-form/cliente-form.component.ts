import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Cliente } from '../../models/cliente.model';
import { ClienteService } from '../../services/cliente.service';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './cliente-form.component.html'
})
export class ClienteFormComponent implements OnInit {
  public clienteForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public clienteId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModoEdicion();
  }

  private inicializarFormulario(): void {
    this.clienteForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
      rucDni: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
      telefono: ['', [Validators.maxLength(20)]],
      email: ['', [Validators.email, Validators.maxLength(100)]],
      direccion: ['', [Validators.maxLength(250)]]
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.clienteId = Number(idParam);
      if (!isNaN(this.clienteId)) {
        this.isEditMode = true;
        this.cargarCliente(this.clienteId);
      }
    }
  }

  private cargarCliente(id: number): void {
    this.clienteService.getClienteById(id).subscribe({
      next: (cliente) => {
        this.clienteForm.patchValue({
          nombre: cliente.nombre,
          rucDni: cliente.rucDni,
          telefono: cliente.telefono,
          email: cliente.email,
          direccion: cliente.direccion
        });
      },
      error: (error) => {
        console.error('Error loading client', error);
        this.errorMessage = 'No se pudo cargar los datos del cliente.';
      }
    });
  }

  public onSubmit(): void {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const clienteRequest = this.clienteForm.value;

    const request$ = this.isEditMode && this.clienteId !== null
      ? this.clienteService.actualizarCliente(this.clienteId, clienteRequest)
      : this.clienteService.crearCliente(clienteRequest);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode 
          ? '¡Cliente actualizado exitosamente!' 
          : '¡Cliente registrado exitosamente!';

        if (!this.isEditMode) {
          this.clienteForm.reset();
          this.clienteForm.clearValidators();
          this.clienteForm.updateValueAndValidity();
        }

        setTimeout(() => {
          this.router.navigate(['/cliente']);
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || error.message || 'Error al guardar el cliente';
      }
    });
  }
}
