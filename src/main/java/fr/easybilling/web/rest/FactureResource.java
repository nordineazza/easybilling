package fr.easybilling.web.rest;

import fr.easybilling.domain.Entreprise;
import fr.easybilling.domain.Facture;
import fr.easybilling.service.EntrepriseService;
import fr.easybilling.service.FactureService;
import fr.easybilling.service.dto.FactureDTO;
import fr.easybilling.web.rest.exception.FactureNotFoundException;
import fr.easybilling.web.rest.form.FactureForm;
import fr.easybilling.web.rest.mapper.FactureMapper;
import fr.easybilling.web.rest.response.FactureUpdateResponse;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.easybilling.domain.Facture}.
 */
@RestController
@RequestMapping("/api")
public class FactureResource {

    private final Logger log = LoggerFactory.getLogger(FactureResource.class);

    private static final String ENTITY_NAME = "facture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactureService factureService;
    private final EntrepriseService entrepriseService;
    private final FactureMapper factureMapper;

    public FactureResource(FactureService factureService,
                           EntrepriseService entrepriseService,
                           FactureMapper factureMapper) {
        this.factureService = factureService;
        this.entrepriseService = entrepriseService;
        this.factureMapper = factureMapper;
    }

    @ExceptionHandler(FactureNotFoundException.class)
    public ResponseEntity<FactureUpdateResponse> handleFactureNotFoundException(Exception e) {
        log.error("facture not found", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param form the facture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facture, or with status {@code 400 (Bad Request)} if the facture has already an ID.
     */
    @PostMapping("/factures")
    public ResponseEntity<Facture> createFacture(@RequestBody FactureForm form) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", form);

        Facture facture = factureMapper.mapFormToFactureForCreation(form);

        List<Entreprise> entreprises = entrepriseService.getEntreprisesOfCurrentUser();
        if (CollectionUtils.isNotEmpty(entreprises)) {
            facture.setEntreprise(entreprises.get(0));
        } else {
            log.error("entreprise does not exist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Facture result = factureService.save(facture);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factures} : Updates an existing facture.
     *
     * @param id the id of the facture to update.
     * @param factureForm the facture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facture,
     * or with status {@code 400 (Bad Request)} if the facture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facture couldn't be updated.
     */
    @PutMapping("/factures/{id}")
    public ResponseEntity<Void> updateFacture(@PathVariable @Min(1) long id, @RequestBody FactureForm factureForm) {
        log.debug("REST request to update Facture : {}", factureForm);

        // Mise à jour de la facture
        Optional<Facture> factureDb = factureService.findOne(id);

        if (factureDb.isPresent()) {
            // Suppression des lignes de facture associées
            factureService.deleteLignesByIdFacture(id);

            Facture factureUpdated = factureMapper.mapFormToFactureForUpdate(factureForm, factureDb);
            factureService.save(factureUpdated);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the facture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/factures/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FactureUpdateResponse> getFacture(@PathVariable Long id) {
        Optional<Facture> optionalFacture = factureService.findOne(id);

        Facture facture = optionalFacture.orElseThrow(FactureNotFoundException::new);

        return ResponseEntity.ok(factureMapper.mapFactureToResponse(facture));
    }

    @GetMapping(value = "/factures/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> getFacturePdf(@PathVariable long id) {

        ByteArrayOutputStream byteArrayOutputStream = factureService.generateFactureWithJasper(id);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=facture_" + id + ".pdf");
        header.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
        header.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));

        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok().headers(header).body(resource);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the facture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        factureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/factures/display-for-datatable")
    public ResponseEntity<List<FactureDTO>> getFacture() {
        List<Entreprise> entreprises = entrepriseService.getEntreprisesOfCurrentUser();
        long entrepriseId = 0;
        if (CollectionUtils.isNotEmpty(entreprises)) {
            entrepriseId = entreprises.get(0).getId();
        }
        Optional<List<FactureDTO>> facture = Optional.ofNullable(factureService.findFacturesByEntrepriseId(entrepriseId));
        return ResponseUtil.wrapOrNotFound(facture);
    }

}
