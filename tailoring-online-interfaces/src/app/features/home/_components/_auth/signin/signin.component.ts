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
import { UserManagementService } from '../../../../../core/services/user-management.service';
import { AuthRequest } from '../../../../../core/dtos/auth-request.interface';
import { KeycloakAuthService } from '../../../../../core/keycloak/keycloak-auth.service';

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
    InputSwitchModule
  ],
  providers: [MessageService],
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {
  signinForm!: FormGroup;
  submitted = false;
  showPassword = false;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private keycloakAuthService: KeycloakAuthService
  ) {}

  ngOnInit(): void {
    this.signinForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(4)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false],
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.signinForm.valid) {
      const rememberMe = this.signinForm.value.rememberMe;

      this.keycloakAuthService.login(rememberMe).then(() => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Login successful!',
        });
      }).catch(() => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Invalid username or password!',
        });
      });
    }
  }

  forgotPassword(): void {
    this.keycloakAuthService.forgotPassword().then(() => {
      this.messageService.add({
        severity: 'info',
        summary: 'Password Reset',
        detail: 'Redirected to password reset page.',
      });
    }).catch(() => {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Unable to reset password.',
      });
    });
  }

  loginWithProvider(provider: string): void {
    console.log('provider');
    
    this.keycloakAuthService.loginWithIdentityProvider(provider);
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  get f() {
    return this.signinForm.controls;
  }
}