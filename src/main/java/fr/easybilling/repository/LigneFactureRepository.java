package fr.easybilling.repository;

import fr.easybilling.domain.LigneFacture;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LigneFacture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneFactureRepository extends JpaRepository<LigneFacture, Long> {
}
