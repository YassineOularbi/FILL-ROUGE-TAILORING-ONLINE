import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NotificationMailing } from '../../../../core/services/notification-mailing.service';
import { HttpClient } from '@angular/common/http';
import { LoadingComponent } from "../../../../shared/animations/loading/loading.component";

@Component({
  selector: 'app-contact-us',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    LoadingComponent
  ],
  providers: [HttpClient],
  templateUrl: './contact-us.component.html',
  styleUrl: './contact-us.component.scss',
})
export class ContactUsComponent implements OnInit {
  contactForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(private fb: FormBuilder, private notificationMailing: NotificationMailing) {
    this.contactForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      message: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.contactForm.valid) {
      this.isLoading = true;
      const { name, email, phone, message } = this.contactForm.value;
      this.notificationMailing.sendContactForm(name, email, phone, message)
        .subscribe({
          next: () => {
            this.successMessage = 'Email sent successfully';
            this.errorMessage = '';
            this.isLoading = false;
          },
          error: () => {
            this.errorMessage = 'Error during sending your email';
            this.successMessage = '';
            this.isLoading = false;
          }
        });
    }
  }

}
