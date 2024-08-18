package dev.sabri.securityjwt.scopes.admin;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
