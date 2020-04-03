import { CanActivate } from '@angular/router';
import { OnInit, Injectable } from '@angular/core';
import { KeycloakService } from './keycloak.service';

@Injectable()
export class AuthGuard implements CanActivate, OnInit {

  constructor(private ck: KeycloakService) {
    if (!ck.keycloakAuth) {
        ck.init();
    }
    console.log("INIT AuthGuard: " + ck.keycloakAuth.loggedIn )
  }

  ngOnInit() {

  }

  canActivate() {
    console.log("check guard: " + this.ck.keycloakAuth.loggedIn)
    return this.ck.keycloakAuth.loggedIn;
  }

}