import { Component, Input } from '@angular/core';

@Component({
  selector: 'cotizacion-view',
  imports: [],
  templateUrl: './cotizacion-view.component.html'
})
export class CotizacionViewComponent {

  @Input() idCotizacion!: number;
  @Input() fecha!: string;
  @Input() usuarioUsername!: string;

}
