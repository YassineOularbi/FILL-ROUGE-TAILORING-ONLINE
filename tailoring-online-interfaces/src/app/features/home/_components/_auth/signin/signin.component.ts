import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button'
import { InputSwitchModule } from 'primeng/inputswitch'; 

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
  styleUrl: './signin.component.scss'
})
export class SigninComponent implements OnInit {
  signinForm!: FormGroup;
  submitted = false;
  showPassword = false;

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  constructor(private fb: FormBuilder, private messageService: MessageService) {}

  ngOnInit(): void {
    this.signinForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(4)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.signinForm.valid) {
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Sign in successful!' });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please check the form for errors.' });
    }
  }

  get f() {
    return this.signinForm.controls;
  }
}
