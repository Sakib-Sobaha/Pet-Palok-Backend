package dev.sabri.securityjwt.scopes.vets;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class VetService {
    private final VetRepository vetRepository;

    public List<Vet> getAll(){
        return vetRepository.findAll();
    }

    public Vet getById(String id) {
        return vetRepository.findById(id).orElse(null);
    }

    public Vet getByEmail(String email) {
        return vetRepository.findByEmail(email).orElse(null);
    }


}
