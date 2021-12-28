package fr.easybilling.service;

import fr.easybilling.domain.Facture;
import fr.easybilling.service.dto.FactureDTO;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Facture}.
 */
public interface FactureService {

    /**
     * Save a facture.
     *
     * @param facture the entity to save.
     * @return the persisted entity.
     */
    Facture save(Facture facture);

    /**
     * Get all the factures.
     *
     * @return the list of entities.
     */
    List<Facture> findAll();


    /**
     * Get the "id" facture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Facture> findOne(Long id);

    /**
     * Delete the "id" facture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get list of factures
     * @return
     */
    List<FactureDTO> findFacturesByEntreprise();

    /**
     * Get facture by id
     *
     * @param id the id of the facture
     * @return
     */
    FactureDTO findFactureById(long id);

    /**
     *
     * @param idFacture
     * @return
     */
    ByteArrayOutputStream generateFactureWithJasper(long idFacture);
}
