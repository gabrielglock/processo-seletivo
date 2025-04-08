import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AddressService } from '../../services/address.service';
import { Contact } from '../../models/contact.model';
import { AddressFormComponent } from '../address-form/address-form.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-address-dialog',
  templateUrl: './address-dialog.component.html',
  styleUrls: ['./address-dialog.component.scss']
})
export class AddressDialogComponent  {
  addresses: any[] = [];
  loading = false;
  contact: Contact;

  constructor(
    private dialogRef: MatDialogRef<AddressDialogComponent>,
    private dialog: MatDialog,
    private addressService: AddressService,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { contact: Contact }
  ) {
    this.contact = data.contact;
    this.loadAddresses();
    
  }

  loadAddresses() {
    this.loading = true;
    if(this.contact.type === 'PF'){
    this.addressService.getPFAddresses(this.contact.id).subscribe({
      next: (addresses) => {
        this.addresses = addresses;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading addresses:', error);
        this.snackBar.open('Erro ao carregar endereços', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.loading = false;
      }
    });
  }
  if(this.contact.type === 'PJ'){
    this.addressService.getPJAddresses(this.contact.id).subscribe({
      next: (addresses) => {
        this.addresses = addresses;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading addresses:', error);
        this.snackBar.open('Erro ao carregar endereços', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.loading = false;
      }
    });
  }
  }


  openAddAddressDialog() {
    const dialogRef = this.dialog.open(AddressFormComponent, {
      width: '600px',
      data: { contactId: this.contact.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadAddresses();
      }
    });
  }

  openEditAddressDialog(address: any) {
    const dialogRef = this.dialog.open(AddressFormComponent, {
      width: '600px',
      data: { 
        contactId: this.contact.id,
        address: address
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadAddresses();
      }
    });
  }

  deleteAddress(addressId: number) {
    this.loading = true;
    const serviceCall = this.contact.type === 'PF'
      ? this.addressService.removePFAddress(this.contact.id, addressId)
      : this.addressService.removePJAddress(this.contact.id, addressId);

    serviceCall.subscribe({
      next: () => {
        this.snackBar.open('Endereço excluído com sucesso', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.loadAddresses();
      },
      error: (error: Error) => {
        console.error('Error deleting address:', error);
        this.snackBar.open('Erro ao excluir endereço', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.loading = false;
      }
    });
  }

  openDeleteDialog(addressId: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar Exclusão',
        message: `Tem certeza que deseja excluir o endereço ?`}
  });

    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.deleteAddress(addressId);
        }
      }
    });
  }

  onClose() {
    this.dialogRef.close();
  }

  getContactName(): string {
    if (this.contact.type === 'PF') {
      return `${this.contact.firstName || ''} ${this.contact.surName || ''}`.trim();
    }
    return this.contact.cnpjName || '';
  }
} 