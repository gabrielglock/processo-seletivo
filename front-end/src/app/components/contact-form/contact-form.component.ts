import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ContactService } from '../../services/contact.service';
import { Contact } from '../../models/contact.model';

@Component({
  selector: 'app-contact-form',
  templateUrl: './contact-form.component.html',
  styleUrls: ['./contact-form.component.scss']
})
export class ContactFormComponent {
  contactForm: FormGroup;
  
  isEditing = false;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private contactService: ContactService,
    private dialogRef: MatDialogRef<ContactFormComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { contact?: Contact }
  ) {
    this.contactForm = this.fb.group({
      type: ['PF', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      description: [''],
      
      cpf: [''],
      firstName: [''],
      surName: [''],
      
      cnpj: [''],
      cnpjName: ['']
    });

    if (data?.contact) {
      this.isEditing = true;
      this.contactForm.patchValue(data.contact);
    }

    // Add conditional validators based on contact type
    this.contactForm.get('type')?.valueChanges.subscribe(type => {
      this.updateValidators(type);
    });

    this.updateValidators(this.contactForm.get('type')?.value);
  }

  private updateValidators(type: string) {
    const cpfControl = this.contactForm.get('cpf');
    const firstNameControl = this.contactForm.get('firstName');
    const surNameControl = this.contactForm.get('surName');
    const cnpjControl = this.contactForm.get('cnpj');
    const cnpjNameControl = this.contactForm.get('cnpjName');

    if (type === 'PF') {
      cpfControl?.setValidators([Validators.required]);
      firstNameControl?.setValidators([Validators.required]);
      surNameControl?.setValidators([Validators.required]);
      cnpjControl?.clearValidators();
      cnpjNameControl?.clearValidators();
    } else {
      cpfControl?.clearValidators();
      firstNameControl?.clearValidators();
      surNameControl?.clearValidators();
      cnpjControl?.setValidators([Validators.required]);
      cnpjNameControl?.setValidators([Validators.required]);
    }

    cpfControl?.updateValueAndValidity();
    firstNameControl?.updateValueAndValidity();
    surNameControl?.updateValueAndValidity();
    cnpjControl?.updateValueAndValidity();
    cnpjNameControl?.updateValueAndValidity();
  }

  onSubmit() {
    if (this.contactForm.valid) {
      this.loading = true;
      const formValue = this.contactForm.value;
      const contactData = {
        ...formValue,
        // Remove empty fields based on type
        ...(formValue.type === 'PF' ? {
          cnpj: undefined,
          cnpjName: undefined
        } : {
          cpf: undefined,
          firstName: undefined,
          surName: undefined
        })
      };

      const serviceCall = this.isEditing
        ? this.contactService.updateContact(this.data.contact!.id, contactData,)
        : formValue.type === 'PF'
          ? this.contactService.createPFContact(contactData)
          : this.contactService.createPJContact(contactData);

      serviceCall.subscribe({
        next: (result: Contact) => {
          this.snackBar.open(
            `Contato ${this.isEditing ? 'atualizado' : 'criado'} com sucesso`,
            'Fechar',
            {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            }
          );
          this.dialogRef.close(result);
        },
        error: (error: any) => {
          console.error('Error saving contact:', error);
          this.snackBar.open(
            `Erro ao ${this.isEditing ? 'atualizar' : 'criar'} contato`,
            'Fechar',
            {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            }
          );
          this.loading = false;
        }
      });
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
} 