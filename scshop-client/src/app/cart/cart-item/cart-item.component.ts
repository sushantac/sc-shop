import { Component, OnInit, Input } from '@angular/core';
import { CartItem } from './cart-item.model';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-cart-item',
  templateUrl: './cart-item.component.html',
  styleUrls: ['./cart-item.component.css']
})
export class CartItemComponent implements OnInit {

  @Input()
  cartItem: CartItem = null;

  constructor(private cartService: CartService) { }

  ngOnInit() {
  }

  onRemoveItemFromCart(productId: string){
    this.cartService.removeItemFromCart(productId);
  }

  onQuanityChanged(){
    this.cartService.cartUpdated.next(this.cartService.cart.items);
  }

}
