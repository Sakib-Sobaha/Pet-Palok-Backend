package dev.sabri.securityjwt.scopes.seller.dto;

import dev.sabri.securityjwt.scopes.pets.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarketItemsDTO {
    private String id;
    private String sellerId;
    private String name;
    private String description;
    private String quantity;
    private Integer totalAvailableCount;
    private Float rating;
    private Float pricePerUnit;
    private String type;  // String representation of ProductType
    private String petType;  // String representation of PetType
    private String[] images;
}
