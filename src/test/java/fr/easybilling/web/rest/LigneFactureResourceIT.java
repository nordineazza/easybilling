package fr.easybilling.web.rest;

import fr.easybilling.EasyBillingApp;
import fr.easybilling.domain.LigneFacture;
import fr.easybilling.repository.LigneFactureRepository;

import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LigneFactureResource} REST controller.
 */
@SpringBootTest(classes = EasyBillingApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LigneFactureResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final BigDecimal DEFAULT_PRIX_HT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_HT = new BigDecimal(2);

    @Autowired
    private LigneFactureRepository ligneFactureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneFactureMockMvc;

    private LigneFacture ligneFacture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneFacture createEntity(EntityManager em) {
        LigneFacture ligneFacture = new LigneFacture()
            .intitule(DEFAULT_INTITULE)
            .quantite(DEFAULT_QUANTITE)
            .prixHt(DEFAULT_PRIX_HT);
        return ligneFacture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneFacture createUpdatedEntity(EntityManager em) {
        LigneFacture ligneFacture = new LigneFacture()
            .intitule(UPDATED_INTITULE)
            .quantite(UPDATED_QUANTITE)
            .prixHt(UPDATED_PRIX_HT);
        return ligneFacture;
    }

    @BeforeEach
    public void initTest() {
        ligneFacture = createEntity(em);
    }

    @Test
    @Transactional
    public void createLigneFacture() throws Exception {
        int databaseSizeBeforeCreate = ligneFactureRepository.findAll().size();
        // Create the LigneFacture
        restLigneFactureMockMvc.perform(post("/ligne-factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneFacture)))
            .andExpect(status().isCreated());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeCreate + 1);
        LigneFacture testLigneFacture = ligneFactureList.get(ligneFactureList.size() - 1);
        assertThat(testLigneFacture.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testLigneFacture.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testLigneFacture.getPrixHt()).isEqualTo(DEFAULT_PRIX_HT);
    }

    @Test
    @Transactional
    public void createLigneFactureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ligneFactureRepository.findAll().size();

        // Create the LigneFacture with an existing ID
        ligneFacture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneFactureMockMvc.perform(post("/ligne-factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneFacture)))
            .andExpect(status().isBadRequest());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLigneFactures() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);

        // Get all the ligneFactureList
        restLigneFactureMockMvc.perform(get("/ligne-factures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneFacture.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].prixHt").value(hasItem(DEFAULT_PRIX_HT.intValue())));
    }

    @Test
    @Transactional
    public void getLigneFacture() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);

        // Get the ligneFacture
        restLigneFactureMockMvc.perform(get("/ligne-factures/{id}", ligneFacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneFacture.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.prixHt").value(DEFAULT_PRIX_HT.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingLigneFacture() throws Exception {
        // Get the ligneFacture
        restLigneFactureMockMvc.perform(get("/ligne-factures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLigneFacture() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);

        int databaseSizeBeforeUpdate = ligneFactureRepository.findAll().size();

        // Update the ligneFacture
        LigneFacture updatedLigneFacture = ligneFactureRepository.findById(ligneFacture.getId()).get();
        // Disconnect from session so that the updates on updatedLigneFacture are not directly saved in db
        em.detach(updatedLigneFacture);
        updatedLigneFacture
            .intitule(UPDATED_INTITULE)
            .quantite(UPDATED_QUANTITE)
            .prixHt(UPDATED_PRIX_HT);

        restLigneFactureMockMvc.perform(put("/ligne-factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLigneFacture)))
            .andExpect(status().isOk());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeUpdate);
        LigneFacture testLigneFacture = ligneFactureList.get(ligneFactureList.size() - 1);
        assertThat(testLigneFacture.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testLigneFacture.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneFacture.getPrixHt()).isEqualTo(UPDATED_PRIX_HT);
    }

    @Test
    @Transactional
    public void updateNonExistingLigneFacture() throws Exception {
        int databaseSizeBeforeUpdate = ligneFactureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneFactureMockMvc.perform(put("/ligne-factures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ligneFacture)))
            .andExpect(status().isBadRequest());

        // Validate the LigneFacture in the database
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLigneFacture() throws Exception {
        // Initialize the database
        ligneFactureRepository.saveAndFlush(ligneFacture);

        int databaseSizeBeforeDelete = ligneFactureRepository.findAll().size();

        // Delete the ligneFacture
        restLigneFactureMockMvc.perform(delete("/ligne-factures/{id}", ligneFacture.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LigneFacture> ligneFactureList = ligneFactureRepository.findAll();
        assertThat(ligneFactureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
