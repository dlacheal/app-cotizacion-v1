import { Routes } from '@angular/router';
import { CategoriaComponent } from './features/categoria/categoria.component';
import { ClienteComponent } from './features/cliente/cliente.component';
import { CotizacionComponent } from './features/cotizacion/components/cotizacion/cotizacion.component';
import { CotizacionTableComponent } from './features/cotizacion/components/cotizacion-table/cotizacion-table.component';
import { PersonaComponent } from './features/persona/persona.component';
import { ProductoComponent } from './features/producto/producto.component';
import { ProveedorComponent } from './features/proveedor/proveedor.component';
import { UsuarioComponent } from './features/usuario/usuario.component';
import { AlmacenComponent } from './features/almacen/almacen.component';
import { AuthorityComponent } from './features/authority/authority.component';
import { MonedaComponent } from './features/moneda/moneda.component';
import { LoginComponent } from './features/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { CategoriaFormComponent } from './features/categoria/components/categoria-form/categoria-form.component';
import { ClienteFormComponent } from './features/cliente/components/cliente-form/cliente-form.component';
import { ProductoFormComponent } from './features/producto/components/producto-form/producto-form.component';
import { ProveedorFormComponent } from './features/proveedor/components/proveedor-form/proveedor-form.component';
import { UsuarioFormComponent } from './features/usuario/components/usuario-form/usuario-form.component';
import { AlmacenFormComponent } from './features/almacen/components/almacen-form/almacen-form.component';
import { MonedaFormComponent } from './features/moneda/components/moneda-form/moneda-form.component';
import { MetodoPagoComponent } from './features/metodo-pago/metodo-pago.component';
import { MetodoPagoFormComponent } from './features/metodo-pago/components/metodo-pago-form/metodo-pago-form.component';

export const routes: Routes = [
  { path: '', redirectTo: 'categoria', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'categoria', component: CategoriaComponent, canActivate: [authGuard] },
  { path: 'categoria/nueva', component: CategoriaFormComponent, canActivate: [authGuard] },
  { path: 'categoria/:id', component: CategoriaFormComponent, canActivate: [authGuard] },
  { path: 'cliente', component: ClienteComponent, canActivate: [authGuard] },
  { path: 'cliente/nuevo', component: ClienteFormComponent, canActivate: [authGuard] },
  { path: 'cliente/:id', component: ClienteFormComponent, canActivate: [authGuard] },
  { path: 'cotizacion', component: CotizacionTableComponent, canActivate: [authGuard] },
  { path: 'cotizacion/nueva', component: CotizacionComponent, canActivate: [authGuard] },
  { path: 'cotizacion/:id', component: CotizacionComponent, canActivate: [authGuard] },
  { path: 'persona', component: PersonaComponent, canActivate: [authGuard] },
  { path: 'producto', component: ProductoComponent, canActivate: [authGuard] },
  { path: 'producto/nuevo', component: ProductoFormComponent, canActivate: [authGuard] },
  { path: 'producto/:id', component: ProductoFormComponent, canActivate: [authGuard] },
  { path: 'proveedor', component: ProveedorComponent, canActivate: [authGuard] },
  { path: 'proveedor/nuevo', component: ProveedorFormComponent, canActivate: [authGuard] },
  { path: 'proveedor/:id', component: ProveedorFormComponent, canActivate: [authGuard] },
  { path: 'usuario', component: UsuarioComponent, canActivate: [authGuard] },
  { path: 'usuario/nuevo', component: UsuarioFormComponent, canActivate: [authGuard] },
  { path: 'usuario/:id', component: UsuarioFormComponent, canActivate: [authGuard] },
  { path: 'almacen', component: AlmacenComponent, canActivate: [authGuard] },
  { path: 'almacen/nuevo', component: AlmacenFormComponent, canActivate: [authGuard] },
  { path: 'almacen/:id', component: AlmacenFormComponent, canActivate: [authGuard] },
  { path: 'authority', component: AuthorityComponent, canActivate: [authGuard] },
  { path: 'moneda', component: MonedaComponent, canActivate: [authGuard] },
  { path: 'moneda/nueva', component: MonedaFormComponent, canActivate: [authGuard] },
  { path: 'moneda/:id', component: MonedaFormComponent, canActivate: [authGuard] },
  { path: 'metodo-pago', component: MetodoPagoComponent, canActivate: [authGuard] },
  { path: 'metodo-pago/nuevo', component: MetodoPagoFormComponent, canActivate: [authGuard] },
  { path: 'metodo-pago/:id', component: MetodoPagoFormComponent, canActivate: [authGuard] }
];
