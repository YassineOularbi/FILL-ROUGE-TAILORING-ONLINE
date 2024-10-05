import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { HomeLayoutComponent } from './_layout/home-layout.component';
import { HomeComponent } from './home.component';

export const HOME_ROUTES: Routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: '',
        component: HomeLayoutComponent
      },
      {
        path: 'auth',
        loadChildren: () => import('./_components/_auth/auth.modules').then(m => m.AuthModule)
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(HOME_ROUTES)],
  exports: [RouterModule]
})
export class HomeRoutes { }