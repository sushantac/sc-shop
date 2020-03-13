import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Product } from '../common/product.model';


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private products: Product[] = [new Product(),new Product(),new Product(),new Product(),new Product(),new Product()];
 
  productsUpdated: Subject<Product[]> = new Subject<Product[]>();
  
  constructor(private http: HttpClient) { }

  getProducts(){
    return this.products.slice();
  }
 
  
}
