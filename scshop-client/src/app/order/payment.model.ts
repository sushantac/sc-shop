export class Payment {
   
    public total: number;
    public grandTotal: number;
    
    constructor(
        public subTotal: number,
        public shippingCharges: number = 20,
        public currency: string = 'AUD'
        ) {

        this.total = this.subTotal + this.shippingCharges;
        this.grandTotal = this.subTotal + this.shippingCharges;
    }
      
}