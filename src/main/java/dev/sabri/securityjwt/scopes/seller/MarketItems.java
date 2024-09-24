package dev.sabri.securityjwt.scopes.seller;



import dev.sabri.securityjwt.scopes.pets.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "_marketItems")


public class MarketItems {
    @Id

    private String id;
    private String sellerId;
    private String name;
    private String description;
    private String quantity;
    private Integer totalAvailableCount;
    private Float rating;
    private Float pricePerUnit;
    private ProductType type;
    private PetType petType;
    private String [] images;
    private Integer sold;

    @Override
    public String toString() {
        String s = (this.name + " ") +
        ("sellerId: " + this.sellerId + " price: " + this.pricePerUnit + " rating: " + this.rating) +
        "pettype: " + this.petType + " product type: " + this.type;

        return s;
    }

}
