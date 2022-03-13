package fr.easybilling.web.rest;

import fr.easybilling.domain.LigneFacture;
import fr.easybilling.repository.LigneFactureRepository;
import fr.easybilling.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.easybilling.domain.LigneFacture}.
 */
@RestController
@RequestMapping("/ligne-factures")
@Transactional
public class LigneFactureResource {

    private final Logger log = LoggerFactory.getLogger(LigneFactureResource.class);

    private static final String ENTITY_NAME = "ligneFacture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneFactureRepository ligneFactureRepository;

    public LigneFactureResource(LigneFactureRepository ligneFactureRepository) {
        this.ligneFactureRepository = ligneFactureRepository;
    }

    /**
     * {@code POST  /ligne-factures} : Create a new ligneFacture.
     *
     * @param ligneFacture the ligneFacture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneFacture, or with status {@code 400 (Bad Request)} if the ligneFacture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<LigneFacture> createLigneFacture(@RequestBody LigneFacture ligneFacture) throws URISyntaxException {
        log.debug("REST request to save LigneFacture : {}", ligneFacture);
        if (ligneFacture.getId() != null) {
            throw new BadRequestAlertException("A new ligneFacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneFacture result = ligneFactureRepository.save(ligneFacture);
        return ResponseEntity.created(new URI("/ligne-factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligne-factures} : Updates an existing ligneFacture.
     *
     * @param ligneFacture the ligneFacture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneFacture,
     * or with status {@code 400 (Bad Request)} if the ligneFacture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneFacture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping
    public ResponseEntity<LigneFacture> updateLigneFacture(@RequestBody LigneFacture ligneFacture) throws URISyntaxException {
        log.debug("REST request to update LigneFacture : {}", ligneFacture);
        if (ligneFacture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LigneFacture result = ligneFactureRepository.save(ligneFacture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneFacture.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ligne-factures} : get all the ligneFactures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneFactures in body.
     */
    @GetMapping
    public List<LigneFacture> getAllLigneFactures() {
        log.debug("REST request to get all LigneFactures");
        return ligneFactureRepository.findAll();
    }

    /**
     * {@code GET  /ligne-factures/:id} : get the "id" ligneFacture.
     *
     * @param id the id of the ligneFacture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneFacture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LigneFacture> getLigneFacture(@PathVariable Long id) {
        log.debug("REST request to get LigneFacture : {}", id);
        Optional<LigneFacture> ligneFacture = ligneFactureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ligneFacture);
    }

    /**
     * {@code DELETE  /ligne-factures/:id} : delete the "id" ligneFacture.
     *
     * @param id the id of the ligneFacture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneFacture(@PathVariable Long id) {
        log.debug("REST request to delete LigneFacture : {}", id);
        ligneFactureRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
