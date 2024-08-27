package dev.sabri.securityjwt.scopes.seller.order;


import dev.sabri.securityjwt.scopes.user.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "_orders")


public class Order {
    @Id
    private String id;

    private HashMap<String,Integer> itemCountMap = new HashMap<>();
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String alternatePhone;
    private String address;
    private String postOffice;
    private String district;
    private String country;
    private float deliveryFee;
    private Date orderedOn;

    @Override
    public String toString() {
        return
                ("CartItemIds: " + itemCountMap.toString() + "\nname: "+name+"\naddress: "+address);

    }
}
