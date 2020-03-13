import { Injectable } from '@angular/core';
import { Subject} from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Product } from '../common/product.model';
import { environment } from '../../environments/environment';
import { map, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class ProductService {

  serverUrl: string = environment.serverUrl;

  private products: Product[] = [];
 
  productsUpdated: Subject<Product[]> = new Subject<Product[]>();
  
  constructor(private http: HttpClient) { }

  getProducts(){
   this.http.get<Product[]>("/products").pipe(
      map(products => {
            this.products =  products.map(product => {
               return Object.assign(new Product(), product);
            });

            this.productsUpdated.next(this.products);
            return this.products;
        }),
        tap(products => {
            console.log("------>");
            console.log(products);
            //this.products = products;
            
        })
    ).subscribe();
  }


  getProduct(productId: string){
    return this.http.get<Product>("/products/" + productId);
  }
 
  
}
