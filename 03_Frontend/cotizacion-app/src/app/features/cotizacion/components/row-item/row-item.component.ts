import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CotizacionDetalle } from '../../models/cotizacionDetalle.model';

@Component({
  selector: 'tr[row-item]',
  standalone: true,
  imports: [],
  templateUrl: './row-item.component.html',
})
export class RowItemComponent {

  @Input() item!: CotizacionDetalle;
  @Input() isNueva: boolean = true;

  @Output() removeEventEmitter: EventEmitter<number> = new EventEmitter<number>();

  onRemove(id: number) {
    this.removeEventEmitter.emit(id);
  }
}
