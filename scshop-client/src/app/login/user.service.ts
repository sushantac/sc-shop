import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { User } from './user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

    serverUrl: string = environment.serverUrl;

    constructor(private http: HttpClient) { }

    createUser(user: User){
        return this.http.post<Response>(this.serverUrl+"/externalGateway/api/v1/users/", user);
    }

    getUser(userId: string){
        return this.http.get<User>(this.serverUrl+"/externalGateway/api/v1/users/" + userId);
    }
 
  
}
