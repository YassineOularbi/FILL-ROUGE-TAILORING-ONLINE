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
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { SendingComponent } from "../../../../shared/animations/sending/sending.component";

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
    LoadingComponent,
    ToastModule,
    SendingComponent
],
  providers: [HttpClient, MessageService],
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.scss'],
})
export class ContactUsComponent implements OnInit {
  contactForm: FormGroup;
  isLoading: boolean = false;

  constructor(private fb: FormBuilder, private notificationMailing: NotificationMailing, private messageService: MessageService) {  // Injecter MessageService
    this.contactForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      message: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.contactForm.valid) {
      this.isLoading = true;
      const { name, email, phone, message } = this.contactForm.value;
      
      this.notificationMailing.sendContactForm(name, email, phone, message)
        .subscribe({
          next: (response) => {
            this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Email sent successfully' });
            setTimeout(() => {
              this.isLoading = false;
            }, 1500);
          },
          error: (error) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error sending your email' });
            setTimeout(() => {
              this.isLoading = false;
            }, 1500);
          }
        });
    } else {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please fill in the form correctly.' });
    }
  }
}
