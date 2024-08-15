package dev.sabri.securityjwt.scopes.admin;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByEmail(String email);
    Admin findAdminById(String id);
}
