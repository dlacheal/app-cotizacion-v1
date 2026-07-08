import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CotizacionDetalle } from '../../models/cotizacionDetalle.model';
import { RowItemComponent } from '../row-item/row-item.component';

@Component({
  selector: 'list-item',
  standalone: true,
  imports: [RowItemComponent],
  templateUrl: './list-item.component.html',
})
export class ListItemComponent {

  @Input() detalles: CotizacionDetalle[] = [];
  @Input() isNueva: boolean = true;

  @Output() removeEventEmitter: EventEmitter<number> = new EventEmitter<number>();

  onRemove(idDetalle: number) {
    this.removeEventEmitter.emit(idDetalle);
  }
}
