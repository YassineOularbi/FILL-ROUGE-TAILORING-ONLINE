import { ApplicationConfig, ENVIRONMENT_INITIALIZER, inject, provideZoneChangeDetection } from '@angular/core';
import { PreloadAllModules, provideRouter, withPreloading } from '@angular/router';
import { routes } from './app.routes';
import { PrimeNGConfig } from 'primeng/api';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withPreloading(PreloadAllModules)),
    provideClientHydration(),
    provideAnimationsAsync(),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),

    {
      provide: ENVIRONMENT_INITIALIZER,
      useFactory: () => () => {
        const primengConfig = inject(PrimeNGConfig);

        primengConfig.zIndex = {
          modal: 1100,   
          overlay: 1000, 
          menu: 1000,    
          tooltip: 1100  
        };
      },
      multi: true
    }
  ]
};
