package dev.sabri.securityjwt.scopes.seller;

import dev.sabri.securityjwt.scopes.pets.PetType;
import dev.sabri.securityjwt.scopes.seller.dto.MarketItemsDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/marketplace")
@AllArgsConstructor
public class MarketItemsController {

    @Autowired
    private final MarketItemsRepository marketItemsRepository;

    @GetMapping("/getAllItems")
    public ResponseEntity<List<MarketItemsDTO>> getAllItems() {
        // Fetch all items from the database
        List<MarketItems> items = marketItemsRepository.findAll();

        // Convert each MarketItems object to MarketItemsDTO
        List<MarketItemsDTO> itemDTOs = items.stream().map(item -> {
            MarketItemsDTO dto = new MarketItemsDTO();
            dto.setId(item.getId());
            dto.setSellerId(item.getSellerId());
            dto.setName(item.getName());
            dto.setDescription(item.getDescription());
            dto.setQuantity(item.getQuantity());
            dto.setTotalAvailableCount(item.getTotalAvailableCount());
            dto.setRating(item.getRating());
            dto.setPricePerUnit(item.getPricePerUnit());
            dto.setType(item.getType().name()); // Convert enum to string
            dto.setPetType(item.getPetType().name()); // Convert enum to string
            dto.setImages(item.getImages());
            return dto;
        }).collect(Collectors.toList());

        // Check if the list is empty
        if (itemDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

//        for (MarketItemsDTO dto : itemDTOs) {
//            System.out.println(itemDTOs);
//        }

        // Return the list of DTOs as a JSON response
        return ResponseEntity.ok(itemDTOs);
    }

    @GetMapping("/getItemsBySellerId/{sellerId}")
    public ResponseEntity<Optional<List<MarketItems>>> getItemsBySellerId(@PathVariable("sellerId") String sellerId) {
        System.out.println("Received by sellerId: " + sellerId);
        return ResponseEntity.ok(marketItemsRepository.findBySellerId(sellerId));
    }

    record NewMarketItem(String sellerId, String name, String quantity, Float pricePerUnit, Integer totalAvailableCount, String petType, String type, String description, String[] images) {
    }

    @PostMapping("/addNewItem")
    public ResponseEntity<MarketItems> addNewItem(@RequestBody NewMarketItem newMarketItem) {
        System.out.println("Received create market item request");

        MarketItems marketItem = new MarketItems();

        if(newMarketItem.sellerId != null)
            marketItem.setSellerId(newMarketItem.sellerId);
        if(newMarketItem.name != null)
            marketItem.setName(newMarketItem.name);
        if(newMarketItem.quantity != null)
            marketItem.setQuantity(newMarketItem.quantity);
        if(newMarketItem.pricePerUnit != null)
            marketItem.setPricePerUnit(newMarketItem.pricePerUnit);
        if(newMarketItem.totalAvailableCount != null)
            marketItem.setTotalAvailableCount(newMarketItem.totalAvailableCount);

        marketItem.setRating(0f);

        // Handle type normalization and enum mapping
        if(newMarketItem.type != null)
            marketItem.setType(convertToProductType(newMarketItem.type));
        else
            marketItem.setType((ProductType.OTHERS));

        if(newMarketItem.petType != null)
            marketItem.setPetType(convertToPetType(newMarketItem.petType));
        else
            marketItem.setPetType((PetType.OTHERS));

        if(newMarketItem.description != null)
            marketItem.setDescription(newMarketItem.description);

        if(newMarketItem.images != null)
            marketItem.setImages(newMarketItem.images);


        marketItemsRepository.save(marketItem);
        System.out.println("Added new market item: " + marketItem.toString());
        return ResponseEntity.ok(marketItem);
    }

    // Helper methods for enum conversion without normalization
    private ProductType convertToProductType(String type) {
        try {
            return ProductType.valueOf(type.toUpperCase()); // Convert to uppercase
        } catch (IllegalArgumentException e) {
            return ProductType.OTHERS; // Default value if input doesn't match any enum constant
        }
    }

    private PetType convertToPetType(String petType) {
        try {
            return PetType.valueOf(petType.toUpperCase()); // Convert to uppercase
        } catch (IllegalArgumentException e) {
            return PetType.OTHERS; // Default value if input doesn't match any enum constant
        }
    }
}
