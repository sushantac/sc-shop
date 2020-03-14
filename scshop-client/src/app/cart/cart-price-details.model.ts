export class CartPriceDetails {
    

    private _totalPrice: number;
    private _delivery: number = 20;

    constructor(public price: number = 0){

    }

    public get delivery(){
        if(this.price === 0){
             return 0;
        }
        this._delivery = 20;
        return this._delivery;
    }

    public get totalPrice(){
       if(this.price === 0){
            return 0;
        }
        return this.price + this.delivery;
    }

    

}
