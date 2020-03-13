import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';
import { ProductComponent } from './product/product.component';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { OrderComponent } from './order/order.component';
import { CartItemComponent } from './cart/cart-item/cart-item.component';
import { PlaceholderDirective } from './common/placeholder.directive';
import { OrderSuccessComponent } from './order/order-success/order-success.component';
import { ProductListComponent } from './product/product-list/product-list.component';
import { ErrorPageComponent } from './common/error-page/error-page.component';
import { MenubarComponent } from './header/menubar/menubar.component';
import { ProductTileComponent } from './product/product-list/product-tile/product-tile.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    CartComponent,
    ProductComponent,
    LoginComponent,
    ProfileComponent,
    OrderComponent,
    ProductListComponent,
    ErrorPageComponent,
    MenubarComponent,
    ProductTileComponent,
    CartItemComponent,
    PlaceholderDirective,
    LoginComponent,
    OrderSuccessComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent],

  entryComponents: [
    LoginComponent
  ]
})
export class AppModule { }
