package fr.easybilling.repository;

import fr.easybilling.domain.Entreprise;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Entreprise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

    @Query("select entreprise from Entreprise entreprise where entreprise.user.login = ?#{principal.username}")
    List<Entreprise> findByUserIsCurrentUser();
}
