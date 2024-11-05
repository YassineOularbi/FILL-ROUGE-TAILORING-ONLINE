import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { BadgeModule } from 'primeng/badge';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { FileUploadModule } from 'primeng/fileupload';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { StepperModule } from 'primeng/stepper';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-become-tailor',
  standalone: true,
  imports: [
    BadgeModule,
    StepperModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    DropdownModule,
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
    IconFieldModule,
    InputIconModule,
    FileUploadModule,
    ToastModule,
  ],
  templateUrl: './become-tailor.component.html',
  styleUrl: './become-tailor.component.scss',
  providers: [MessageService]
})
export class BecomeTailorComponent {
  active = 0;
  userForm: FormGroup;
  submitted = false;
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;
  passwordsMatch: boolean = true;
  totalSize: number = 0;
  totalSizePercent: number = 0;

  genderOptions = [
    { label: 'Female', value: 'Female' },
    { label: 'Male', value: 'Male' }
  ];
  notificationOptions = [
    { label: 'Email', value: 'Email' },
    { label: 'SMS', value: 'SMS' }
  ];

  constructor(private fb: FormBuilder, private messageService: MessageService) {
    this.userForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      dateOfBirth: [null, Validators.required],
      gender: [null, Validators.required],
      notificationPreference: [null, Validators.required],
      profilePicture: [null]
    });
  }

  get f() {
    return this.userForm.controls;
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  validatePasswords() {
    if (this.userForm) {
      const password = this.f['password'].value;
      const confirmPassword = this.f['confirmPassword'].value;
      this.passwordsMatch = password === confirmPassword;
    }
  }

  goToNextStep() {
    this.submitted = true;
    if (this.isCurrentStepValid()) {
      this.active++;
      this.submitted = false;
    }
  }

  isCurrentStepValid(): boolean {
    switch (this.active) {
      case 0:
        return this.f['firstName'].valid &&
               this.f['lastName'].valid &&
               this.f['username'].valid &&
               this.f['email'].valid &&
               this.f['password'].valid &&
               this.f['confirmPassword'].valid;
      case 1:
        return this.f['phoneNumber'].valid &&
               this.f['dateOfBirth'].valid &&
               this.f['gender'].valid &&
               this.f['notificationPreference'].valid;
      case 2:
        return true;
      default:
        return false;
    }
  }

  onSubmit() {
    this.submitted = true;
    if (this.userForm.valid) {
      console.log('Form submitted:', this.userForm.value);
    } else {
      console.log('Please fill in all required fields');
    }
  }

  updateTotalSize(files: File[]) {
    this.totalSize = files.reduce((acc, file) => acc + file.size, 0);
    this.totalSizePercent = (this.totalSize / 1000000) * 100;
  }

  onSelectedFiles(event: any) {
    this.updateTotalSize(event.files);
    this.messageService.add({ severity: 'info', summary: 'File Selected', detail: `${event.files.length} files selected.` });
  }

  onTemplatedUpload() {
    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'File Uploaded Successfully' });
  }

  choose(event: Event, chooseCallback: Function) {
    chooseCallback();
  }

  uploadEvent(uploadCallback: Function) {
    uploadCallback();
  }

  onRemoveTemplatingFile(event: Event, file: any, removeFileCallback: Function, index: number) {
    this.totalSize -= file.size;
    this.totalSizePercent = (this.totalSize / 1000000) * 100;
    removeFileCallback(index);
  }

  formatSize(bytes: number) {
    if (bytes === 0) {
      return '0 Bytes';
    }
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }
}
