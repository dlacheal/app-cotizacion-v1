import { Component, OnInit } from '@angular/core';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductoService } from '../../services/producto.service';
import { CategoriaService } from '../../../categoria/services/categoria.service';
import { ProveedorService } from '../../../proveedor/services/proveedor.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink],
  templateUrl: './producto-form.component.html'
})
export class ProductoFormComponent implements OnInit {
  public productoForm!: FormGroup;
  public isSubmitting = false;
  public successMessage: string | null = null;
  public errorMessage: string | null = null;
  public isEditMode = false;
  public productoId: number | null = null;

  public categorias: any[] = [];
  public proveedores: any[] = [];

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private proveedorService: ProveedorService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.inicializarFormulario();
    this.cargarDropdownsYVerificarEdicion();
  }

  private inicializarFormulario(): void {
    this.productoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
      descripcion: ['', [Validators.maxLength(2000)]],
      idCategoria: ['', [Validators.required]],
      idProveedor: ['', [Validators.required]],
      precioBase: ['', [Validators.required, Validators.min(0)]]
    });
  }

  private cargarDropdownsYVerificarEdicion(): void {
    forkJoin({
      categoriasData: this.categoriaService.getCategorias(0, 1000),
      proveedoresData: this.proveedorService.getProveedoresTodos()
    }).subscribe({
      next: (res) => {
        this.categorias = res.categoriasData.content || [];
        this.proveedores = res.proveedoresData || [];
        this.verificarModoEdicion();
      },
      error: (err) => {
        console.error('Error al cargar datos auxiliares', err);
        this.errorMessage = 'No se pudieron cargar las categorías o proveedores para el formulario.';
      }
    });
  }

  private verificarModoEdicion(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.productoId = Number(idParam);
      if (!isNaN(this.productoId)) {
        this.isEditMode = true;
        this.cargarProducto(this.productoId);
      }
    }
  }

  private cargarProducto(id: number): void {
    this.productoService.getProductoById(id).subscribe({
      next: (producto) => {
        this.productoForm.patchValue({
          nombre: producto.nombre,
          descripcion: producto.descripcion,
          idCategoria: this.categorias.find(c => c.nombre === producto.categoriaNombre)?.idCategoria || '',
          idProveedor: this.proveedores.find(p => p.nombre === producto.proveedorNombre)?.idProveedor || '',
          precioBase: producto.precioBase
        });
      },
      error: (error) => {
        console.error('Error loading product', error);
        this.errorMessage = 'No se pudo cargar los datos del producto.';
      }
    });
  }

  public onSubmit(): void {
    if (this.productoForm.invalid) {
      this.productoForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = null;
    this.errorMessage = null;

    const productoRequest = {
      nombre: this.productoForm.value.nombre,
      descripcion: this.productoForm.value.descripcion,
      idCategoria: Number(this.productoForm.value.idCategoria),
      idProveedor: Number(this.productoForm.value.idProveedor),
      precioBase: Number(this.productoForm.value.precioBase)
    };

    const request$ = this.isEditMode && this.productoId !== null
      ? this.productoService.actualizarProducto(this.productoId, productoRequest)
      : this.productoService.crearProducto(productoRequest);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = this.isEditMode 
          ? '¡Producto actualizado exitosamente!' 
          : '¡Producto registrado exitosamente!';

        if (!this.isEditMode) {
          this.productoForm.reset();
          this.productoForm.clearValidators();
          this.productoForm.updateValueAndValidity();
        }

        setTimeout(() => {
          this.router.navigate(['/producto']);
        }, 1500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.errorMessage = error.error?.message || error.message || 'Error al guardar el producto';
      }
    });
  }
}
