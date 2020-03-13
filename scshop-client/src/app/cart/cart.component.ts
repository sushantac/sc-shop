import { Component, OnInit } from '@angular/core';
import { CartItem } from './cart-item/cart-item.model';
import { Router, ActivatedRoute } from '@angular/router';
import { Cart } from './cart.model';
import { CartService } from './cart.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cart: Cart;

  cartSubscription:Subscription;

  constructor(private route: ActivatedRoute, private router:Router, private cartService: CartService) {
    
  }

  ngOnInit() {
    this.cart = new Cart();
    this.cart.items = this.cartService.getCartItems();

    this.cartSubscription = this.cartService.cartUpdated.subscribe((cartItems: CartItem[]) => {
      this.cart.items = cartItems;
    });
  }


  onPlaceOrder(){
    this.router.navigate(['order']);
  }
}
