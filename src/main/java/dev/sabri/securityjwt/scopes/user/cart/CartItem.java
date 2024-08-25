package dev.sabri.securityjwt.scopes.user.cart;
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
@Document(collection = "_cartItems")


public class CartItem {
    @Id
    private String id;
    private String itemId;
    private String userId;
    private int count;

    @Override
    public String toString() {
        return ("Item id: " + this.itemId + "\n" + this.count + "\nUser id: " + this.userId );
    }
}
