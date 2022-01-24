package fr.easybilling.web.rest;

import fr.easybilling.domain.Tiers;
import fr.easybilling.service.TiersService;
import fr.easybilling.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.easybilling.domain.Tiers}.
 */
@RestController
@RequestMapping("/api")
public class TiersResource {

    private final Logger log = LoggerFactory.getLogger(TiersResource.class);

    private static final String ENTITY_NAME = "tiers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TiersService tiersService;

    public TiersResource(TiersService tiersService) {
        this.tiersService = tiersService;
    }

    /**
     * {@code POST  /tiers} : Create a new tiers.
     *
     * @param tiers the tiers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tiers, or with status {@code 400 (Bad Request)} if the tiers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tiers")
    public ResponseEntity<Tiers> createTiers(@RequestBody Tiers tiers) throws URISyntaxException {
        log.debug("REST request to save Tiers : {}", tiers);
        if (tiers.getId() != null) {
            throw new BadRequestAlertException("A new tiers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tiers result = tiersService.save(tiers);
        return ResponseEntity.created(new URI("/api/tiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tiers} : Updates an existing tiers.
     *
     * @param tiers the tiers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiers,
     * or with status {@code 400 (Bad Request)} if the tiers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tiers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tiers")
    public ResponseEntity<Tiers> updateTiers(@RequestBody Tiers tiers) throws URISyntaxException {
        log.debug("REST request to update Tiers : {}", tiers);
        if (tiers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tiers result = tiersService.save(tiers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiers.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tiers} : get all the tiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tiers in body.
     */
    @GetMapping("/tiers")
    public List<Tiers> getAllTiers() {
        log.debug("REST request to get all Tiers");
        return tiersService.findAll();
    }

    /**
     * {@code GET  /tiers/:id} : get the "id" tiers.
     *
     * @param id the id of the tiers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tiers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tiers/{id}")
    public ResponseEntity<Tiers> getTiers(@PathVariable Long id) {
        log.debug("REST request to get Tiers : {}", id);
        Optional<Tiers> tiers = tiersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tiers);
    }

    /**
     * {@code DELETE  /tiers/:id} : delete the "id" tiers.
     *
     * @param id the id of the tiers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tiers/{id}")
    public ResponseEntity<Void> deleteTiers(@PathVariable Long id) {
        log.debug("REST request to delete Tiers : {}", id);
        tiersService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
