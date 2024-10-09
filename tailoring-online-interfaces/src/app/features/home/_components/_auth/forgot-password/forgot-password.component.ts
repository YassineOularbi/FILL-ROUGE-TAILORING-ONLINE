import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
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
    HttpClientModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {
  forgotPasswordForm: FormGroup;
  submitted = false;
  loading = false;
  error: string = '';
  success: string = '';
  isTyping = false;
  private typingSubject: Subject<void> = new Subject<void>();
  private typingSubscription!: Subscription;

  constructor(private formBuilder: FormBuilder, private http: HttpClient) {
    this.forgotPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
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

  get f() {
    return this.forgotPasswordForm.controls;
  }

  onInput(): void {
    if (this.f['email'].value.trim() !== '') {
      this.isTyping = true;
      this.typingSubject.next();
    } else {
      this.isTyping = false;
    }
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = '';
    this.success = '';

    if (this.forgotPasswordForm.invalid) {
      return;
    }

    this.loading = true;

    const email = this.f['email'].value;

    this.http.post<any>('https://votre-api.com/api/reset-password', { email })
      .pipe(
        catchError(err => {
          this.error = err.error.message || 'Une erreur est survenue. Veuillez réessayer.';
          this.loading = false;
          return throwError(err);
        })
      )
      .subscribe(response => {
        this.success = 'Un email de réinitialisation a été envoyé.';
        this.loading = false;
        this.forgotPasswordForm.reset();
        this.submitted = false;
      });
  }
}
