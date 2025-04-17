import { Component } from '@angular/core';
import { PaymentsService } from '../payments.service';

let Stripe: any;

@Component({
  selector: 'app-payments',
  imports: [],
  templateUrl: './payments.component.html',
  styleUrl: './payments.component.css'
})
export class PaymentsComponent {

  stripe = Stripe("pk_test_51R3da8HGC2ak83XFq7ZrqtWxD44MM1bKMbpUt1hF70QG4H19A5rDPUTbje0LScMxhEi39JGbibdkEVKJXMb9maqd00Ip4nAvcL");
  transactionId? : string;
constructor(private service: PaymentsService) {}

  prepay(){
    this.service.prepay().subscribe(
      token => {
        this.showForm();
        this.transactionId = token;
      },
    error => {  

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
      const button = document.querySelector("button");
      const cardError = document.querySelector("#card-error");
      if (button) button.disabled = event.empty;
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
    this.stripe.confirmCardPayment(this.transactionId, {
      payment_method: {
        card: card
      }
    }).then((response: any) => {
      if (response.error) {
        alert(response.error.message);
      } else {
        if (response.paymentIntent.status === 'succeeded') {
          alert("Pago exitoso");
          /*this.service.confirm().subscribe({
            next: (response: any) => {
              alert(response);
            }
          });*/
        }
      }
    }).catch((error: any) => {
      alert(error);
    });
  }

  /*confirm(): Observable<any> {
    return this.service.client.get<any>(this.service.httpUrl + "payments/confirm", {
      withCredentials: true,
      observe: "response"
    });
  }*/



}
