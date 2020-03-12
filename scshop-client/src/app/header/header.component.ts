import { Component, OnInit, ViewContainerRef, ViewChild, ComponentFactoryResolver, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})

export class HeaderComponent implements OnInit, OnDestroy {

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit() {


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

  ngOnDestroy(){
    if(this.closeSub){
      this.closeSub.unsubscribe();
    }
  }

}
