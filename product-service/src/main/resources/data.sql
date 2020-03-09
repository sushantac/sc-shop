INSERT INTO PRODUCT(id, name,brand,category,sub_category,price,currency,img_url, available_inventory) 
VALUES('22335d51265a4e01ad4d306be659a48f','LATITUDE E7450','Dell','Electronics','Computers & Laptops',1000,'AUD','https://s3.ap-southeast-2.amazonaws.com/laptop/latitude-e7450.png', 10000);


INSERT INTO ORDER_SUMMARY(order_id, user_id, inventory_status)
VALUES('14335d51265a4e01ad4d306be659a48f', '34335d51265a4e01ad4d306be659a48f', 'REDUCE_STOCK_UPDATE_INITIATED');