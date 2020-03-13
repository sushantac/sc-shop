import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CartItem } from './cart-item/cart-item.model';
import { Cart } from './cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cart: Cart = new Cart();
   
  cartUpdated: Subject<CartItem[]> = new Subject<CartItem[]>();
  
  constructor(private http: HttpClient) { }

  getNumberOfCartItems(){
    return this.cart.items.length;
  }

  getCartItems(){
    return this.cart.items.slice();
  }
  
  addItemToCart(item: CartItem){
    this.cart.items.push(item);
    this.cartUpdated.next(this.cart.items);

    //API CALL
  }

  removeItemFromCart(productId: string){

    //API CALL

    let deleteIndex: number = -1;
    for (let index = 0; index < this.cart.items.length; index++) {
      const element:CartItem = this.cart.items[index];
      if(element.productId === productId){
        deleteIndex = index;
      }
    }

    this.cart.items.splice(deleteIndex, 1);
    this.cartUpdated.next(this.cart.items);
  }
  
}
