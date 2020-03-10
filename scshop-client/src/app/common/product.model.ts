export class Product {
    
    constructor(
        public name: string, 
        public imgUrl: string,
        public price: number,
        public brand: string,         
        public category: string = null, 
        public subCategory: string = null,         
        public availableInventory: number = 1,
        public currency: string = "AUD"
        ){


    }

}
