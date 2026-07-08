import { Component, signal, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeadPageComponent } from './shared/components/head-page/head-page.component';
import { FootPageComponent } from './shared/components/foot-page/foot-page.component';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    HeadPageComponent,
    FootPageComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  public readonly authService = inject(AuthService);
  protected readonly title = signal('cotizacion-app');
}
