import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProductService } from '../product.service';
import { Product } from 'src/app/common/product.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnDestroy {

  products: Product[] = [];
  productsSubscription: Subscription;

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productsSubscription = this.productService.productsUpdated.subscribe((products: Product[]) => {
      this.products = products;
      console.log(this.products);
    });

    this.productService.getProducts();
  }

  ngOnDestroy(){
    this.productsSubscription.unsubscribe();
  }

}
