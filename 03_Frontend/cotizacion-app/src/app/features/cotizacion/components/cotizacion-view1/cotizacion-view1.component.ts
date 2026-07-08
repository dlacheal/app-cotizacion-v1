import { Component, Input } from '@angular/core';

@Component({
  selector: 'cotizacion-view1',
  imports: [],
  templateUrl: './cotizacion-view1.component.html'
})
export class CotizacionView1Component {

  @Input() monedaCodigo!: string;
  @Input() monedaDescripcion!: string;
  @Input() formaPagoNombre!: string;
  @Input() estadoNombre!: string;

  @Input() validezDias!: number;
  @Input() observaciones!: string;

}
