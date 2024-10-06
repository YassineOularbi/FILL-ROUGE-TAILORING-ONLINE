import { APP_INITIALIZER, ApplicationConfig, ENVIRONMENT_INITIALIZER, importProvidersFrom, inject, provideZoneChangeDetection } from '@angular/core';
import { PreloadAllModules, provideRouter, withPreloading } from '@angular/router';
import { routes } from './app.routes';
import { PrimeNGConfig } from 'primeng/api';
import { provideClientHydration } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { initializeKeycloak } from './core/keycloak/initializeKeycloak';


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withPreloading(PreloadAllModules)),
    provideClientHydration(),
    provideAnimationsAsync(),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
    importProvidersFrom(KeycloakAngularModule),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
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
