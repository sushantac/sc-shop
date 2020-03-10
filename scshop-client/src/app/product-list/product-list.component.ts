import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  products = [1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9];

  constructor() { }

  ngOnInit() {
  }

}
