import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { InputOtpModule } from 'primeng/inputotp';
import { catchError, debounceTime } from 'rxjs/operators';
import { throwError, Subject, Subscription } from 'rxjs';
import { NotificationMailing } from '../../../../../core/services/notification-mailing.service';
import { SendingComponent } from "../../../../../shared/animations/sending/sending.component";

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    InputTextModule,
    ButtonModule,
    InputOtpModule,
    SendingComponent
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
  isSending= false;

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
  email: string | null = null;

  isTyping = false;
  private typingSubject: Subject<void> = new Subject<void>();
  private typingSubscription!: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private notificationMailing: NotificationMailing
  ) {
    this.forgotPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.verifyCodeForm = this.formBuilder.group({
      verificationCode: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });

    this.resetPasswordForm = this.formBuilder.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordsMatchValidator });
  }

  ngOnInit(): void {
    this.typingSubscription = this.typingSubject
      .pipe(debounceTime(500))
      .subscribe(() => {
        this.isTyping = false;
      });
  }

  ngOnDestroy(): void {
    this.typingSubscription.unsubscribe();
  }

  get f(): { [key: string]: AbstractControl } {
    return this.forgotPasswordForm.controls;
  }

  get v(): { [key: string]: AbstractControl } {
    return this.verifyCodeForm.controls;
  }

  get r(): { [key: string]: AbstractControl } {
    return this.resetPasswordForm.controls;
  }

  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    if (control instanceof FormGroup) {
      const newPassword = control.get('newPassword')?.value;
      const confirmPassword = control.get('confirmPassword')?.value;

      if (newPassword !== confirmPassword) {
        control.get('confirmPassword')?.setErrors({ mismatch: true });
        return { mismatch: true };
      } else {
        const errors = control.get('confirmPassword')?.errors;
        if (errors) {
          delete errors['mismatch'];
          if (Object.keys(errors).length === 0) {
            control.get('confirmPassword')?.setErrors(null);
          } else {
            control.get('confirmPassword')?.setErrors(errors);
          }
        }
        return null;
      }
    }
    return null;
  }

  onSubmitForgotPassword(): void {
    this.isSending = true
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.forgotPasswordForm.invalid) {
      return;
    }

    this.loading = true;
    this.email = this.f['email'].value;

    this.notificationMailing.sendVerificationCode(this.email!)
      .pipe(
        catchError(err => {
          this.isSending = false
          this.error = 'An error occurred. Please try again.';
          this.loading = false;
          return throwError(err);
        })
      )
      .subscribe(response => {
        this.isSending = false
        this.loading = false;
        this.forgotPasswordForm.reset();
        this.submitted = false;
        this.step = 2;
      });
  }

  onSubmitVerifyCode(): void {
    this.isSending = true
    this.verifySubmitted = true;
    this.verifyError = '';
    this.verifySuccess = '';

    if (this.verifyCodeForm.invalid) {
      return;
    }

    this.verifyLoading = true;
    const email = this.email;
    const code = this.v['verificationCode'].value;

    this.notificationMailing.verifyCode(email!, code)
      .pipe(
        catchError(err => {
          this.isSending = false
          console.log(err);
          this.verifyError = 'Verification failed. Please try again.';
          this.verifyLoading = false;
          return throwError(err);
        })
      )
      .subscribe(response => {
        console.log(response);
        this.isSending = false
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

    this.notificationMailing.sendOTPVerification(email)
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
    const newPassword = this.r['newPassword'].value;

    this.notificationMailing.sendOTPVerification(newPassword)
      .pipe(
        catchError(err => {
          this.resetError = 'Failed to reset password. Please try again.';
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

  onInput(): void {
    if (this.f['email'].value.trim() !== '') {
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
}
