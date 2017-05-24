import { Injectable } from '@angular/core';
import { Headers, RequestOptions, Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Cart } from './cart';
import { CartElement } from './cartElement';
import { gatewayUrl } from '../app.routes';
import { SharedService } from '../notifications/shared.service';

@Injectable()
export class CartService {

  private url = gatewayUrl + "/carts";

  constructor(private http: Http, private sharedService : SharedService) { }

  public retrieveCart(customerID: string): Observable<Response> {

    let empty = false;
    return this.http.get(this.url + "/" + customerID).map(

      response => response
    ).catch(
      error => Observable.throw(error));
  }


  public ajouterProduit(id: number, price: number, callback: () => void) {
    let customer = JSON.parse(localStorage['customer']);
    let idCustomer: string = customer['id'];
    this.retrieveCartAndAdd(id, price, idCustomer, callback);

  }

  public removeElement(id: number) {

    let customer = localStorage.getItem("customer");
    let customerID = JSON.parse(customer).id;
    return this.http.delete(this.url + "/" + customerID + "/elements/" + id)
      .map(response => { response.json(); })
      .catch(error => Observable.throw("an error occured"));
  }

  private retrieveCartAndAdd(id: number, price: number, idCustomer: string, callback) {

    let token = localStorage['token'];
    let headers = this.sharedService.getAuthorizationHeader(token)
    let observable: Observable<any> = this.http.get(this.url + '/' + idCustomer, headers)
      .map(this.extractData)
      .catch(this.handleError);
    observable.subscribe(
      cart => {
        this.addCart(cart, id, price, idCustomer, callback);
      },
      error => {
        this.addCartEmpty(id, price, idCustomer);
      }
    );
  }

  /**
  * ajoute le produit à un panier existant, ou increment son nombre
  */
  private addCart(cart: any, id: number, price: number, idCustomer: string, callback): void {
    let elements = cart.cartElements;
    let contains = false;
    if (elements.length == 0) this.addCartEmpty(id, price, idCustomer);
    let elementID = elements.length + 1;
    for (let element of elements) {
      if (element.productID == id) {
        console.log("called");
        console.log(element.quantity);
        element.quantity++;
        console.log(element.quantity);
        contains = true;
        break;
      }
    }
    if (!contains) {
      let product = {
        elementID: elementID,
        productID: id,
        quantity: 1,
        unitPrice: price
      }
      elements.push(product)
    }
    this.createCart(cart, callback);
  }

  /**
  * créer le panier avec le nouveau produit
  */
  private addCartEmpty(id: number, price: number, idCustomer: string): void {
    let data = {
      id: idCustomer,
      cartElements: [{
        elementID: 1,
        productID: id,
        quantity: 1,
        unitPrice: price
      }],
      timeStamp: new Date().getTime() + '',
      customerID: idCustomer,
      totalPrice: 0.000000
    };
    this.createCart(data, () => { });
  }

  private createCart(data: any, callback): void {
    let token = localStorage['token'];
    let headers = this.sharedService.getAuthorizationHeader(token);
    let observable: Observable<any> = this.http.post(this.url, data, headers)
      .map(this.extractData)
      .catch(this.handleError);
    observable.subscribe(
      resp => {
        console.log(resp);
        callback();
      },
      error => {
        console.log("unexecpected error");
      }
    );
  }

  private extractData(res: Response) {
    return res.json() || null;
  }

  private handleError(error: Response | any) {
    return Observable.throw('an error occured');
  }

}
