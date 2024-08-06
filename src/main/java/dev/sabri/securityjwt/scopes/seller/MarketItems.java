package dev.sabri.securityjwt.scopes.seller;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MarketItems {
    private String name;
    private String description;
    private Integer quantity;
    private Integer count;
    private Double price;
    private String brand;
    private String imageUrl;

}
