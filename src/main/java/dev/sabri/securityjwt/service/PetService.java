package dev.sabri.securityjwt.service;


import dev.sabri.securityjwt.repository.PetRepository;
import dev.sabri.securityjwt.scopes.pets.Pet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PetService {
    private final PetRepository petRepository;

    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    public Pet getById(String id) {
        return petRepository.findById(id).orElse(null);
    }
}
