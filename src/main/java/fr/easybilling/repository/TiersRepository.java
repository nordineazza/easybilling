package fr.easybilling.repository;

import fr.easybilling.domain.Tiers;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Tiers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TiersRepository extends JpaRepository<Tiers, Long> {
}
