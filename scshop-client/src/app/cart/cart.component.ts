import { Component, OnInit } from '@angular/core';
import { Product } from '../common/product.model';
import { CartItem } from './cart-item/cart-item.model';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cartItems: Array<CartItem> = [new CartItem(), new CartItem()];

  constructor() { }

  ngOnInit() {
  }

}
