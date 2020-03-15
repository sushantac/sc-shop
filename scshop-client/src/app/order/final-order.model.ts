import { Address } from './address.model';
import { Payment } from './payment.model';
import { OrderItem } from './order-item.model';

export class FinalOrder {
   
    public userId: string;
    public shippingAddress: Address;
    public payment: Payment;
    public items: OrderItem[] = [];

    constructor() {

    }

}