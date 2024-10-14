import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { LanguagePreference } from '../../../../core/enums/language-preference.enum';
import { KeycloakService } from '../../../../core/keycloak/keycloak.service';
import { KeycloakLogoutOptions } from 'keycloak-js';
import { AutoCompleteModule } from 'primeng/autocomplete';


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    DropdownModule,
    FormsModule,
    CommonModule,
    AutoCompleteModule
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent implements OnInit {
  languageOptions: any[] = [];
  selectedLanguage: any | undefined;

  currencies: any[] = [];
  selectedCurrency: any | undefined;

  locations: any[] = [];
  selectedLocation: any | undefined;

  constructor(private keycloakService: KeycloakService) {}

  ngOnInit() {
    this.initializeLanguages();
    this.initializeCurrencies();
    this.initializeLocations();
  }

  private initializeLanguages() {
    this.languageOptions = [
      { name: 'English', code: LanguagePreference.EN, flag: 'us' },
      { name: 'Français', code: LanguagePreference.FR, flag: 'fr' },
      { name: 'Español', code: LanguagePreference.ES, flag: 'es' },
      { name: 'Italiano', code: LanguagePreference.IT, flag: 'it' },
      { name: 'العربية', code: LanguagePreference.AR, flag: 'ar' },
    ];
    this.selectedLanguage = this.languageOptions[0];
  }

  private initializeCurrencies() {
    this.currencies = [
      { name: 'US Dollar', code: 'USD', symbol: '$' },
      { name: 'Euro', code: 'EUR', symbol: '€' },
      { name: 'British Pound', code: 'GBP', symbol: '£' },
      { name: 'Japanese Yen', code: 'JPY', symbol: '¥' },
    ];
    this.selectedCurrency = this.currencies[0];
  }

  private initializeLocations() {
    this.locations = [
      { name: 'New York', code: 'NY' },
      { name: 'Los Angeles', code: 'LA' },
      { name: 'Chicago', code: 'CHI' },
      { name: 'Houston', code: 'HOU' },
      { name: 'Phoenix', code: 'PHX' },
    ];
    this.selectedLocation = this.locations[0];
  }

  onLogout(): void {
    const returnUrl = encodeURIComponent(window.location.pathname);

    const logoutOptions: KeycloakLogoutOptions = {
        redirectUri: `${window.location.origin}/auth/signin?returnUrl=${returnUrl}`,
        logoutMethod: 'GET',
    };
    this.keycloakService.logout(logoutOptions);
  }
}