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
    
    // Nuevas propiedades para la recuperación de circuitos
    savedCircuits: any[] = [];
    showSavedCircuits: boolean = false;
    selectedCircuit: any = null;
    
    // Para Stripe
    stripe: any;
    card: any;
    clientSecret: string = '';

    constructor(
      private service: CircuitService, 
      public manager: ManagerService,
      private paymentsService: PaymentsService,
      private router: Router
    ) {
      this.inputQubits = 2;
      this.outputQubits = 2;
    }

    async ngOnInit() {
      // Si no hay autenticación y hay una ruta de login, redirigir
      if (!this.manager.isAuthenticated) {
        this.router.navigate(['/login']);
      }
      console.log('User:', this.manager.currentUser);
      
      try {
        await this.loadStripeScript();
        console.log('Script de Stripe cargado correctamente');
        
        // Cargar circuitos guardados si el usuario está autenticado
        if (this.manager.isAuthenticated && this.manager.currentUser) {
          this.loadSavedCircuits();
        }
      } catch (error) {
        console.error(error);
        this.errorMessage = 'Error al cargar el script de Stripe. Por favor, recargue la página.';
      }
    }

    loadStripeScript() {
      if (!document.querySelector('#stripe-script')) {
        const script = document.createElement('script');
        script.id = 'stripe-script';
        script.src = 'https://js.stripe.com/v3/';
        script.onload = () => {
          this.stripe = Stripe('pk_test_51R3da8HGC2ak83XFq7ZrqtWxD44MM1bKMbpUt1hF70QG4H19A5rDPUTbje0LScMxhEi39JGbibdkEVKJXMb9maqd00Ip4nAvcL');
        };
        document.body.appendChild(script);
      }
    }

    loadSavedCircuits() {
      if (!this.manager.currentUser) return;
      
      this.service.getUserCircuits(this.manager.currentUser).subscribe(
        (circuits) => {
          this.savedCircuits = circuits;
          console.log('Circuitos guardados:', this.savedCircuits);
        },
        (error) => {
          console.error('Error al cargar circuitos guardados:', error);
          this.errorMessage = 'Error al cargar circuitos guardados.';
        }
      );
    }
    
    toggleSavedCircuits() {
      this.showSavedCircuits = !this.showSavedCircuits;
      if (this.showSavedCircuits && this.savedCircuits.length === 0) {
        this.loadSavedCircuits();
      }
    }
    
    loadCircuitById(circuitId: string) {
      this.service.getCircuitById(circuitId).subscribe(
        (circuit) => {
          this.selectedCircuit = circuit;
          this.generatedCode = circuit.code;
          this.showCode = true;
          this.showSavedCircuits = false;
        },
        (error) => {
          console.error('Error al cargar el circuito:', error);
          this.errorMessage = 'Error al cargar el circuito seleccionado.';
        }
      );
    }
    
    downloadCircuitCode(circuit: any) {
      const blob = new Blob([circuit.code], { type: 'text/plain' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `circuito_${circuit.id}.py`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
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
      
      if (!this.matrix) {
        this.errorMessage = 'Por favor, construya una matriz primero';
        return;
      }

      this.service.generateCode(this.outputQubits, this.matrix, this.manager.token, this.manager.currentUser).subscribe(
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
      this.paymentProcessing = false;
      this.paymentSuccess = false;
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
      let self = this
      this.stripe.confirmCardPayment(this.clientSecret, {
        payment_method: {
        card: this.card,
         
      }
      
      }).then((response : any) =>{
        if (response.error) {
          alert(response.error.message);
        } else {
          if (response.paymentIntent.status === 'succeeded') {
          alert("Pago exitoso");
          self.paymentsService.confirmPayment(this.manager.token).subscribe({
            next : (response : any) => {
              this.closePaymentAndGenerateCode();
              alert(response)
            },
            error : (response : any) => {
              alert(response)
            }
          })
        }
      }
    });
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

    handlePaymentButtonClick() {
      this.paymentsService.prepay().subscribe(
        (clientSecret: string) => {
          this.clientSecret = clientSecret;
          this.showPaymentForm = true; // Asegurarse de que el formulario de Stripe sea visible
          setTimeout(() => {
            this.initStripeElements(); // Llamar a initStripeElements después de que el DOM esté listo
          }, 0);
        },
        (error) => {
          console.error('Error during prepay:', error);
          this.errorMessage = 'Error al preparar el pago. Inténtelo de nuevo más tarde.';
        }
      );
    }
    

}

