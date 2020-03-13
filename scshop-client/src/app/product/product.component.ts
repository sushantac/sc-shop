import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../common/product.model';
import { CartItem } from '../cart/cart-item/cart-item.model';
import { CartService } from '../cart/cart.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input()
  product:Product = new Product();

  isProductAdded: boolean = false;
  
  constructor(private cartService:CartService) { }

  ngOnInit() {
  }

  onAddToCart(product:Product){

    let cartItem: CartItem = new CartItem();
    cartItem.productId = product.id;
    cartItem.quantity = 1;
    cartItem.productName = product.name;
    cartItem.price = product.price;
    cartItem.currency = product.currency;
    cartItem.imgUrl = product.imgUrl;

    this.cartService.addItemToCart(cartItem);

  }

  onRemoveFromCart(product:Product){

    let cartItem: CartItem = new CartItem();
    cartItem.productId = product.id;
    cartItem.quantity = 1;
    cartItem.productName = product.name;
    cartItem.price = product.price;
    cartItem.currency = product.currency;
    cartItem.imgUrl = product.imgUrl;

    this.cartService.addItemToCart(cartItem);

  }

}
