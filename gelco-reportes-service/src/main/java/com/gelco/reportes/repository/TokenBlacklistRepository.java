package com.gelco.reportes.repository;

import com.gelco.reportes.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByTokenJti(String tokenJti);
    Optional<TokenBlacklist> findByTokenJti(String tokenJti);
}
