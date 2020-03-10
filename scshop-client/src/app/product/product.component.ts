import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../common/product.model';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input()
  product:Product = new Product("Name", "https://cdn.pixabay.com/photo/2016/03/27/07/12/apple-1282241_1280.jpg", 200, "Brand");

  constructor() { }

  ngOnInit() {
  }

}
