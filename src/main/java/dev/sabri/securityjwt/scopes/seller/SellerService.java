package dev.sabri.securityjwt.scopes.seller;


import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public List<Seller> getAllSellers(){ return sellerRepository.findAll(); }

    public Seller findSellerById(String id) { return sellerRepository.findById(id).orElse(null); }
}
