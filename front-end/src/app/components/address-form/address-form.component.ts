import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AddressService } from '../../services/address.service';
import { CepService } from '../../services/cep.service';
import { Address } from '../../models/contact.model';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-address-form',
  templateUrl: './address-form.component.html',
  styleUrls: ['./address-form.component.scss']
})
export class AddressFormComponent implements OnInit {
  addressForm!: FormGroup;
  
  isEditing = false;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private addressService: AddressService,
    private cepService: CepService,
    private dialogRef: MatDialogRef<AddressFormComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { 
      contactId: number, 
      address?: Address,
      contactType: string
    }
  ) {
    this.isEditing = !!data.address;
    this.initializeForm();
  }

  ngOnInit(): void {
    this.setupCepSearch();
  }

  private initializeForm() {
    this.addressForm = this.fb.group({
      cep: ['', Validators.required],
      contryState: ['', Validators.required],
      stateCity: ['', Validators.required],
      cityNeighborhood: ['', Validators.required],
      address: ['', Validators.required],
      addressNumber: ['', Validators.required],
      addressExtra: [''],
    });

    if (this.data.address) {
      this.addressForm.patchValue(this.data.address);
    }
  }

  private setupCepSearch() {
    this.addressForm.get('cep')?.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe(value => {
        if (value && value.length === 8) {
          this.searchCep();
        }
      });
  }

  private searchCep() {
    const cep = this.addressForm.get('cep')?.value;
    this.cepService.searchCep(cep).subscribe({
      next: (response) => {
        if("erro" in response){
          this.snackBar.open('CEP não encontrado', 'Fechar', {
            duration: 3000,
            horizontalPosition: 'end',
            verticalPosition: 'top'
          });
        }
        this.addressForm.patchValue({
          address: response.logradouro,
          addressExtra: response.complemento,
          cityNeighborhood: response.bairro,
          stateCity: response.localidade,
          contryState: response.uf,
        });
      },
      error: (erro) => {
        console.error('Erro ao buscar CEP:', erro);
        this.snackBar.open('CEP não encontrado', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
      }
    });
  }

  onSubmit() {
    if (this.addressForm.valid) {
      this.loading = true;
      const formValue = this.addressForm.value;

      let serviceCall;
      if (this.isEditing) {
        serviceCall = this.addressService.updateAddress(this.data.address!.id, formValue);
      } else {
        serviceCall = this.data.contactType === 'PF' 
          ? this.addressService.addPFAddress(this.data.contactId, formValue)
          : this.addressService.addPJAddress(this.data.contactId, formValue);
      }

      serviceCall.subscribe({
        next: (result) => {
          this.snackBar.open(
            `Endereço ${this.isEditing ? 'atualizado' : 'adicionado'} com sucesso`,
            'Fechar',
            {
              duration: 3000,
              horizontalPosition: 'end',
              verticalPosition: 'top'
            }
          );
          this.dialogRef.close(result);
        },
        error: (error) => {
          console.error('Error saving address:', error);
          this.snackBar.open(
            `Erro ao ${this.isEditing ? 'atualizar' : 'adicionar'} endereço`,
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