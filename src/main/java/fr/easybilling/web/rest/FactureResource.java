package fr.easybilling.web.rest;

import fr.easybilling.domain.Entreprise;
import fr.easybilling.domain.Facture;
import fr.easybilling.domain.LigneFacture;
import fr.easybilling.domain.Tiers;
import fr.easybilling.service.EntrepriseService;
import fr.easybilling.service.FactureService;
import fr.easybilling.service.dto.FactureDTO;
import fr.easybilling.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static fr.easybilling.web.rest.FactureStatusEnum.*;

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

    public FactureResource(FactureService factureService,
                           EntrepriseService entrepriseService) {
        this.factureService = factureService;
        this.entrepriseService = entrepriseService;
    }

    /**
     * {@code POST  /factures} : Create a new facture.
     *
     * @param facture the facture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facture, or with status {@code 400 (Bad Request)} if the facture has already an ID.
     */
    @PostMapping("/factures")
    public ResponseEntity<Facture> createFacture(@RequestBody FactureForm form) throws URISyntaxException {
        log.debug("REST request to save Facture : {}", form);

        Facture facture = new Facture();
        facture.setCreationDate(LocalDate.now());
        facture.setStatus(EN_COURS.getStatus());
        facture.setTva(form.getTva());
        facture.setEcheanceDate(form.getEcheance());

        Tiers tiers = new Tiers();
        tiers.setRaisonSociale(form.getRaisonSociale());
        tiers.setAdr1(form.getAdr1());
        tiers.setAdr2(form.getAdr2());
        tiers.setAdr3(form.getAdr3());
        tiers.setCodePostale(form.getCodePostal());
        tiers.setVille(form.getVille());
        tiers.setEmail(form.getEmail());
        tiers.setInscrit(false);

        facture.setDestinataire(tiers);
        Optional<Entreprise> entreprise = entrepriseService.findOne(1L);

        facture.setEntreprise(entreprise.get());

        for (FactureForm.LigneFactureForm ligneFactureForm : form.getLignesFacture()) {
            LigneFacture ligneFacture = new LigneFacture();
            ligneFacture.setIntitule(ligneFactureForm.getDetail());
            ligneFacture.setQuantite(ligneFactureForm.getQuantite());
            ligneFacture.setPrixHt(ligneFactureForm.getMontantHT());
            facture.addLignes(ligneFacture);
        }
        Facture result = factureService.save(facture);
        return ResponseEntity.created(new URI("/api/factures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /factures} : Updates an existing facture.
     *
     * @param facture the facture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facture,
     * or with status {@code 400 (Bad Request)} if the facture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/factures")
    public ResponseEntity<Facture> updateFacture(@RequestBody Facture facture) throws URISyntaxException {
        log.debug("REST request to update Facture : {}", facture);
        if (facture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Facture result = factureService.save(facture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facture.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /factures} : get all the factures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factures in body.
     */
    @GetMapping("/factures")
    public List<Facture> getAllFactures() {
        log.debug("REST request to get all Factures");
        return factureService.findAll();
    }

    /**
     * {@code GET  /factures/:id} : get the "id" facture.
     *
     * @param id the id of the facture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/factures/{id}")
    public ResponseEntity<Facture> getFacture(@PathVariable Long id) {
        log.debug("REST request to get Facture : {}", id);
        Optional<Facture> facture = factureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(facture);
    }

    /**
     * {@code DELETE  /factures/:id} : delete the "id" facture.
     *
     * @param id the id of the facture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable Long id) {
        log.debug("REST request to delete Facture : {}", id);
        factureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/factures/dto")
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

    /**
    @GetMapping(value = "/factures/{id}/pdf", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Void> getFacturePdf(@PathVariable long id, HttpServletResponse response) throws IOException {

        try (OutputStream outputStream = response.getOutputStream()) {
            ByteArrayOutputStream byteArrayOutputStream = factureService.generateFactureWithJasper(id);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            outputStream.write(bytes);

            response.setContentLength(bytes.length);
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader("Content-Disposition", "inline");

            return ResponseEntity.ok().build();
        }
    }
    **/
}
