import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // para ngModel en el input
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CircuitComponent } from './circuit/circuit.component';
import { importProvidersFrom } from '@angular/core';
import { LoginComponent } from './login/login.component';
import { PaymentsComponent } from "./payments/payments.component";

@NgModule({
  declarations: [
    AppComponent,
    CircuitComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    PaymentsComponent 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
