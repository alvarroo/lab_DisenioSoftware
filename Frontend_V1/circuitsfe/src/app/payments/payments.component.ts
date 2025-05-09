import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PaymentsService } from '../payments.service';
import { ManagerService } from '../manager.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CircuitComponent } from '../circuit/circuit.component';

declare var Stripe: any;

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class PaymentsComponent {
  stripe = Stripe("pk_test_51R3da8HGC2ak83XFq7ZrqtWxD44MM1bKMbpUt1hF70QG4H19A5rDPUTbje0LScMxhEi39JGbibdkEVKJXMb9maqd00Ip4nAvcL");
  transactionId?: string;
  paymentStatus: 'idle' | 'processing' | 'success' | 'error' = 'idle';
  errorMessage: string = '';

  constructor(
    private service: PaymentsService,
    private manager: ManagerService,
    private router: Router
  ) {}

  ngOnInit() {
    // Verificar si el usuario está autenticado
    if (!this.manager.isAuthenticated) {
      this.router.navigate(['/login']);
    }
  }

  prepay() {
    this.paymentStatus = 'processing';
    this.errorMessage = '';
    
    this.service.prepay().subscribe(
      token => {
        this.transactionId = token;
        this.showForm();
      },
      error => {
        this.paymentStatus = 'error';
        this.errorMessage = 'Error al iniciar el proceso de pago. Por favor intente de nuevo más tarde.';
        console.error('Error en prepay:', error);
      }
    );
  }

  showForm() {
    let elements = this.stripe.elements();
    let style = {
      base: {
        color: "#32325d",
        fontFamily: 'Arial, sans-serif',
        fontSmoothing: "antialiased",
        fontSize: "16px",
        "::placeholder": {
          color: "#32325d"
        },
      },
      invalid: {
        fontFamily: 'Arial, sans-serif',
        color: "#fa755a",
        iconColor: "#fa755a"
      }
    };
    
    let card = elements.create("card", { style: style });
    card.mount("#card-element");
    
    card.on("change", (event: any) => {
      const button = document.querySelector("#submit");
      const cardError = document.querySelector("#card-error");
      if (button) button.setAttribute('disabled', event.empty ? 'true' : 'false');
      if (cardError) cardError.textContent = event.error ? event.error.message : "";
    });
    
    const form = document.getElementById("payment-form");
    if (form) {
      form.addEventListener("submit", (event) => {
        event.preventDefault();
        this.payWithCard(card);
      });
      form.style.display = "block";
    }
  }

  payWithCard(card: any) {
    this.paymentStatus = 'processing';
    document.querySelector("#submit")?.setAttribute('disabled', 'true');
    
    this.stripe.confirmCardPayment(this.transactionId, {
      payment_method: {
        card: card
      }
    }).then((result: any) => {
      if (result.error) {
        this.paymentStatus = 'error';
        this.errorMessage = result.error.message;
        document.querySelector("#submit")?.removeAttribute('disabled');
      } else {
        if (result.paymentIntent.status === 'succeeded') {
          this.paymentStatus = 'success';
          // Confirmar el pago en el backend
          this.confirmPayment();
        }
      }
    }).catch((error: any) => {
      this.paymentStatus = 'error';
      this.errorMessage = 'Ha ocurrido un error al procesar el pago.';
      console.error('Error en confirmCardPayment:', error);
    });
  }

  confirmPayment() {
    if (!this.manager.currentUser) {
      this.errorMessage = 'No se pudo identificar al usuario. Por favor inicie sesión nuevamente.';
      return;
    }
    
    this.service.confirmPayment(this.manager.currentUser).subscribe(
      response => {
        // Pago confirmado exitosamente
        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 3000); // Redirigir después de 3 segundos para mostrar el mensaje de éxito
      },
      error => {
        this.errorMessage = 'El pago se realizó correctamente, pero hubo un error al registrarlo. Por favor contacte con soporte.';
        console.error('Error al confirmar pago:', error);
      }
    );
  }
  
  goBack() {
    this.router.navigate(['/home']);
  }
}
