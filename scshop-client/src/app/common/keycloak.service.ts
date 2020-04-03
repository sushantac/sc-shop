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
    const token = null;//localStorage.getItem('kc_token');
    const refreshToken = null;//localStorage.getItem('kc_refreshToken');

    return new Promise((resolve, reject) => {
        const config = {
          'url': environment.authUrl + '/auth',
          'realm': 'sc-shop',
          'clientId': 'js-console'
        };

        this.keycloakAuth = new Keycloak(config);

        this.keycloakAuth.init({ onLoad: 'login-required', token, refreshToken }) //
          .then(() => {
            localStorage.setItem('kc_token', this.keycloakAuth.token);
            localStorage.setItem('kc_refreshToken', this.keycloakAuth.refreshToken);
            this.keycloakAuth.loggedIn = true;
            //this.keycloakAuth.authz = this.keycloakAuth;

            //this.keycloakAuth.loadUserInfo().then(data => this.test(data))
            resolve(this.keycloakAuth);
          })
          .catch(() => {
            reject();
          });
    });
  }

  loadUserData(authData:any){
   
    let token = authData.token;
    this.loadProfile(authData).then((userData) => {
      const authInfo: AuthInfo = new AuthInfo(userData.firstName, userData.lastName, userData.email, userData.username, token);
      this.authInfoSubject.next(authInfo);
    });
  }

  loadProfile(authData): Promise<any>{ 
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
