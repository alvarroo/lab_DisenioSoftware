import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // para ngModel en el input
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CircuitComponent } from './circuit/circuit.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { PaymentsComponent } from "./payments/payments.component";

@NgModule({
  declarations: [
    AppComponent,
    CircuitComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    // Incluimos los componentes standalone solo en imports
    LoginComponent,
    RegisterComponent,
    PaymentsComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
