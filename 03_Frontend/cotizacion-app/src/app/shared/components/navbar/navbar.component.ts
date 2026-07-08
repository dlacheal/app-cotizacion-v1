import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  public readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  public readonly title = 'Cotizador LTS';

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
