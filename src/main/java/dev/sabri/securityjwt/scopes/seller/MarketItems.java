package dev.sabri.securityjwt.scopes.seller;



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

    private Integer id;
    private String name;
    private String description;
    private Integer quantity;
    private Integer count;
    private Double price;
    private String brand;
    private String imageUrl;

}
