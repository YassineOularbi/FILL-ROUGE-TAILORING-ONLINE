import { Component, OnInit } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { KeycloakService } from '../../../../core/keycloak/keycloak.service';
import { ProductService } from '../../../../core/services/product.service';
import { debounceTime, switchMap } from 'rxjs/operators';
import { KeycloakLogoutOptions } from 'keycloak-js';
import { CommonModule } from '@angular/common';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    DropdownModule
  ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  languageOptions: any[] = [];
  selectedLanguage: any | undefined;

  currencies: any[] = [];
  selectedCurrency: any | undefined;

  locations: any[] = [];
  selectedLocation: any | undefined;

  searchQuery = new FormControl('');
  filteredResults: any[] = [];
  showSuggestions: boolean = false;

  constructor(private keycloakService: KeycloakService, private productService: ProductService) {}

  ngOnInit() {
    this.initializeLanguages();
    this.initializeCurrencies();
    this.initializeLocations();

    this.searchQuery.valueChanges.pipe(
      debounceTime(300),
      switchMap((query) => this.search(query!))
    ).subscribe((results) => {
      this.filteredResults = results;
      this.showSuggestions = results.length > 0;
    });
  }

  private initializeLanguages() {
    this.languageOptions = [
      { name: 'English', code: 'EN', flag: 'us' },
      { name: 'Français', code: 'FR', flag: 'fr' },
      { name: 'Español', code: 'ES', flag: 'es' },
      { name: 'Italiano', code: 'IT', flag: 'it' },
      { name: 'العربية', code: 'AR', flag: 'ar' },
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

  search(query: string) {
    if (query) {
      return this.productService.autocomplete(query);
    } else {
      this.filteredResults = [];
      return [];
    }
  }

  performSearch() {
    this.productService.search(this.searchQuery.value!, 0, 10, 'name', 'asc').subscribe((result) => {
      console.log('Search Results:', result);
      this.showSuggestions = false;
    });
  }

  selectSuggestion(suggestion: string) {
    this.searchQuery.setValue(suggestion);
    this.showSuggestions = false;
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
