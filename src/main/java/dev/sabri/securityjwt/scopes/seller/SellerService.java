package dev.sabri.securityjwt.scopes.seller;


import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public List<Seller> getAllSellers(){ return sellerRepository.findAll(); }

    public Seller findSellerById(String id) { return sellerRepository.findById(id).orElse(null); }

    public void updateSellerStatus(String sellerId, String status) {
        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);
        if(sellerOptional.isPresent()) {
            sellerOptional.get().setStatus(status);
            sellerRepository.save(sellerOptional.get());
        } else {
            throw new UsernameNotFoundException("Seller not found with id: " + sellerId);
        }
    }
}
