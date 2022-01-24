package fr.easybilling.service;

import fr.easybilling.domain.Tiers;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Tiers}.
 */
public interface TiersService {

    /**
     * Save a tiers.
     *
     * @param tiers the entity to save.
     * @return the persisted entity.
     */
    Tiers save(Tiers tiers);

    /**
     * Get all the tiers.
     *
     * @return the list of entities.
     */
    List<Tiers> findAll();


    /**
     * Get the "id" tiers.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tiers> findOne(Long id);

    /**
     * Delete the "id" tiers.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
