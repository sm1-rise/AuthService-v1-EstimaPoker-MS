package br.com.samuel.martins.AuthService_v1.repository;

import br.com.samuel.martins.AuthService_v1.model.User;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    Optional <User> findByEmail(String email);
}
