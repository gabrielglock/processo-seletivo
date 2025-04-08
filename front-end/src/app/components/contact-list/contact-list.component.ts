import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ContactFormComponent } from '../contact-form/contact-form.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { AddressDialogComponent } from '../address-dialog/address-dialog.component';
import { ContactService } from '../../services/contact.service';
import { Contact } from '../../models/contact.model';


@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.scss']
})
export class ContactListComponent implements OnInit {
  contacts: Contact[] = [];
  loading = true;
  pageSize = 30;
  currentPage = 0;
  totalItems = 0;
  
  pageSizeOptions = [10, 20, 30, 50, 100];
  searchTerm = '';
  private searchTimeout: any;

  filterForm: FormGroup;
  private dialogRef?: MatDialogRef<ContactFormComponent>;
  

  constructor(
    private dialog: MatDialog,
    private contactService: ContactService,
    private snackBar: MatSnackBar,
    private fb: FormBuilder
  ) {
    this.filterForm = this.fb.group({
      type: ['todos']
    });

    // Subscribe to filter changes
    this.filterForm.valueChanges.subscribe(() => {
      this.currentPage = 0; // Reset to first page when filter changes
      this.loadContacts();
    });
  }

  ngOnInit() {
    this.loadContacts();
    
    
  }

  onSearch(term: string) {
    this.searchTerm = term;
    // Clear previous timeout
    if (this.searchTimeout) {
      clearTimeout(this.searchTimeout);
    }
    
    // Set new timeout
    this.searchTimeout = setTimeout(() => {
      if (term.trim()) {
        this.searchContacts(term);
      } else {
        this.loadContacts();
      }
    }, 300); // 300ms delay
  }

  clearSearch() {
    this.searchTerm = '';
    this.loadContacts();
  }

  private searchContacts(term: string) {
    this.loading = true;
    const filterType = this.filterForm.get('type')?.value || 'todos';
    
    this.contactService.searchContacts(term).subscribe({
      next: (response) => {
        console.log('Search response:', response);
        // Map all contacts with their types
        const allContacts = response.map((item: any) => ({
          ...item.contato,
          type: item.tipo.toUpperCase()
        }));
        
        // Apply filter if not "todos"
        if (filterType !== 'todos') {
          this.contacts = allContacts.filter((contact: Contact) => 
            contact.type === filterType.toUpperCase()
          );
        } else {
          this.contacts = allContacts;
        }
        
        this.totalItems = this.contacts.length;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error searching contacts:', error);
        this.snackBar.open('Erro ao buscar contatos', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.loading = false;
      }
    });
  }

  loadContacts() {
    this.loading = true;
    const filterType = this.filterForm.get('type')?.value || 'todos';
    
    this.contactService.getContacts(filterType, this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        console.log('API Response:', response); // 
        if (filterType === 'todos') {
          const pfContacts = (response.pf || []).map((contact: Contact) => ({ ...contact, type: 'PF' }));
          const pjContacts = (response.pj || []).map((contact: Contact) => ({ ...contact, type: 'PJ' }));
          this.contacts = [...pfContacts, ...pjContacts];
          this.totalItems = response.totalElements || this.contacts.length;
        } else {
          // Garantir que os contatos mantenham seus tipos
          this.contacts = (response.content || []).map((contact: Contact) => ({
            ...contact,
            type: filterType.toUpperCase()
          }));
          this.totalItems = response.totalElements || 0;
        }
        console.log('Processed Contacts:', this.contacts); // Para debug
        console.log('Total Items:', this.totalItems); // Para debug
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading contacts:', error);
        this.snackBar.open('Erro ao carregar contatos', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.loading = false;
      }
    });
  }

  onPageChange(event: any) {
    this.currentPage= event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadContacts();
  }

  openContactForm(contact?: Contact) {
    console.log(this.dialogRef);
    if (this.dialogRef) {
      this.dialogRef.close();
      return;
    }


    const dialogRef = this.dialog.open(ContactFormComponent, {
      width: '600px',
      data: { contact }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadContacts();
      }
    });
  }

  openDeleteDialog(contact: Contact) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Confirmar Exclusão',
        message: `Tem certeza que deseja excluir o contato ${this.getContactName(contact)}?`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteContact(contact);
      }
    });
  }

  private deleteContact(contact: Contact) {
    this.contactService.deleteContact(contact.id, contact.type).subscribe({
      next: (response) => {
        console.log('Delete response:', response); // Para debug
        this.snackBar.open('Contato excluído com sucesso', 'Fechar', {
          duration: 3000,
          horizontalPosition: 'end',
          verticalPosition: 'top'
        });
        this.loadContacts();
      },
      error: (error) => {
        console.error('Error deleting contact:', error);
        // Só mostra o erro se realmente houver um erro na API
        if (error.status !== 200) {
          this.snackBar.open('Erro ao excluir contato', 'Fechar', {
            duration: 3000,
            horizontalPosition: 'end',
            verticalPosition: 'top'
          });
        } else {
          // Se o status for 200, considera como sucesso
          this.snackBar.open('Contato excluído com sucesso', 'Fechar', {
            duration: 3000,
            horizontalPosition: 'end',
            verticalPosition: 'top'
          });
          this.loadContacts();
        }
      }
    });
  }

  getContactName(contact: Contact): string {
    if (contact.type === 'PF') {
      return `${contact.firstName || ''} ${contact.surName || ''}`.trim();
    }
    return contact.cnpjName || '';
  }

  getContactIdentifier(contact: Contact): string {
    return contact.type === 'PF' ? (contact.cpf || '') : (contact.cnpj || '');
  }

  getContactTypeLabel(type: string): string {
    return type === 'PF' ? 'Pessoa Física' : 'Pessoa Jurídica';
  }

  openAddressDialog(contact: Contact) {
    const dialogRef = this.dialog.open(AddressDialogComponent, {
      width: '800px',
      data: { contact }
    });
  }
} 