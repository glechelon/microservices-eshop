import { Component } from '@angular/core';
import { Headers, RequestOptions, Http } from '@angular/http';
import { Credentials } from './credentials';
import { AuthResponse } from './authResponse';
import { Customer } from './customer';
import { LoginService } from './login.service';
import { CustomerService } from './customer.service';
import { AuthStatus } from './authStatus';


@Component({
  selector: 'login',
  templateUrl: './login.component.html',
})
export class LoginComponent {

  constructor(private http: Http) { }


  submitted = false
  credentials = new Credentials("mail2@mail.fr", "passijjfeij");
  authResponse = new AuthResponse();
  authStatus = new AuthStatus();
  customer = new Customer();
  loginService = new LoginService(this.http);
  customerService = new CustomerService(this.http);


  auth() {

    this.loginService.authenticate(this.credentials).subscribe(

      response => {
        this.authResponse.code = response.json().code;
        this.authResponse.content = response.json().token;


        if (this.authResponse != null) {

          localStorage.setItem("token", this.authResponse.content);

          let token = localStorage.getItem("token");
          this.customerService.retrieveCustomer(token).subscribe(

            response => {

              this.customer.id = response.json().id;
              this.customer.firstname = response.json().firstname;
              this.customer.lastname = response.json().lastname;
              this.customer.email = response.json().email;
              this.customer.credentials = response.json().credentials;
              this.customer.address = response.json().address;
              this.customer.phoneNumber = response.json().phoneNumber;
              localStorage.setItem("customer", JSON.stringify(this.customer));

            }
          );

        }
      }
    );


  }
  get json() { return JSON.stringify(this.credentials); }
  get authResponseJSON() { return JSON.stringify(this.authResponse); }

}
