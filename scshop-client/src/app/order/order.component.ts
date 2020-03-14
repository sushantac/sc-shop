import { Component, OnInit, OnDestroy, ViewChild, ViewContainerRef, ComponentFactoryResolver } from '@angular/core';
import { LoginComponent } from '../login/login.component';
import { CartItem } from '../cart/cart-item/cart-item.model';
import { Cart } from '../cart/cart.model';
import { CartService } from '../cart/cart.service';
import { Subscription } from 'rxjs';
import { CartPriceDetails } from '../cart/cart-price-details.model';
import { AuthService } from '../login/auth.service';
import { User } from '../login/user.model';


@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit, OnDestroy {

  cart: Cart;
  user: User = null;
  cartSubscription: Subscription;

  isUserLoggedIn:boolean = false;
  isAddressEntered:boolean = false;
  isOrderValidated:boolean = false;
  isPaymentValidated:boolean = false;

  constructor( 
    private componentFactoryResolver: ComponentFactoryResolver, 
    private cartService: CartService,
    private authService: AuthService) { 

  }

  ngOnInit() {

    this.authService.userSubject.subscribe( user => {
      this.user = user; 
      this.isUserLoggedIn = (user != null);
    });

    this.cart = new Cart(this.cartService.getCartItems());
                    
    this.cartSubscription = this.cartService.cartUpdated.subscribe((cartItems: CartItem[]) => {
      this.cart.items = cartItems;
    });
  }
  ngOnDestroy() {
    this.cartSubscription.unsubscribe();
  }

 
  public get showAddressSection(): boolean{
    return this.isUserLoggedIn;
  }

  public get showOrderSummarySection(): boolean{
    return this.isUserLoggedIn && this.isAddressEntered;
  }

  public get showPaymentSection(): boolean{
    return this.isUserLoggedIn && this.isAddressEntered;
  }


  @ViewChild('place', {static: true, read: ViewContainerRef}) alertHost: ViewContainerRef;
  private closeSub: Subscription;
  showLoginBox(){
    console.log(this.alertHost);
    const loginComponentFactory = this.componentFactoryResolver.resolveComponentFactory(LoginComponent);

    const hostContainerRef = this.alertHost;

    hostContainerRef.clear();
    let loginBoxInstance = hostContainerRef.createComponent(loginComponentFactory).instance;
    loginBoxInstance.message = "test";
    this.closeSub = loginBoxInstance.close.subscribe(() => {
      this.closeSub.unsubscribe();
      hostContainerRef.clear();
    });
  }

  
  

}
