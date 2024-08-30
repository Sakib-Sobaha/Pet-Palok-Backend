package dev.sabri.securityjwt.scopes.admin;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    public Admin findAdminById(String id){
        return adminRepository.findById(id).orElse(null);
    }

    public void updateAdminStatus(String adminId, String status){
        Optional<Admin> admin = adminRepository.findById(adminId);
        if(admin.isPresent()) {
            admin.get().setStatus(status);
            adminRepository.save(admin.get());

        } else {
            throw new UsernameNotFoundException("Admin not found with id: " + adminId);
        }
    }
}
