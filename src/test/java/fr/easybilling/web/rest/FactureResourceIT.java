package fr.easybilling.web.rest;

import fr.easybilling.EasyBillingApp;
import fr.easybilling.domain.Facture;
import fr.easybilling.domain.Tiers;
import fr.easybilling.repository.FactureRepository;
import fr.easybilling.service.FactureService;
import fr.easybilling.web.rest.form.FactureForm;
import fr.easybilling.web.rest.form.LigneFactureForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static fr.easybilling.web.rest.FactureStatusEnum.EN_COURS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FactureResource} REST controller.
 */
@SpringBootTest(classes = EasyBillingApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FactureResourceIT {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ECHEANCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ECHEANCE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_TVA = new BigDecimal(1);
    private static final BigDecimal UPDATED_TVA = new BigDecimal(2);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureService factureService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureMockMvc;

    private Facture facture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createEntity(EntityManager em) {
        Facture facture = new Facture()
            .creationDate(DEFAULT_CREATION_DATE)
            .echeanceDate(DEFAULT_ECHEANCE_DATE)
            .tva(DEFAULT_TVA)
            .status(DEFAULT_STATUS);

        Tiers tiers = new Tiers();
        tiers.setRaisonSociale("RAISON_SOCIALE");
        facture.setDestinataire(tiers);
        return facture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facture createUpdatedEntity(EntityManager em) {
        Facture facture = new Facture()
            .creationDate(UPDATED_CREATION_DATE)
            .echeanceDate(UPDATED_ECHEANCE_DATE)
            .tva(UPDATED_TVA)
            .status(UPDATED_STATUS);
        return facture;
    }

    @BeforeEach
    public void initTest() {
        facture = createEntity(em);
    }

    @Disabled("todo")
    @Test
    @Transactional
    void createFacture() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        FactureForm factureForm = getFactureForm();

        // Create the Facture
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureForm)))
            .andExpect(status().isCreated());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate + 1);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(testFacture.getEcheanceDate()).isEqualTo(factureForm.getEcheanceDate());
        assertThat(testFacture.getTva()).isEqualTo(factureForm.getTva());
        assertThat(testFacture.getStatus()).isEqualTo(EN_COURS.getStatus());
    }

    private FactureForm getFactureForm() {
        FactureForm factureForm = new FactureForm();
        factureForm.setEcheanceDate(LocalDate.now());
        factureForm.setTva(new BigDecimal("0.20"));
        factureForm.setEmail("test@mail.fr");
        factureForm.setRaisonSociale("Wayne Corporation");
        factureForm.setAdr1("23 Wall Street Avenue");
        factureForm.setAdr2("2");
        factureForm.setAdr3("3");
        factureForm.setCodePostal("12345");
        factureForm.setVille("Gotham City");

        List<LigneFactureForm> lignes = getLignesFacture();
        factureForm.getLignesFacture().addAll(lignes);
        return factureForm;
    }


    private List<LigneFactureForm> getLignesFacture() {
        LigneFactureForm ligne = new LigneFactureForm();
        ligne.setQuantite(4);
        ligne.setIntitule("Réparation de la BatMobile");
        ligne.setPrixHt(new BigDecimal(10450));
        return Collections.singletonList(ligne);
    }

    @Test
    @Transactional
    void createFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = factureRepository.findAll().size();

        // Create the Facture with an existing ID
        FactureForm factureForm = getFactureForm();
        factureForm.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc.perform(post("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factureForm)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    void getFacture() throws Exception {
        // Initialize the database
        factureRepository.saveAndFlush(facture);

        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", facture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facture.getId().intValue()))
            .andExpect(jsonPath("$.raisonSociale").value(facture.getDestinataire().getRaisonSociale()))
            .andExpect(jsonPath("$.adr1").value(facture.getDestinataire().getAdr1()))
            .andExpect(jsonPath("$.adr2").value(facture.getDestinataire().getAdr2()))
            .andExpect(jsonPath("$.adr3").value(facture.getDestinataire().getAdr3()))
            .andExpect(jsonPath("$.codePostal").value(facture.getDestinataire().getCodePostal()))
            .andExpect(jsonPath("$.ville").value(facture.getDestinataire().getVille()))
            .andExpect(jsonPath("$.email").value(facture.getDestinataire().getEmail()))
            .andExpect(jsonPath("$.tva").value(DEFAULT_TVA.intValue()));

    }
    @Test
    @Transactional
    void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get("/api/factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // Update the facture
        Facture updatedFacture = factureRepository.findById(facture.getId()).get();
        // Disconnect from session so that the updates on updatedFacture are not directly saved in db
        em.detach(updatedFacture);
        updatedFacture
            .creationDate(UPDATED_CREATION_DATE)
            .echeanceDate(UPDATED_ECHEANCE_DATE)
            .tva(UPDATED_TVA)
            .status(UPDATED_STATUS);

        restFactureMockMvc.perform(put("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFacture)))
            .andExpect(status().isNoContent());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
        Facture testFacture = factureList.get(factureList.size() - 1);
        assertThat(testFacture.getEcheanceDate()).isEqualTo(UPDATED_ECHEANCE_DATE);
        assertThat(testFacture.getTva()).isEqualTo(UPDATED_TVA);
    }

    @Test
    @Transactional
    void updateNonExistingFacture() throws Exception {
        int databaseSizeBeforeUpdate = factureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc.perform(put("/api/factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(facture)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacture() throws Exception {
        // Initialize the database
        factureService.save(facture);

        int databaseSizeBeforeDelete = factureRepository.findAll().size();

        // Delete the facture
        restFactureMockMvc.perform(delete("/api/factures/{id}", facture.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facture> factureList = factureRepository.findAll();
        assertThat(factureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
