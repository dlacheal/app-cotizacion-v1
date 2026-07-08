import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { PersonaService } from '../../../persona/services/persona.service';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './usuario-form.component.html'
})
export class UsuarioFormComponent implements OnInit {
  public usuarioForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public usuarioId: number | null = null;
  public personaId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private personaService: PersonaService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.verificarModoEdicion();
  }

  private inicializarFormulario(): void {
    this.usuarioForm = this.fb.group({
      // Campos de Persona
      nombrePersona: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
      dni: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      telefonoPersona: ['', [Validators.maxLength(20)]],
      emailPersona: ['', [Validators.email, Validators.maxLength(100)]],
      
      // Campos de Usuario
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      enable: [true],
      adminRole: [false],
      vendedorRole: [false],
      userRole: [true]
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.usuarioId = Number(idParam);
      if (!isNaN(this.usuarioId)) {
        this.isEditMode = true;
        this.cargarUsuario(this.usuarioId);
      }
    }
  }

  private cargarUsuario(id: number): void {
    this.usuarioService.getUsuarioById(id).subscribe({
      next: (usuario) => {
        this.personaId = usuario.persona?.idPersona || null;

        const hasAdmin = usuario.roles?.some((r: any) => r.nombre === 'ROLE_ADMIN');
        const hasVendedor = usuario.roles?.some((r: any) => r.nombre === 'ROLE_VENDEDOR');
        const hasUser = usuario.roles?.some((r: any) => r.nombre === 'ROLE_USER');

        this.usuarioForm.patchValue({
          nombrePersona: usuario.persona?.nombre || '',
          dni: usuario.persona?.dni || '',
          telefonoPersona: usuario.persona?.telefono || '',
          emailPersona: usuario.persona?.email || '',
          username: usuario.username,
          enable: usuario.enable,
          adminRole: hasAdmin,
          vendedorRole: hasVendedor,
          userRole: hasUser
        });

        this.usuarioForm.get('password')?.setValidators([Validators.minLength(6)]);
        this.usuarioForm.get('password')?.updateValueAndValidity();
      },
      error: (error) => {
        console.error('Error loading user', error);
        this.errorMessage = 'No se pudo cargar los datos del usuario.';
      }
    });
  }

  public onSubmit(): void {
    if (this.usuarioForm.invalid) {
      this.usuarioForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const rolesList: string[] = [];
    if (this.usuarioForm.value.adminRole) rolesList.push('ADMIN');
    if (this.usuarioForm.value.vendedorRole) rolesList.push('VENDEDOR');
    if (this.usuarioForm.value.userRole) rolesList.push('USER');
    if (rolesList.length === 0) rolesList.push('USER');

    const personaPayload = {
      nombre: this.usuarioForm.value.nombrePersona,
      dni: this.usuarioForm.value.dni,
      telefono: this.usuarioForm.value.telefonoPersona,
      email: this.usuarioForm.value.emailPersona
    };

    if (this.isEditMode) {
      if (this.usuarioId === null || this.personaId === null) {
        this.isSubmitting = false;
        this.errorMessage = 'Identificadores inválidos en el formulario.';
        return;
      }

      this.personaService.actualizarPersona(this.personaId, personaPayload).pipe(
        switchMap(() => {
          const usuarioPayload: any = {
            username: this.usuarioForm.value.username,
            idPersona: this.personaId,
            enable: this.usuarioForm.value.enable,
            roles: rolesList
          };
          if (this.usuarioForm.value.password) {
            usuarioPayload.password = this.usuarioForm.value.password;
          }
          return this.usuarioService.actualizarUsuario(this.usuarioId!, usuarioPayload);
        })
      ).subscribe({
        next: () => {
          this.isSubmitting = false;
          this.successMessage = '¡Usuario y datos de persona actualizados con éxito!';
          setTimeout(() => this.router.navigate(['/usuario']), 1500);
        },
        error: (err) => {
          this.isSubmitting = false;
          this.errorMessage = err.error?.message || err.error || err.message || 'Error al actualizar los datos.';
        }
      });

    } else {
      this.personaService.existsByDni(personaPayload.dni).subscribe({
        next: (dniExists) => {
          if (dniExists) {
            this.isSubmitting = false;
            this.errorMessage = 'El DNI ya se encuentra registrado para otra persona.';
            return;
          }

          this.personaService.crearPersona(personaPayload).pipe(
            switchMap((personaCreada) => {
              const usuarioPayload = {
                username: this.usuarioForm.value.username,
                password: this.usuarioForm.value.password,
                idPersona: personaCreada.idPersona,
                roles: rolesList
              };
              return this.usuarioService.registrarUsuario(usuarioPayload);
            })
          ).subscribe({
            next: () => {
              this.isSubmitting = false;
              this.successMessage = '¡Persona y Usuario registrados exitosamente!';
              this.usuarioForm.reset({ enable: true, userRole: true });
              this.usuarioForm.clearValidators();
              this.usuarioForm.updateValueAndValidity();
              setTimeout(() => this.router.navigate(['/usuario']), 1500);
            },
            error: (err) => {
              this.isSubmitting = false;
              this.errorMessage = err.error?.message || err.error || err.message || 'Error al registrar el usuario.';
            }
          });
        },
        error: (err) => {
          this.isSubmitting = false;
          this.errorMessage = 'Error de conexión al verificar el DNI.';
        }
      });
    }
  }
}
