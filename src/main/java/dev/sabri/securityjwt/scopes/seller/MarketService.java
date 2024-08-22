package dev.sabri.securityjwt.scopes.seller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MarketService
{
    private final MarketItemsRepository marketItemsRepository;

    public List<MarketItems> getAllItems()
    {
        return marketItemsRepository.findAll();
    }
}
