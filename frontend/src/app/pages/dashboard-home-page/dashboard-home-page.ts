import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-dashboard-home-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './dashboard-home-page.html',
  styleUrl: './dashboard-home-page.css',
})
export class DashboardHomePageComponent {}
