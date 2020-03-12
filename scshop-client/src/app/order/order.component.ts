import { Component, OnInit } from '@angular/core';
import { CartPriceDetails } from '../cart/cart-price-details.model';
import { CartItem } from '../cart/cart-item/cart-item.model';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit {

  cartPrice: CartPriceDetails = new CartPriceDetails(0);
  cartItems: Array<CartItem> = [new CartItem(), new CartItem(), new CartItem()];

  constructor() { }

  ngOnInit() {
    this.cartItems.forEach(element => {
      this.cartPrice.price = this.cartPrice.price + (element.price * element.quantity); 
    });
  }

}
