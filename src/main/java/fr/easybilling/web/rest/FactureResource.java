package fr.easybilling.web.rest;

import fr.easybilling.domain.Entreprise;
import fr.easybilling.domain.Facture;
import fr.easybilling.service.EntrepriseService;
import fr.easybilling.service.FactureService;
import fr.easybilling.service.dto.FactureDTO;
import fr.easybilling.web.rest.errors.BadRequestAlertException;
import fr.easybilling.web.rest.form.FactureForm;
import fr.easybilling.web.rest.mapper.FactureMapper;
import fr.easybilling.web.rest.response.FactureUpdateResponse;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
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

        if (form.getId() != 0) {
            throw new BadRequestAlertException("A new facture cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Facture facture = factureMapper.mapFormToFactureForCreation(form);

        Optional<Entreprise> entreprise = entrepriseService.findOne(1L);
        entreprise.ifPresent(facture::setEntreprise);

        Facture result = factureService.save(facture);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factures} : Updates an existing facture.
     *
     * @param factureForm the facture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facture,
     * or with status {@code 400 (Bad Request)} if the facture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facture couldn't be updated.
     */
    @PutMapping("/factures")
    public ResponseEntity<Void> updateFacture(@RequestBody FactureForm factureForm) {
        log.debug("REST request to update Facture : {}", factureForm);

        long idFacture = factureForm.getId();

        if (idFacture == 0) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        // Suppression des lignes de facture associées
        factureService.deleteLignesByIdFacture(idFacture);

        // Mise à jour de la facture
        Optional<Facture> factureDb = factureService.findOne(idFacture);
        Facture factureUpdated = factureMapper.mapFormToFactureForUpdate(factureForm, factureDb);
        factureService.save(factureUpdated);

        return ResponseEntity.noContent().build();
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the facture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factures/{id}")
    public ResponseEntity<FactureUpdateResponse> getFacture(@PathVariable Long id) {
        Optional<Facture> optionalFacture = factureService.findOne(id);
        if (isFactureDoesNotExist(optionalFacture)) {
            throw new FactureNotFoundException();
        }

        FactureUpdateResponse facture = factureMapper.mapFactureToResponse(optionalFacture.get());

        return ResponseEntity.ok(facture);
    }

    private boolean isFactureDoesNotExist(Optional<Facture> optionalFacture) {
        return !optionalFacture.isPresent();
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
        Optional<List<FactureDTO>> facture = Optional.ofNullable(factureService.findFacturesByEntreprise());
        return ResponseUtil.wrapOrNotFound(facture);
    }

    @GetMapping(value = "/factures/{id}/pdf")
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
}
