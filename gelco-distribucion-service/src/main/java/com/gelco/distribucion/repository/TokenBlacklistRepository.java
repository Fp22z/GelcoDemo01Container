package com.gelco.distribucion.repository;

import com.gelco.distribucion.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByTokenJti(String tokenJti);
    Optional<TokenBlacklist> findByTokenJti(String tokenJti);
}
