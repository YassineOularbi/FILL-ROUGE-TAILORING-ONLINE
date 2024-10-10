import { Routes } from '@angular/router';
import { SignupComponent } from './signup.component';

export const SIGNUP_ROUTES: Routes = [
    {
        path: '',
        component: SignupComponent,
        children: [
            {
                path: 'become-customer',
                loadComponent: () => import('./become-customer/become-customer.component').then(m => m.BecomeCustomerComponent)
            },
            {
                path: 'become-tailor',
                loadComponent: () => import('./become-tailor/become-tailor.component').then(m => m.BecomeTailorComponent)
            }
        ]
    }
];