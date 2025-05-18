package com.springzr.museio.services.auth.repository;

import com.springzr.museio.services.auth.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Account} entities.
 *
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    /**
     * Finds an account by its email address.
     *
     * @param email the email to search for
     * @return an optional containing the account if found, otherwise empty
     */
    Optional<Account> findByEmail(String email);
}
