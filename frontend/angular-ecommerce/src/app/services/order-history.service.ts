import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderHistory } from '../common/order-history';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  private orderUrl = environment.cartifyUrl + '/orders';

  constructor(private httpClient: HttpClient) { }

  getOrderHistory(theEmail: String): Observable<GetResponseOrderHistory> {

    //need to build url based on customer email
    const orderHistroyUrl = `${this.orderUrl}/search/findByCustomerEmailOrderByDateCreatedDesc?email=${theEmail}`;

    return this.httpClient.get<GetResponseOrderHistory>(orderHistroyUrl);
  }
}
interface GetResponseOrderHistory {
  _embedded: {
    orders: OrderHistory[];
  }
}
