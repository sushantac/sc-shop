export class OrderItem {
    
    constructor(
        public productId: string,
        public quantity: number,
        public productName: string,
        public price: number,
        public currency: string,
        public imgUrl: string
        ){


    }

}
