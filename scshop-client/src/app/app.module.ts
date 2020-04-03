import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER, ApplicationRef, DoBootstrap } from '@angular/core';

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
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthGuard } from './common/auth-guard';
import { AuthInterceptor } from './common/auth.interceptor';
import { KeycloakService } from './common/keycloak.service';

// export function kcFactory(keycloakService: KeycloakService) {
//   return () => keycloakService.init();
// }

const keycloakService = new KeycloakService();

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    CartComponent,
    ProductComponent,
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
  providers: [
    KeycloakService,
    AuthGuard,
    { 
      provide: HTTP_INTERCEPTORS, 
      useClass: AuthInterceptor,
      multi: true
    },
    // {
    //   provide: APP_INITIALIZER,
    //   useFactory: kcFactory,
    //   deps: [KeycloakService],
    //   multi: true
    // }
    {
      provide: KeycloakService,
      useValue: keycloakService
    }
  ],
  //bootstrap: [AppComponent],

  entryComponents: [
    LoginComponent,
    AppComponent
  ]
})
export class AppModule implements DoBootstrap {
  ngDoBootstrap(appRef: ApplicationRef) {
    keycloakService
      .init()
      .then((data) => {
        console.log('[ngDoBootstrap] bootstrap app =====> : ' + data);

        appRef.bootstrap(AppComponent);

        keycloakService.loadUserData(data);
        //resolve();
      })
      .catch(error => console.error('[ngDoBootstrap] init Keycloak failed', error));
  }
}