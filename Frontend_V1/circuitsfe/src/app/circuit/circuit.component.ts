import { Component, OnInit } from '@angular/core';
import { Matrix } from './Matrix';
import { CircuitService } from '../circuit.service'; 
import { ManagerService } from '../manager.service';
import { PaymentsService } from '../payments.service';
import { Router } from '@angular/router';

declare var Stripe: any;

@Component({
  selector: 'app-circuit',
  standalone: false,
  templateUrl: './circuit.component.html',
  styleUrls: ['./circuit.component.css']
})
export class CircuitComponent implements OnInit {
    inputQubits: number; 
    outputQubits: number;
    matrix?: Matrix;
    generatedCode: string = '';
    showCode: boolean = false;
    errorMessage: string = '';
    showPaymentButton: boolean = false;
    showPaymentForm: boolean = false;
    paymentProcessing: boolean = false;
    paymentSuccess: boolean = false;
    
    // Para Stripe
    stripe: any;
    card: any;
    clientSecret: string = '';

    constructor(
      private service: CircuitService, 
      private manager: ManagerService,
      private paymentsService: PaymentsService,
      private router: Router
    ) {
      this.inputQubits = 2;
      this.outputQubits = 2;
    }

    ngOnInit() {
      // Si no hay autenticación y hay una ruta de login, redirigir
      if (!this.manager.isAuthenticated) {
        this.router.navigate(['/login']);
      }
      
      // Cargar el script de Stripe
      this.loadStripeScript();
    }

    loadStripeScript() {
      if (!document.querySelector('#stripe-script')) {
        const script = document.createElement('script');
        script.id = 'stripe-script';
        script.src = 'https://js.stripe.com/v3/';
        script.onload = () => {
          this.stripe = Stripe('pk_test_51R3da8HGC2ak83XFnbip8wiT7af2s4I809w2ox88F3FBZnDJNtpeoy52DyJuqZe7gdpwk8vBrjtbTYG5EcVNlDXc001jN1ntPf');
        };
        document.body.appendChild(script);
      }
    }

    buildMatrix() {
      this.matrix = new Matrix(this.inputQubits, this.outputQubits);
      this.showCode = false;
      this.errorMessage = '';
      this.showPaymentButton = false;
    }

    negate(row: number, col: number) {
      if (this.matrix) {
        this.matrix.table[row][col] = this.matrix.table[row][col] === 0 ? 1 : 0;
      }
    }

    generateCode() {
      this.showCode = false;
      this.errorMessage = '';
      this.showPaymentButton = false;
      
      // Validar que el número de qubits sea válido
      if (this.outputQubits <= 0) {
        this.errorMessage = 'El número de qubits de salida debe ser mayor que 0';
        return;
      }

      if (!this.matrix) {
        this.errorMessage = 'Por favor, construya una matriz primero';
        return;
      }

      this.service.generateCode(this.outputQubits, this.matrix, this.manager.token).subscribe(
        (response: any) => {
          this.generatedCode = response;
          this.showCode = true;
        }, 
        error => {
          console.error("Error al generar el código:", error);
          if (error.status === 402) {
            this.errorMessage = 'Este servicio requiere pago. Por favor, realice un pago para continuar.';
            this.showPaymentButton = true;
          } else {
            this.errorMessage = 'Ocurrió un error al generar el código. Por favor, inténtelo de nuevo.';
          }
        }
      );
    }

    initStripeElements() {
      const elements = this.stripe.elements();
      
      const style = {
        base: {
          color: '#32325d',
          fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
          fontSmoothing: 'antialiased',
          fontSize: '16px',
          '::placeholder': {
            color: '#aab7c4'
          }
        },
        invalid: {
          color: '#fa755a',
          iconColor: '#fa755a'
        }
      };
      
      this.card = elements.create('card', { style });
      
      setTimeout(() => {
        this.card.mount('#card-element');
        
        this.card.on('change', (event: any) => {
          const displayError = document.getElementById('card-errors');
          if (displayError) {
            if (event.error) {
              displayError.textContent = event.error.message;
            } else {
              displayError.textContent = '';
            }
          }
        });
      }, 100);
    }

    cancelPayment() {
      this.showPaymentForm = false;
      if (this.card) {
        this.card.unmount();
      }
    }

    processPayment() {
      this.paymentProcessing = true;
      
      // Primero obtenemos el clientSecret del backend
      this.paymentsService.prepay().subscribe(
        (clientSecret: string) => {
          this.clientSecret = clientSecret;
          
          // Una vez obtenido el clientSecret, confirmamos el pago
          this.stripe.confirmCardPayment(clientSecret, {
            payment_method: {
              card: this.card
            }
          }).then((result: any) => {
            this.paymentProcessing = false;
            
            if (result.error) {
              // Mostrar error al usuario
              const errorElement = document.getElementById('card-errors');
              if (errorElement) {
                errorElement.textContent = result.error.message;
              }
            } else {
              if (result.paymentIntent.status === 'succeeded') {
                // Pago exitoso
                this.paymentSuccess = true;
                
                // Actualizar estado de pago en el backend
                if (this.manager.currentUser) {
                  this.paymentsService.confirmPayment(this.manager.currentUser).subscribe(
                    () => {
                      console.log('Estado de pago actualizado correctamente');
                    },
                    (error) => {
                      console.error('Error al actualizar el estado de pago', error);
                    }
                  );
                } else {
                  console.error('No hay usuario autenticado para actualizar el pago');
                }
              }
            }
          });
        },
        (error) => {
          this.paymentProcessing = false;
          console.error('Error al preparar el pago', error);
          const errorElement = document.getElementById('card-errors');
          if (errorElement) {
            errorElement.textContent = 'Error al preparar el pago. Inténtelo de nuevo más tarde.';
          }
        }
      );
    }

    closePaymentAndGenerateCode() {
      this.showPaymentForm = false;
      this.paymentSuccess = false;
      this.errorMessage = '';
      this.showPaymentButton = false;
      
      // Volvemos a intentar generar el código ahora que el usuario ha pagado
      this.generateCode();
    }

    printCode() {
      const printContent = document.createElement('div');
      printContent.innerHTML = `
        <h2 style="text-align:center;">Código del Circuito Cuántico</h2>
        <pre style="padding: 20px; background-color: #f5f5f5; border-radius: 5px; overflow-x: auto;">${this.generatedCode}</pre>
      `;
      
      const printWindow = window.open('', '_blank');
      if (printWindow) {
        printWindow.document.open();
        printWindow.document.write(`
          <html>
            <head>
              <title>Código de Circuito Cuántico</title>
              <style>
                body { font-family: Arial, sans-serif; padding: 20px; }
                pre { white-space: pre-wrap; font-family: monospace; }
              </style>
            </head>
            <body>
              ${printContent.innerHTML}
            </body>
          </html>
        `);
        printWindow.document.close();
        setTimeout(() => {
          printWindow.print();
        }, 500);
      } else {
        alert('Por favor permita las ventanas emergentes para imprimir el código.');
      }
    }

    logout() {
      this.manager.logout();
      this.router.navigate(['/login']);
    }
}

