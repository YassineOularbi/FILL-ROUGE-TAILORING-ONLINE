import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { InputOtpModule } from 'primeng/inputotp';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError, debounceTime } from 'rxjs/operators';
import { throwError, Subject, Subscription } from 'rxjs';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    InputTextModule,
    ButtonModule,
    InputOtpModule,
    HttpClientModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {
  forgotPasswordForm: FormGroup;
  verifyCodeForm: FormGroup;
  resetPasswordForm: FormGroup;

  step: number = 1;

  submitted = false;
  verifySubmitted = false;
  resetSubmitted = false;

  loading = false;
  verifyLoading = false;
  resetLoading = false;
  resendLoading = false;

  error: string = '';
  success: string = '';
  verifyError: string = '';
  verifySuccess: string = '';
  resetError: string = '';
  resetSuccess: string = '';

  isTyping = false;
  private typingSubject: Subject<void> = new Subject<void>();
  private typingSubscription!: Subscription;

  constructor(private formBuilder: FormBuilder, private http: HttpClient) {
    this.forgotPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.verifyCodeForm = this.formBuilder.group({
      verificationCode: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });

    this.resetPasswordForm = this.formBuilder.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordsMatch });
  }

  ngOnInit(): void {
    this.typingSubscription = this.typingSubject
      .pipe(
        debounceTime(500)
      )
      .subscribe(() => {
        this.isTyping = false;
      });
  }

  ngOnDestroy(): void {
    this.typingSubscription.unsubscribe();
  }

  get f(): { email: AbstractControl } {
    return this.forgotPasswordForm.controls as { email: AbstractControl };
  }

  get v(): { verificationCode: AbstractControl } {
    return this.verifyCodeForm.controls as { verificationCode: AbstractControl };
  }

  get r(): { newPassword: AbstractControl; confirmPassword: AbstractControl } {
    return this.resetPasswordForm.controls as { newPassword: AbstractControl; confirmPassword: AbstractControl };
  }

  passwordsMatch: ValidatorFn = (group: FormGroup): ValidationErrors | null => {
    const password = group.get('newPassword')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordsMismatch: true };
  };

  onInput(): void {
    if (this.f.email.value.trim() !== '') {
      this.isTyping = true;
      this.typingSubject.next();
    } else {
      this.isTyping = false;
    }

    if (this.error) {
      this.error = '';
    }
    if (this.success) {
      this.success = '';
    }
  }

  onSubmitForgotPassword(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.forgotPasswordForm.invalid) {
      return;
    }

    this.loading = true;

    const email = this.f.email.value;

    this.http.post<any>('https://your-api.com/api/reset-password', { email })
      .pipe(
        catchError(err => {
          this.error = err.error.message || 'An error occurred. Please try again.';
          this.loading = false;
          return throwError(err);
        })
      )
      .subscribe(response => {
        this.success = 'An email with reset instructions has been sent.';
        this.loading = false;
        this.forgotPasswordForm.reset();
        this.submitted = false;
        this.step = 2;
      });
  }

  onSubmitVerifyCode(): void {
    this.verifySubmitted = true;
    this.verifyError = '';
    this.verifySuccess = '';

    if (this.verifyCodeForm.invalid) {
      return;
    }

    this.verifyLoading = true;

    const code = this.verifyCodeForm.value.verificationCode;

    this.http.post<any>('https://your-api.com/api/verify-code', { code })
      .pipe(
        catchError(err => {
          this.verifyError = err.error.message || 'Verification failed. Please try again.';
          this.verifyLoading = false;
          return throwError(err);
        })
      )
      .subscribe(response => {
        this.verifySuccess = 'Your account has been successfully authenticated.';
        this.verifyLoading = false;
        this.verifyCodeForm.reset();
        this.verifySubmitted = false;
        this.step = 3;
      });
  }

  onResendCode(): void {
    this.resendLoading = true;
    this.verifyError = '';
    this.verifySuccess = '';

    const email = this.forgotPasswordForm.value.email;

    this.http.post<any>('https://your-api.com/api/resend-code', { email })
      .pipe(
        catchError(err => {
          this.verifyError = err.error.message || 'Failed to resend code. Please try again.';
          this.resendLoading = false;
          return throwError(err);
        })
      )
      .subscribe(response => {
        this.verifySuccess = 'A new verification code has been sent to your email.';
        this.resendLoading = false;
      });
  }

  onSubmitResetPassword(): void {
    this.resetSubmitted = true;
    this.resetError = '';
    this.resetSuccess = '';

    if (this.resetPasswordForm.invalid) {
      return;
    }

    this.resetLoading = true;

    const newPassword = this.resetPasswordForm.value.newPassword;

    this.http.post<any>('https://your-api.com/api/reset-password-final', { newPassword })
      .pipe(
        catchError(err => {
          this.resetError = err.error.message || 'Failed to reset password. Please try again.';
          this.resetLoading = false;
          return throwError(err);
        })
      )
      .subscribe(response => {
        this.resetSuccess = 'Your password has been successfully reset.';
        this.resetLoading = false;
        this.resetPasswordForm.reset();
        this.resetSubmitted = false;
        this.step = 1;
      });
  }
}
