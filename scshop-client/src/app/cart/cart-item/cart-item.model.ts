export class CartItem {
    
    constructor(
        public id: string = "",
        public userId: string ="",
        public productId: string ="",
        public quantity: number = 1,
        public productName: string = "Apple MacBook Pro Core i5 8th Gen - (8 GB/256 GB SSD/Mac OS Mojave) MV962HN  (13.3 inch, Space Grey, 1.37 kg)", 
        public price: number = 2200,
        public currency: string = "AUD",
        public imgUrl: string =  "https://cdn.pixabay.com/photo/2016/03/27/07/12/apple-1282241_1280.jpg"
        ){


    }

}
