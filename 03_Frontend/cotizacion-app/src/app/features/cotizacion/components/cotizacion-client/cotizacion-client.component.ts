import { Component, Input } from '@angular/core';

@Component({
  selector: 'cotizacion-client',
  imports: [],
  templateUrl: './cotizacion-client.component.html',
})
export class CotizacionClientComponent {

  @Input() clienteNombre!: string;
  @Input() clienteRucDni!: string;
  @Input() clienteTelefono!: string;
  @Input() clienteEmail!: string;
  @Input() clienteDireccion!: string;

}
