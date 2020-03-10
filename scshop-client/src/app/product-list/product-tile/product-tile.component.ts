import { Component, OnInit, Input } from '@angular/core';
import { Product } from 'src/app/common/product.model';

@Component({
  selector: 'app-product-tile',
  templateUrl: './product-tile.component.html',
  styleUrls: ['./product-tile.component.css']
})
export class ProductTileComponent implements OnInit {

  @Input()
  product:Product = new Product("Name", "https://cdn.pixabay.com/photo/2016/03/27/07/12/apple-1282241_1280.jpg", 200, "Brand");

  constructor() { }

  ngOnInit() {
  }

}
