INSERT INTO FINAL_ORDER(id, user_id, name, address_line1, address_line2, city, state, country, status, 
sub_total,shipping_charges,total,grand_total,currency) 
VALUES('14335d51265a4e01ad4d306be659a48f', '34335d51265a4e01ad4d306be659a48f', 'Sushant Chanmanwar', '1 Street', null, 
'Sydney', 'NSW', 'AUSTRALIA', 'INITIATED', 400, 20, 420, 420, 'AUD');

	
	

INSERT INTO ORDER_ITEM(id, order_id, product_id, product_name, quantity, price, currency) 
VALUES('24335d51265a4e01ad4d306be659a48f', '14335d51265a4e01ad4d306be659a48f', '22335d51265a4e01ad4d306be659a48f', 'LATITUDE E7450', 2, 1000, 'AUD');


