export class Product {
    
    constructor(
        public name: string = "Apple MacBook Pro Core i5 8th Gen - (8 GB/256 GB SSD/Mac OS Mojave) MV962HN  (13.3 inch, Space Grey, 1.37 kg)", 
        public imgUrl: string =  "https://cdn.pixabay.com/photo/2016/03/27/07/12/apple-1282241_1280.jpg",
        public price: number = 2200,
        public brand: string = "Apple",         
        public category: string = null, 
        public subCategory: string = null,         
        public availableInventory: number = 1,
        public currency: string = "AUD",
        public description: string = "Thin and Light Laptop 13.3 inch Quad HD LED Backlit IPS Retina Display (True Tone Technology, Wide Color (P3), 500 nits Brightness)"
        ){


    }

}
