package dev.sabri.securityjwt.scopes.vets;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public boolean updateVetStatusByEmail(String email, String status) {
        Optional<Vet> vetOptional = vetRepository.findByEmail(email);
        if(vetOptional.isPresent()) {
            vetOptional.get().setStatus(status);
            vetRepository.save(vetOptional.get());
            return true;
        }
        return false;
    }

    public void updateVetStatus(String vetId, String status) {
        Optional<Vet> vetOptional = vetRepository.findById(vetId);
        if(vetOptional.isPresent()) {
            vetOptional.get().setStatus(status);
            vetRepository.save(vetOptional.get());
        } else {
            throw new UsernameNotFoundException("Vet with id " + vetId + " not found");
        }
    }


}
