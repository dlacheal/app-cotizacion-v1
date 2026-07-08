import { Component } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-head-page',
  standalone: true,
  imports: [NavbarComponent],
  templateUrl: './head-page.component.html'
})
export class HeadPageComponent { }
