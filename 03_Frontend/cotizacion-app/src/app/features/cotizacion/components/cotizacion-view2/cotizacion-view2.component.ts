import { Component, Input } from '@angular/core';

@Component({
  selector: 'cotizacion-view2',
  imports: [],
  templateUrl: './cotizacion-view2.component.html'
})
export class CotizacionView2Component {

  @Input() inversionTotal!: number;
  @Input() utilidadBrutaTotal!: number;
  @Input() subTotal!: number;
  @Input() montoIgv!: number;
  @Input() totalGeneral!: number;

}
