import { Injectable } from '@angular/core';
import { AuthInfo } from '../login/auth.model';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';

declare var Keycloak: any;

@Injectable()
export class KeycloakService{

  constructor() { }

  authInfoSubject:BehaviorSubject<AuthInfo> = new BehaviorSubject<AuthInfo>(null);
  
  public keycloakAuth: any;

  init(): Promise<any> {
    //const idToken = localStorage.getItem('kc_idToken');
    //const token = localStorage.getItem('kc_token');

    return new Promise((resolve, reject) => {
        const config = {
          
          /** IMPLICIT AUTH FLOW **/
          //url: environment.authUrl + '/auth',
          //realm: 'sc-shop',
          //clientId: 'js-console'
          
          /** AUTHORIZATION_CODE AUTH FLOW **/
          url: environment.authUrl + '/auth',
          realm: 'sc-shop',
          clientId: 'js-console',
          //clientSecret: '2e0d9739-db5c-4a51-8272-034dd0bb6fcf',
          redirectUri: 'http://localhost:4200/home',
          scope: 'openid',
          responseType: 'code'
        };

        this.keycloakAuth = new Keycloak(config);
        this.keycloakAuth.init({ onLoad: 'login-required' /*, token */})
          .then(() => {
            //localStorage.setItem('kc_idToken', this.keycloakAuth.idToken);
            //localStorage.setItem('kc_token', this.keycloakAuth.token);
            this.keycloakAuth.loggedIn = true;

            resolve(this.keycloakAuth);
          })
          .catch(() => {
            reject();
          });
    });
  }

  loadUserData(authData:any){

    //let token = authData.idTokenParsed.sub;
    //this.loadProfile(authData).then((userData) => {
      const authInfo: AuthInfo = new AuthInfo(
        authData.idTokenParsed.sub,
        authData.idTokenParsed.given_name, 
        authData.idTokenParsed.family_name, 
        authData.idTokenParsed.email, 
        authData.idTokenParsed.preferred_username, 
        authData.token
        );
      this.authInfoSubject.next(authInfo);
   // });
  }

  loadProfile(authData:any): Promise<any>{ 
     return new Promise<any>((resolve, reject) => {
      if (authData && authData.token) {
          authData.loadUserProfile()
            .success((userData: any) => {
              resolve(<any>userData);
            })
            .error(() => {
              reject('Failed to load profile...!');
            });
      } else {
        reject('Not logged in...!');
      }
    })
  }

  

  getToken(): string {
    if(!this.keycloakAuth){
      return null;
    }
    return this.keycloakAuth.token;
  }

  logout(){
    this.keycloakAuth.logout();
  }


}
