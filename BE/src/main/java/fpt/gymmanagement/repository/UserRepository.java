package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.enums.EUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.username = :usernameLogin or u.email = :usernameLogin")
    Optional<User> findByUsernameOrEmail(String usernameLogin);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByUsernameAndType(String username, EUserType type);

}
