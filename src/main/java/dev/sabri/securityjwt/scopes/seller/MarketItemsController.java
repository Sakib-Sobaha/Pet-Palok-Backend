package dev.sabri.securityjwt.scopes.seller;

import dev.sabri.securityjwt.scopes.pets.PetType;
import dev.sabri.securityjwt.scopes.seller.dto.MarketItemsDTO;
import dev.sabri.securityjwt.scopes.seller.order.Order;
import dev.sabri.securityjwt.scopes.seller.order.OrderRepository;
import dev.sabri.securityjwt.scopes.seller.order.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/marketplace")
@AllArgsConstructor
public class MarketItemsController {

    @Autowired
    private final MarketItemsRepository marketItemsRepository;
    @Autowired
    private OrderRepository orderRepository;

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
    public ResponseEntity<List<MarketItems>> getItemsBySellerId(@PathVariable("sellerId") String sellerId) {
        System.out.println("Received by sellerId: " + sellerId);
        return ResponseEntity.ok(marketItemsRepository.findBySellerId(sellerId));
    }

    record NewMarketItem(String sellerId, String name, String quantity, Float pricePerUnit, Integer totalAvailableCount,
                         String petType, String type, String description, String[] images) {
    }

    @PostMapping("/addNewItem")
    public ResponseEntity<MarketItems> addNewItem(@RequestBody NewMarketItem newMarketItem) {
        System.out.println("Received create market item request: type: " + newMarketItem.type + " petType: " + newMarketItem.petType + " description: " + newMarketItem.description);

        MarketItems marketItem = new MarketItems();

        if (newMarketItem.sellerId != null)
            marketItem.setSellerId(newMarketItem.sellerId);
        if (newMarketItem.name != null)
            marketItem.setName(newMarketItem.name);
        if (newMarketItem.quantity != null)
            marketItem.setQuantity(newMarketItem.quantity);
        if (newMarketItem.pricePerUnit != null)
            marketItem.setPricePerUnit(newMarketItem.pricePerUnit);
        if (newMarketItem.totalAvailableCount != null)
            marketItem.setTotalAvailableCount(newMarketItem.totalAvailableCount);

        marketItem.setSold(0);
        marketItem.setRating(0f);

        // Handle type normalization and enum mapping
        if (newMarketItem.type != null)
            marketItem.setType(convertToProductType(newMarketItem.type));
        else
            marketItem.setType((ProductType.OTHERS));

        if (newMarketItem.petType != null)
            marketItem.setPetType(convertToPetType(newMarketItem.petType));
        else
            marketItem.setPetType((PetType.OTHERS));

        if (newMarketItem.description != null)
            marketItem.setDescription(newMarketItem.description);

        if (newMarketItem.images != null)
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

    @GetMapping("/item/{itemId}")
    public ResponseEntity<MarketItems> getItem(@PathVariable("itemId") String itemId) {
        MarketItems item = marketItemsRepository.findById(itemId).orElse(null);

        if (item == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else
            return ResponseEntity.ok((item));
    }

    @DeleteMapping("/deleteItem/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") String itemId) {
        System.out.println("Received delete market item request: " + itemId);
        try {
            // Find the item by itemId
            Optional<MarketItems> item = marketItemsRepository.findById(itemId);
            System.out.println("item found: " + item.toString());

            // Check if the item exists in the database
            if (item.isPresent()) {
                marketItemsRepository.deleteById(itemId); // Delete the item if it exists
                return ResponseEntity.ok("Item deleted successfully");
            } else {
                // Return a 404 Not Found if the item does not exist
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while deleting the item: " + e.getMessage());
        }
    }

    record EditItem(Integer count) {
    }

    @PostMapping("/editItem/{itemId}")
    public ResponseEntity<MarketItems> editItem(@PathVariable String itemId, @RequestBody EditItem editItem) {
        Optional<MarketItems> item = marketItemsRepository.findById(itemId);
        if (item.isPresent()) {
            MarketItems marketItem = item.get();
            marketItem.setTotalAvailableCount(marketItem.getTotalAvailableCount() + editItem.count);
            marketItemsRepository.save(marketItem);
            return ResponseEntity.ok(marketItem);
        }
        return ResponseEntity.notFound().build();
    }

//    record Stats1_Seller(Integer uniqueUsers, Integer uniqueProductsSold, Integer ordersCompleted
//                         ) {
//    }
//
//    @GetMapping("/stats1/{sellerId}")
//    public ResponseEntity<Stats1_Seller> getStats1(@PathVariable("sellerId") String sellerId) {
//        // Fetch all market items for the given seller
//        List<MarketItems> marketItems = marketItemsRepository.findBySellerId(sellerId);
//
//        // If no items found for the seller, return not found
//        if (marketItems.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Get the item IDs for this seller
//        List<String> itemIds = marketItems.stream()
//                .map(MarketItems::getId)
//                .collect(Collectors.toList());
//
//        // Fetch all orders containing items sold by this seller
//        List<Order> allOrders = orderRepository.findByItemIdsIn(itemIds);
//
//        // Calculate uniqueUsers (count distinct userId from the orders)
//        Set<String> uniqueUserIds = allOrders.stream()
//                .map(Order::getUserId)
//                .collect(Collectors.toSet());
//        Integer uniqueUsers = uniqueUserIds.size();
//
//        // Calculate uniqueProductsSold (count non-zero sold items for the seller)
//        long uniqueProductsSold = marketItems.stream()
//                .filter(item -> item.getSold() != null && item.getSold() > 0)
//                .count();
//
//        // Calculate ordersCompleted (count of orders with COMPLETED status)
//        long ordersCompleted = allOrders.stream()
//                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
//                .count();
//
////        // Calculate type-wise product count
////        HashMap<ProductType, Integer> productTypeCount = new HashMap<>();
////
////        // Iterate through market items and group them by ProductType
////        marketItems.forEach(item -> {
////            ProductType type = item.getType();
////            productTypeCount.put(type, productTypeCount.getOrDefault(type, 0) + 1);
////        });
//
//        // Construct and return the Stats1_Seller object
//        Stats1_Seller stats = new Stats1_Seller(uniqueUsers, (int) uniqueProductsSold, (int) ordersCompleted);
//
//        return ResponseEntity.ok(stats);
//    }


}
