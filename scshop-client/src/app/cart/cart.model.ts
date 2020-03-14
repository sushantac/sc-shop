import { CartPriceDetails } from './cart-price-details.model';
import { CartItem } from './cart-item/cart-item.model';

export class Cart {
    
    
    private _priceDetails: CartPriceDetails;

    constructor(private _items: CartItem[] = []){
        
        this._priceDetails = new CartPriceDetails(0);
        _items.forEach(element => {
            this._priceDetails.price = this._priceDetails.price + (element.price * element.quantity); 
        });

    }

    public set items(items: CartItem[]){
        this._items = items;

        this._priceDetails = new CartPriceDetails(0);
        //this._priceDetails.price = 0;
        this._items.forEach(element => {
            this._priceDetails.price = this._priceDetails.price + (element.price * element.quantity); 
        });
    }

    public get items(){
        return this._items;
    }

    public get priceDetails(){
        return this._priceDetails;
    }

    public get size(){
        return this.items.length;
    }

}
