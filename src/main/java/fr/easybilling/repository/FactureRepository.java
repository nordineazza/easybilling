package fr.easybilling.repository;

import fr.easybilling.domain.Facture;

import fr.easybilling.service.dto.FactureDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Facture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    @Query("SELECT new fr.easybilling.service.dto.FactureDTO(F.id, F.creationDate, F.echeanceDate, F.tva, sum(LF.quantite * LF.prixHt), F.status, " +
    " destinataire.raisonSociale, destinataire.adr1, destinataire.adr2, destinataire.adr3, destinataire.codePostal, destinataire.ville)" +
    "FROM Facture F " +
    "INNER JOIN F.lignes LF ON F.id = LF.facture.id " +
    "INNER JOIN F.entreprise entreprise ON entreprise.id = F.entreprise.id " +
    "INNER JOIN F.destinataire destinataire ON destinataire.id = F.destinataire.id " +
    "WHERE entreprise.id = ?1 " +
    "GROUP BY F.id, F.creationDate, F.echeanceDate, F.tva, F.status, " +
    "destinataire.raisonSociale, destinataire.adr1, destinataire.adr2, destinataire.adr3, destinataire.codePostal, destinataire.ville")
    List<FactureDTO> findFacturesByEntrepriseId(long entrepriseId);

    @Query("SELECT new fr.easybilling.service.dto.FactureDTO(F.id, F.creationDate, F.echeanceDate, F.tva, sum(LF.quantite * LF.prixHt), F.status, " +
    " destinataire.raisonSociale, destinataire.adr1, destinataire.adr2, destinataire.adr3, destinataire.codePostal, destinataire.ville)" +
    "FROM Facture F " +
    "INNER JOIN F.lignes LF ON F.id = LF.facture.id " +
    "INNER JOIN F.entreprise entreprise ON entreprise.id = F.entreprise.id " +
    "INNER JOIN F.destinataire destinataire ON destinataire.id = F.destinataire.id " +
    "WHERE F.id = ?1 " +
    "GROUP BY F.id, F.creationDate, F.echeanceDate, F.tva, F.status, " +
    "destinataire.raisonSociale, destinataire.adr1, destinataire.adr2, destinataire.adr3, destinataire.codePostal, destinataire.ville")
    FactureDTO findFactureById(long id);
}
