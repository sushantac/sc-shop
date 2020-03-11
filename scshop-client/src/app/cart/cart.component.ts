import { Component, OnInit } from '@angular/core';
import { Product } from '../common/product.model';
import { CartItem } from './cart-item/cart-item.model';
import { CartPriceDetails } from './cart-price-details.model';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cartPrice: CartPriceDetails = new CartPriceDetails(0);
  cartItems: Array<CartItem> = [new CartItem(), new CartItem(), new CartItem(), new CartItem(), new CartItem(), new CartItem(), new CartItem()];

  constructor(private route: ActivatedRoute, private router:Router) {

  }

  ngOnInit() {
    this.cartItems.forEach(element => {
      this.cartPrice.price = this.cartPrice.price + (element.price * element.quantity); 
    });
  }


  onPlaceOrder(){
    this.router.navigate(['order']);
  }
}
