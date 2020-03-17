import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from './auth.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  @Input() message: string;
  @Output() close: EventEmitter<void> = new EventEmitter<void>(); 


  errorMessage: string;
  successMessage: string;

  isLoading: boolean;

  signUpInfo: {email: string, password: string};


  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  onLogin(loginForm:NgForm){
    if(!loginForm.valid){
      return;
    }
    this.successMessage = null;
    this.errorMessage = null;
    this.isLoading = true;

    console.log(loginForm.value);

    this.signUpInfo = loginForm.value;

    this.authService.login(this.signUpInfo.email, this.signUpInfo.password)
    .subscribe(response => {
        this.isLoading = false;

        //Close login pop-up
        this.close.emit();

      }, errorMessage => {

        this.errorMessage = errorMessage;
        this.isLoading = false;
      }
    );
    
    
  }
  onSignUp(loginForm:NgForm){
      if(!loginForm.valid){
        return;
      }
      this.successMessage = null;
      this.errorMessage = null;
      this.isLoading = true;

      this.signUpInfo = loginForm.value;
      this.authService.signUp(this.signUpInfo.email, this.signUpInfo.password).subscribe(response => {
          this.errorMessage = null;
          this.successMessage = "You have signed up successfully! You can login now!";
          this.isLoading = false;

      }, errorMessage => {

        this.errorMessage = errorMessage;
        this.isLoading = false;

      })
  }

  onClose(){
    this.close.emit();
  }
}
