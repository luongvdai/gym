package fpt.gymmanagement.repository;

import fpt.gymmanagement.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);

    @Query(value = "select t from Token t where t.username =:username  and (t.expired=false  or t.revoked=false )")
    List<Token> getByUsername(String username);

    List<Token> getAllByUsername(String username);

    @Query("select t from Token  t where t.createdDate < :time")
    List<Token> getLessThanTime(Long time);
}
