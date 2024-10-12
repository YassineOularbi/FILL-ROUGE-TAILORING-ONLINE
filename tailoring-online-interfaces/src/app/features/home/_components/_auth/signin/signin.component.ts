import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { InputSwitchModule } from 'primeng/inputswitch';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { KeycloakService } from '../../../../../core/keycloak/keycloak.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { AuthRequest } from '../../../../../core/interfaces/auth-request.interface';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    ReactiveFormsModule,
    CommonModule,
    ToastModule,
    PasswordModule,
    ButtonModule,
    InputSwitchModule,
    RouterLink
  ],
  providers: [MessageService],
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {
  signinForm!: FormGroup;
  submitted = false;
  showPassword = false;
  isRememberedAccount = false;
  returnUrl: string | null = null; 

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private authService: AuthService,
    private keyloakService: KeycloakService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
    this.signinForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(4)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false],
    });

    const rememberMe: boolean = localStorage.getItem('remember-me') === 'true';

    if (rememberMe) {
      this.isRememberedAccount = true;
      this.signinForm.patchValue({
        username: localStorage.getItem('saved-username') || '',
        password: localStorage.getItem('saved-password') || '',
        rememberMe: rememberMe,
      });
      this.signinForm.controls['rememberMe'].disable();
    }
  }

  onSubmit(): void {
    this.submitted = true;
    console.log("submit");
    console.log(this.signinForm);
    
    if (this.signinForm.valid) {
      console.log("here");
      
      const rememberMe = this.signinForm.value.rememberMe;
      const authRequest: AuthRequest = this.signinForm.value;
      this.authService.signing(authRequest).subscribe({
        next: (response) => {
          if (rememberMe) {
            localStorage.setItem('remember-me', 'true');
            localStorage.setItem('saved-username', this.signinForm.value.username);
            localStorage.setItem('saved-password', this.signinForm.value.password);
            this.isRememberedAccount = true;
            this.signinForm.controls['username'].disable();
            this.signinForm.controls['password'].disable();
            this.signinForm.controls['rememberMe'].disable();
          } else {
            localStorage.setItem('remember-me', 'false');
            localStorage.removeItem('saved-username');
            localStorage.removeItem('saved-password');
            this.isRememberedAccount = false;
          }

          localStorage.setItem('keycloak', JSON.stringify(response));
          this.router.navigate([this.returnUrl || '/dashboard']);
        },
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Invalid username or password!',
          });
        }
      });
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  changeAccount(): void {
    this.submitted = false;
    this.signinForm.reset({
      username: '',
      password: '',
      rememberMe: false,
    });
    this.signinForm.controls['username'].enable();
    this.signinForm.controls['password'].enable();
    this.signinForm.controls['rememberMe'].enable();

    localStorage.removeItem('saved-username');
    localStorage.removeItem('saved-password');
    localStorage.removeItem('remember-me');
    this.isRememberedAccount = false;
  }

  onLoginWithProvider(provider: string): void {
    this.keyloakService.onLoginWithProvider(provider)?.then(() => {
      this.router.navigate(['/auth/signup']);
      if (this.keyloakService.isLoggedIn()) {
        this.router.navigate(['/auth/signup']);
      }
    }).catch((error) => {
      this.messageService.add({
        severity: 'error',
        summary: 'Login Error',
        detail: `Failed to login with ${provider}: ${error.message}`
      });
    });
  }

  get f() {
    return this.signinForm.controls;
  }
}
