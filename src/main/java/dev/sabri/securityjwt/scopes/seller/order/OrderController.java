package dev.sabri.securityjwt.scopes.seller.order;

import java.util.Date;

import dev.sabri.securityjwt.scopes.notifications.Notification;
import dev.sabri.securityjwt.scopes.notifications.NotificationRepository;
import dev.sabri.securityjwt.scopes.notifications.NotificationType;
import dev.sabri.securityjwt.scopes.seller.MarketItems;
import dev.sabri.securityjwt.scopes.seller.MarketItemsRepository;
import dev.sabri.securityjwt.scopes.seller.Seller;
import dev.sabri.securityjwt.scopes.seller.SellerRepository;
import dev.sabri.securityjwt.scopes.seller.review.pending.PendingReview;
import dev.sabri.securityjwt.scopes.seller.review.pending.PendingReviewRepository;
import dev.sabri.securityjwt.scopes.user.User;
import dev.sabri.securityjwt.scopes.user.UserRepository;
import dev.sabri.securityjwt.scopes.user.cart.CartItem;
import dev.sabri.securityjwt.scopes.user.cart.CartItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/order")
@AllArgsConstructor

public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MarketItemsRepository marketItemsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private PendingReviewRepository pendingReviewRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/getOrdersByUserId")
    public ResponseEntity<Optional<List<Order>>> getOrdersByUserId(Principal principal) {
        String email = principal.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String userId = user.get().getId();

            List<Order> orders = orderRepository.findByUserId(userId);

            // Filter orders where any item in the order's itemCountMap belongs to the given sellerId


            return ResponseEntity.ok(Optional.of(orders));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/getOrdersBySellerId")
    public ResponseEntity<Optional<List<Order>>> getOrdersBySellerId(Principal principal) {
        String email = principal.getName();
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if (seller.isPresent()) {
            String sellerId = seller.get().getId();
            // Fetch all orders
            List<Order> allOrders = orderRepository.findAll();

            // Filter orders where any item in the order's itemCountMap belongs to the given sellerId
            List<Order> filteredOrders = allOrders.stream()
                    .filter(order -> order.getItemCountMap().keySet().stream()
                            .anyMatch(itemId -> {
                                Optional<MarketItems> marketItem = marketItemsRepository.findById(itemId);
                                return marketItem.isPresent() && sellerId.equals(marketItem.get().getSellerId());
                            }))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Optional.of(filteredOrders));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    record NewOrder(List<String> cartItemIds, String name, String email, String phone, String alternatePhone,
                    String address, String postOffice, String district, String country, Float deliveryFee) {

    }

    @GetMapping("/accept/{orderId}")
    public ResponseEntity<Optional<Order>> acceptOrder(@PathVariable String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && order.get().getStatus() == OrderStatus.PENDING) {
            Order orderToAccept = order.get();
            orderToAccept.setStatus(OrderStatus.ACCEPTED);
            orderRepository.save(orderToAccept);

            System.out.println("Accepted order" + orderId);

            // generate a notification:
            Notification notification = new Notification();
            notification.setType(NotificationType.ORDER_ACCEPTED);

            // Build the notification text
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Order accepted. Items:\n");

            // Iterate over itemCountMap to append item details
            orderToAccept.getItemCountMap().forEach((itemName, count) -> {
                stringBuilder.append(marketItemsRepository.findById(itemName).get().getName()).append(": ").append(count).append("\n");
            });

            // Set the notification text with item details
            notification.setText(stringBuilder.toString());
            notification.setTimestamp(new Date());
            notification.setReceiver(orderToAccept.getUserId());
            notification.setMainContextId(orderToAccept.getId());
            notification.setUnread(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok(Optional.of(orderToAccept));
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/reject/{orderId}")
    public ResponseEntity<Optional<Order>> rejectOrder(@PathVariable String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && order.get().getStatus() == OrderStatus.PENDING) {
            Order orderToReject = order.get();
            orderToReject.setStatus(OrderStatus.REJECTED);
            orderRepository.save(orderToReject);

            // generate a notification:
            Notification notification = new Notification();
            notification.setType(NotificationType.ORDER_DECLINED);

            // Build the notification text
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Order declined. Items:\n");

            // Iterate over itemCountMap to append item details
            orderToReject.getItemCountMap().forEach((itemName, count) -> {
                stringBuilder.append(marketItemsRepository.findById(itemName).get().getName()).append(": ").append(count).append("\n");
            });

            // Set the notification text with item details
            notification.setText(stringBuilder.toString());
            notification.setTimestamp(new Date());
            notification.setReceiver(orderToReject.getUserId());
            notification.setMainContextId(orderToReject.getId());
            notification.setUnread(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok(Optional.of(orderToReject));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/outForDelivery/{orderId}")
    public ResponseEntity<Optional<Order>> outForDeliveryOrder(@PathVariable String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && order.get().getStatus() == OrderStatus.ACCEPTED) {
            Order order1 = order.get();
            order1.setStatus(OrderStatus.OUT_FOR_DELIVERY);
            orderRepository.save(order1);

            // generate a notification:
            Notification notification = new Notification();
            notification.setType(NotificationType.ORDER_OUT_FOR_DELIVERY);

            // Build the notification text
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Order is out for delivery. Items:\n");

            // Iterate over itemCountMap to append item details
            order1.getItemCountMap().forEach((itemName, count) -> {
                stringBuilder.append(marketItemsRepository.findById(itemName).get().getName()).append(": ").append(count).append("\n");
            });

            // Set the notification text with item details
            notification.setText(stringBuilder.toString());

            notification.setTimestamp(new Date());
            notification.setReceiver(order1.getUserId());
            notification.setMainContextId(order1.getId());
            notification.setUnread(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok(Optional.of(order1));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/delivered/{orderId}")
    public ResponseEntity<Optional<Order>> deliveredOrder(@PathVariable String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent() && order.get().getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            Order order1 = order.get();
            order1.setStatus(OrderStatus.DELIVERED);
            orderRepository.save(order1);

            // Create pending reviews for each item in the order
            createPendingReviewsForOrder(order1);

            // generate a notification:
            Notification notification = new Notification();
            notification.setType(NotificationType.ORDER_OUT_FOR_DELIVERY);

            // Build the notification text
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Order delivered. Items:\n");

            // Iterate over itemCountMap to append item details
            order1.getItemCountMap().forEach((itemName, count) -> {
                stringBuilder.append(marketItemsRepository.findById(itemName).get().getName()).append(": ").append(count).append("\n");
            });

            // Set the notification text with item details
            notification.setText(stringBuilder.toString());

            notification.setTimestamp(new Date());
            notification.setReceiver(order1.getUserId());
            notification.setMainContextId(order1.getId());
            notification.setUnread(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok(Optional.of(order1));
        }

        return ResponseEntity.notFound().build();
    }

    private void createPendingReviewsForOrder(Order order) {
        for (String marketItemId : order.getItemCountMap().keySet()) {
            PendingReview newPendingReview = new PendingReview();
            newPendingReview.setUserId(order.getUserId());
            newPendingReview.setMarketItemId(marketItemId);

            pendingReviewRepository.save(newPendingReview);
        }
    }


    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody NewOrder newOrder, Principal principal) {
        System.out.println("Received create order request");
        System.out.println(newOrder.toString());
        // Retrieve the logged-in user's email
        String email = principal.getName();

        // Fetch the user based on the email
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            System.out.println("user not found");
            return ResponseEntity.notFound().build();
        } else {
            // check valid uid or not
            List<String> cartItemIdsCheck = newOrder.cartItemIds;
            CartItem cartItem = cartItemRepository.findById(cartItemIdsCheck.get(0)).orElse(null);
            if (cartItem == null) {
                System.out.println("cartItem not found");
                return ResponseEntity.notFound().build();
            } else if (!cartItem.getUserId().equals(user.getId())) {
                System.out.println("userId: " + user.getId());
                System.out.println("CartItem UserID: " + cartItem.getUserId());
                System.out.println("cannot place order for other user huh :3");
                return ResponseEntity.badRequest().build();
            } else {
                System.out.println("All ok. initiating new order");
                Order order = new Order();

                order.setUserId(user.getId());
//                order.setCartItemIds(newOrder.cartItemIds);
                order.setName(newOrder.name);
                order.setEmail(newOrder.email);
                order.setPhone(newOrder.phone);
                order.setAlternatePhone(newOrder.alternatePhone);
                order.setAddress(newOrder.address);
                order.setPostOffice(newOrder.postOffice);
                order.setDistrict(newOrder.district);
                order.setCountry(newOrder.country);
                order.setDeliveryFee(newOrder.deliveryFee);

                // set the current time as ordered time and date
                Date date = new Date(new java.util.Date().getTime());
                System.out.println("ordering on : " + date);
                order.setOrderedOn(date);
                order.setStatus(OrderStatus.PENDING);
                System.out.println(order.getStatus());

                HashMap<String, Integer> itemCountMap = new HashMap<>();
                for (String id : newOrder.cartItemIds) {
                    CartItem cartItemTemp = cartItemRepository.findById(id).orElse(null);
                    if (cartItemTemp == null) {
                        System.out.println("cart item not found");
                        return ResponseEntity.notFound().build();
                    } else {
                        String marketItemId = cartItemTemp.getItemId();
                        Integer count = cartItemTemp.getCount();

                        itemCountMap.put(marketItemId, count);

                    }
                }

                order.setItemCountMap(itemCountMap);

                orderRepository.save(order);
                // generate a notification:
                Notification notification = new Notification();
                notification.setType(NotificationType.ORDER_PLACED);

                // Build the notification text
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Order Placed. Items:\n");

                // Iterate over itemCountMap to append item details
                order.getItemCountMap().forEach((itemName, count) -> {
                    stringBuilder.append(marketItemsRepository.findById(itemName).get().getName()).append(": ").append(count).append("\n");
                });

                // Set the notification text with item details
                notification.setText(stringBuilder.toString());

                notification.setTimestamp(new Date());
                notification.setReceiver(order.getUserId());
                notification.setMainContextId(order.getId());
                notification.setUnread(true);
                notificationRepository.save(notification);

                // Generate the notification
                notification.setType(NotificationType.ORDER_RECEIVED);

                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("Order Received. Items:\n");

// Fetch the sellerId from the first item (assuming all items in the order belong to the same seller)
                String sellerId = null;

// Iterate over itemCountMap to append item details and find the sellerId
                for (String itemId : order.getItemCountMap().keySet()) {
                    MarketItems marketItem = marketItemsRepository.findById(itemId).orElse(null);
                    if (marketItem != null) {
                        // Append item details
                        stringBuilder1.append(marketItem.getName())
                                .append(": ")
                                .append(order.getItemCountMap().get(itemId))
                                .append("\n");

                        // Set the sellerId from the first item
                        if (sellerId == null) {
                            sellerId = marketItem.getSellerId();
                        }
                    }
                }

// Set the notification text with item details
                notification.setText(stringBuilder1.toString());
                notification.setTimestamp(new Date());

// Set the sellerId as the notification receiver
                if (sellerId != null) {
                    notification.setReceiver(sellerId);
                } else {
                    throw new RuntimeException("Unable to find sellerId for order " + order.getId());
                }

                notification.setUnread(true);
                notificationRepository.save(notification);


                System.out.println("order created: " + order);
                // time to delete the cartItems

                cartItemRepository.deleteAllById(newOrder.cartItemIds);

                return ResponseEntity.ok(order);
            }

        }
    }

    private OrderStatus convertStringToOrderStatus(String orderStatus) {
        switch (orderStatus.toLowerCase()) {
            case "delivered":
                return OrderStatus.DELIVERED;
            case "pending":
                return OrderStatus.PENDING;
            case "accepted":
                return OrderStatus.ACCEPTED;
            case "rejected":
                return OrderStatus.REJECTED;
            case "out for delivery":
                return OrderStatus.OUT_FOR_DELIVERY;
            default:
                return OrderStatus.UNIDENTIFIED;
        }
    }

    private String convertStatusToString(OrderStatus orderStatus) {
        switch (orderStatus) {
            case OUT_FOR_DELIVERY -> {
                return "Out for delivery";
            }
            case ACCEPTED -> {
                return "Accepted";
            }
            case REJECTED -> {
                return "Rejected";
            }
            case PENDING -> {
                return "Pending";
            }
            case DELIVERED -> {
                return "Delivered";
            }
            default -> {
                return "Unidentified";
            }
        }
    }

}
