import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import {RouterModule, Routes} from '@angular/router';



import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';

import { AppComponent } from './app.component';
import { ContactListComponent } from './components/contact-list/contact-list.component';
import { ContactFormComponent } from './components/contact-form/contact-form.component';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { AddressDialogComponent } from './components/address-dialog/address-dialog.component';
import { AddressFormComponent } from './components/address-form/address-form.component';
import { CepService } from './services/cep.service';

const routes: Routes = [
  { path: '', component: ContactListComponent },
  { path: 'new', component: ContactFormComponent },
  { path: 'edit/:id', component: ContactFormComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    ContactListComponent,
    ContactFormComponent,
    ConfirmDialogComponent,
    AddressDialogComponent,
    AddressFormComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    NgxMaskModule.forRoot(),
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatRadioModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatPaginatorModule,
    RouterModule.forRoot(routes)
  ],
  providers: [CepService],
  bootstrap: [AppComponent]
})
export class AppModule { }
