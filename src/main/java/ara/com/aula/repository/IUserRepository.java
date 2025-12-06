package ara.com.aula.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ara.com.aula.model.UserModel;

public interface IUserRepository extends JpaRepository <UserModel, UUID> {
    UserModel findByUsername(String username); 
}