import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../common/product.model';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input()
  product:Product = new Product();

  constructor() { }

  ngOnInit() {
  }

}
