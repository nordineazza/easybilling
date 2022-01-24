package fr.easybilling.web.rest;

import fr.easybilling.EasyBillingApp;
import fr.easybilling.domain.Tiers;
import fr.easybilling.repository.TiersRepository;
import fr.easybilling.service.TiersService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TiersResource} REST controller.
 */
@SpringBootTest(classes = EasyBillingApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TiersResourceIT {

    private static final String DEFAULT_RAISON_SOCIALE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIALE = "BBBBBBBBBB";

    private static final String DEFAULT_ADR_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADR_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADR_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADR_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADR_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADR_3 = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INSCRIT = false;
    private static final Boolean UPDATED_INSCRIT = true;

    @Autowired
    private TiersRepository tiersRepository;

    @Autowired
    private TiersService tiersService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTiersMockMvc;

    private Tiers tiers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tiers createEntity(EntityManager em) {
        Tiers tiers = new Tiers()
            .raisonSociale(DEFAULT_RAISON_SOCIALE)
            .adr1(DEFAULT_ADR_1)
            .adr2(DEFAULT_ADR_2)
            .adr3(DEFAULT_ADR_3)
            .codePostal(DEFAULT_CODE_POSTAL)
            .ville(DEFAULT_VILLE)
            .pays(DEFAULT_PAYS)
            .email(DEFAULT_EMAIL)
            .inscrit(DEFAULT_INSCRIT);
        return tiers;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tiers createUpdatedEntity(EntityManager em) {
        Tiers tiers = new Tiers()
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .adr1(UPDATED_ADR_1)
            .adr2(UPDATED_ADR_2)
            .adr3(UPDATED_ADR_3)
            .codePostal(UPDATED_CODE_POSTAL)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .email(UPDATED_EMAIL)
            .inscrit(UPDATED_INSCRIT);
        return tiers;
    }

    @BeforeEach
    public void initTest() {
        tiers = createEntity(em);
    }

    @Test
    @Transactional
    public void createTiers() throws Exception {
        int databaseSizeBeforeCreate = tiersRepository.findAll().size();
        // Create the Tiers
        restTiersMockMvc.perform(post("/api/tiers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tiers)))
            .andExpect(status().isCreated());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeCreate + 1);
        Tiers testTiers = tiersList.get(tiersList.size() - 1);
        assertThat(testTiers.getRaisonSociale()).isEqualTo(DEFAULT_RAISON_SOCIALE);
        assertThat(testTiers.getAdr1()).isEqualTo(DEFAULT_ADR_1);
        assertThat(testTiers.getAdr2()).isEqualTo(DEFAULT_ADR_2);
        assertThat(testTiers.getAdr3()).isEqualTo(DEFAULT_ADR_3);
        assertThat(testTiers.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testTiers.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testTiers.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testTiers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTiers.isInscrit()).isEqualTo(DEFAULT_INSCRIT);
    }

    @Test
    @Transactional
    public void createTiersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tiersRepository.findAll().size();

        // Create the Tiers with an existing ID
        tiers.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTiersMockMvc.perform(post("/api/tiers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tiers)))
            .andExpect(status().isBadRequest());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTiers() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        // Get all the tiersList
        restTiersMockMvc.perform(get("/api/tiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tiers.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE)))
            .andExpect(jsonPath("$.[*].adr1").value(hasItem(DEFAULT_ADR_1)))
            .andExpect(jsonPath("$.[*].adr2").value(hasItem(DEFAULT_ADR_2)))
            .andExpect(jsonPath("$.[*].adr3").value(hasItem(DEFAULT_ADR_3)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].inscrit").value(hasItem(DEFAULT_INSCRIT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTiers() throws Exception {
        // Initialize the database
        tiersRepository.saveAndFlush(tiers);

        // Get the tiers
        restTiersMockMvc.perform(get("/api/tiers/{id}", tiers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tiers.getId().intValue()))
            .andExpect(jsonPath("$.raisonSociale").value(DEFAULT_RAISON_SOCIALE))
            .andExpect(jsonPath("$.adr1").value(DEFAULT_ADR_1))
            .andExpect(jsonPath("$.adr2").value(DEFAULT_ADR_2))
            .andExpect(jsonPath("$.adr3").value(DEFAULT_ADR_3))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.inscrit").value(DEFAULT_INSCRIT.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTiers() throws Exception {
        // Get the tiers
        restTiersMockMvc.perform(get("/api/tiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTiers() throws Exception {
        // Initialize the database
        tiersService.save(tiers);

        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();

        // Update the tiers
        Tiers updatedTiers = tiersRepository.findById(tiers.getId()).get();
        // Disconnect from session so that the updates on updatedTiers are not directly saved in db
        em.detach(updatedTiers);
        updatedTiers
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .adr1(UPDATED_ADR_1)
            .adr2(UPDATED_ADR_2)
            .adr3(UPDATED_ADR_3)
            .codePostal(UPDATED_CODE_POSTAL)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .email(UPDATED_EMAIL)
            .inscrit(UPDATED_INSCRIT);

        restTiersMockMvc.perform(put("/api/tiers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTiers)))
            .andExpect(status().isOk());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
        Tiers testTiers = tiersList.get(tiersList.size() - 1);
        assertThat(testTiers.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testTiers.getAdr1()).isEqualTo(UPDATED_ADR_1);
        assertThat(testTiers.getAdr2()).isEqualTo(UPDATED_ADR_2);
        assertThat(testTiers.getAdr3()).isEqualTo(UPDATED_ADR_3);
        assertThat(testTiers.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testTiers.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testTiers.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testTiers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTiers.isInscrit()).isEqualTo(UPDATED_INSCRIT);
    }

    @Test
    @Transactional
    public void updateNonExistingTiers() throws Exception {
        int databaseSizeBeforeUpdate = tiersRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiersMockMvc.perform(put("/api/tiers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tiers)))
            .andExpect(status().isBadRequest());

        // Validate the Tiers in the database
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTiers() throws Exception {
        // Initialize the database
        tiersService.save(tiers);

        int databaseSizeBeforeDelete = tiersRepository.findAll().size();

        // Delete the tiers
        restTiersMockMvc.perform(delete("/api/tiers/{id}", tiers.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tiers> tiersList = tiersRepository.findAll();
        assertThat(tiersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
