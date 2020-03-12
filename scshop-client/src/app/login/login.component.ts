import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  @Input() message: string;
  @Output() close: EventEmitter<void> = new EventEmitter<void>(); 

  constructor() { }

  ngOnInit() {
  }

  onClose(){
    this.close.emit();
  }
}
