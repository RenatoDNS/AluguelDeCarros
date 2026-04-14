import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './dashboard-page.html',
  styleUrl: './dashboard-page.css',
})
export class DashboardPageComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly userType = this.authService.userType;
  readonly showDescobrir = computed(() => this.authService.isCliente());

  logout() {
    this.authService.logout();
    void this.router.navigate(['/login']);
  }
}
