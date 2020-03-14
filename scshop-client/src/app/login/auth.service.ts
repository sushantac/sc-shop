import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError, BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { User } from './user.model';


interface AuthenticationResponse{
    username: string,  
    token: string

    //TODO
    //refreshToken: string,
    //expiresIn: string,
}

@Injectable({providedIn: "root"})
export class AuthService{

    userSubject:BehaviorSubject<User> = new BehaviorSubject<User>(null);

    private loginUrl: string = "/externalGateway/api/v1/auth/token";
    private signUpUrl: string = "/externalGateway/api/v1/auth/signup";


    constructor(private http: HttpClient, private router: Router){}

    login(username: string, password: string){
        return this.http.post<AuthenticationResponse>(this.loginUrl, 
            {
                username: username,
                password: password
            }
        ).pipe(catchError(this.handleError), tap(this.handleAuthentication.bind(this)));
    }

    signUp(username: string, password: string){
        return this.http.post<AuthenticationResponse>(this.signUpUrl, 
            {
                username: username,
                password: password
            }
        ).pipe(catchError(this.handleError), tap(this.handleSignUp.bind(this)));
    }

    logout(auto:boolean = false){
        this.userSubject.next(null);

        localStorage.removeItem('userData');

        if(auto){
            alert("Your sessions has timed out. Please login again to continue.");
        }else{
            alert("Your have logged out successfully. Thank you for visiting!");
        }

        if(this.timer){
            clearTimeout(this.timer);
        }
        this.timer = null;
    }

    
    timer: any;
    autoLogout(expirationDuration: number){
        this.timer = setTimeout(() => {
            this.logout();
        }, expirationDuration);
    }

    private handleSignUp(response: Object){
        console.log(response);
        
        //const user: User = new User(response.username, response.token);
        //this.userSubject.next(user);
        //localStorage.setItem('userData', JSON.stringify(user));

        //TODO: hard coded expiration for now
        //this.autoLogout(5 * 1000);

        
    }

    private handleAuthentication(response: AuthenticationResponse){
        console.log(response);
        
        const user: User = new User(response.username, response.token);
        this.userSubject.next(user);
        localStorage.setItem('userData', JSON.stringify(user));

        //TODO: hard coded expiration for now
        this.autoLogout(5 * 10000);
    }
    
    private handleError( errorResponse: HttpErrorResponse){
        console.log(errorResponse);

        let erroMessage = "An unknown error occured!";
        if(!errorResponse.error){
            return throwError(erroMessage);
        }

        switch(errorResponse.error.message){
            case 'USERNAME_EXISTS':
                erroMessage = "Email already exists!";
                break;
          
            case 'USER_NOT_FOUND':
                erroMessage = "Email or password is not correct!";
                break;
        }

        return throwError(erroMessage);
    }
}