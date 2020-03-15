import { Injectable } from '@angular/core';
import { Subject} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Product } from '../common/product.model';
import { environment } from '../../environments/environment';
import { map, tap } from 'rxjs/operators';
import { EventEmitter } from 'protractor';
import { FinalOrder } from './final-order.model';


@Injectable({
  providedIn: 'root'
})
export class OrderService {

  serverUrl: string = environment.serverUrl;

  orderSubmitted: Subject<String> = new Subject<String>();
  
  constructor(private http: HttpClient) { }

  submitOrder(finalOrder: FinalOrder){
    return this.http.post<Response>(this.serverUrl+"/externalGateway/api/v1/orders", finalOrder);
  }

  
}
