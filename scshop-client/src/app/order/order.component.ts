import { Component, OnInit, OnDestroy } from '@angular/core';
import { CartPriceDetails } from '../cart/cart-price-details.model';
import { CartItem } from '../cart/cart-item/cart-item.model';
import { Cart } from '../cart/cart.model';
import { CartService } from '../cart/cart.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit, OnDestroy {

  cart: Cart;

  cartSubscription: Subscription;

  constructor(private cartService: CartService) { }

  ngOnInit() {
    this.cart = new Cart();
    this.cart.items = this.cartService.getCartItems();

    this.cartSubscription = this.cartService.cartUpdated.subscribe((cartItems: CartItem[]) => {
      this.cart.items = cartItems;
    });
  }


  ngOnDestroy() {
    this.cartSubscription.unsubscribe();
  }

  

}
