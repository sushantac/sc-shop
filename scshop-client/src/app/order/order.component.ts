import { Component, OnInit, OnDestroy, ViewChild, ViewContainerRef, ComponentFactoryResolver } from '@angular/core';
import { LoginComponent } from '../login/login.component';
import { CartItem } from '../cart/cart-item/cart-item.model';
import { Cart } from '../cart/cart.model';
import { CartService } from '../cart/cart.service';
import { Subscription } from 'rxjs';
import { AuthService } from '../login/auth.service';
import { AuthInfo } from '../login/auth.model';
import { NgForm } from '@angular/forms';
import { FinalOrder } from './final-order.model';
import { Address } from './address.model';
import { OrderService } from './order.service';
import { Router } from '@angular/router';
import { OrderItem } from './order-item.model';
import { Payment } from './payment.model';


@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent implements OnInit, OnDestroy {

  finalOrder: FinalOrder = new FinalOrder();

  cart: Cart;
  user: AuthInfo = null;
  cartSubscription: Subscription;

  isUserLoggedIn:boolean = false;
  isAddressEntered:boolean = false;
  isOrderValidated:boolean = false;
  isPaymentValidated:boolean = false;

  orderSubmitted:boolean = false;

  constructor( 
    private componentFactoryResolver: ComponentFactoryResolver, 
    private cartService: CartService,
    private orderService: OrderService, 
    private authService: AuthService,
    private router:Router) { 

  }

  ngOnInit() {

    this.authService.authInfoSubject.subscribe( user => {
      this.user = user; 

      if(user != null){
        this.finalOrder.userId = user.userId;
      }
      this.isUserLoggedIn = (user !== null);
    });

    this.cart = new Cart(this.cartService.getCartItems());
                    
    this.cartSubscription = this.cartService.cartUpdated.subscribe((cartItems: CartItem[]) => {
      this.cart.items = cartItems;
    });
  }

  onAddressSave(addressForm: NgForm){
    if(addressForm.valid){
      console.log("addressForm: " + addressForm);
      this.isAddressEntered = true;

      let address: Address = addressForm.value;

      this.finalOrder.shippingAddress = address;
    }else{
      alert("Please verify your address!");
    }
  }


  onPaySubmit(){

    this.orderSubmitted = true;

    this.finalOrder.items = [];
    this.cart.items.forEach(item => {
      let orderItem: OrderItem = new OrderItem(item.productId, item.quantity, item.productName, item.price, item.currency, item.imgUrl);
      this.finalOrder.items.push(orderItem);
    });

    this.finalOrder.payment = new Payment(this.cart.priceDetails.price,this.cart.priceDetails.delivery);

    this.orderService.submitOrder(this.finalOrder).subscribe(orderId => {

        this.cartService.cartUpdated.next([]);
      this.router.navigate(['order-success'], {state: {orderId: orderId}});
        
    }, (error) =>{

      alert("Order submission failed!");

    });;
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

  
  ngOnDestroy() {
    this.cartSubscription.unsubscribe();
  }
  

}
