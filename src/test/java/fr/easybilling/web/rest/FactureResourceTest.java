package fr.easybilling.web.rest;

import fr.easybilling.domain.Facture;
import fr.easybilling.domain.LigneFacture;
import fr.easybilling.domain.Tiers;
import fr.easybilling.service.EntrepriseService;
import fr.easybilling.service.FactureService;
import fr.easybilling.web.rest.form.FactureForm;
import fr.easybilling.web.rest.form.LigneFactureForm;
import fr.easybilling.web.rest.mapper.FactureMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FactureResourceTest {

    private MockMvc restFactureMockMvc;

    @Mock
    private FactureService factureService;
    @Mock
    private EntrepriseService entrepriseService;
    @Mock
    private FactureMapper factureMapper;

    @BeforeEach
    public void setUp() {
        FactureResource factureResource = new FactureResource(factureService, entrepriseService, factureMapper);
        restFactureMockMvc = MockMvcBuilders.standaloneSetup(factureResource).build();
    }

    @Test
    @DisplayName("Should return 200 correctly when bill exists")
    void getFactureWhen200() throws Exception {
        Facture facture = getFactureDataSet();

        when(factureService.findOne(anyLong())).thenReturn(Optional.of(facture));

        restFactureMockMvc.perform(get("/api/factures/" + 1)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 404 correctly when bill does not exists")
    void getFactureWhen404() throws Exception {
        when(factureService.findOne(anyLong())).thenReturn(Optional.empty());

        restFactureMockMvc.perform(get("/api/factures/" + 1)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 204 if done successfully")
    void updateFacture() throws Exception {
        FactureForm form = getFactureForm();

        restFactureMockMvc.perform(put("/api/factures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(form)))
            .andExpect(status().isNoContent());
    }

    private FactureForm getFactureForm() {
        FactureForm factureForm = new FactureForm();
        factureForm.setId(1);
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

    private Facture getFactureDataSet() {
        Facture facture = new Facture();

        Tiers tiers = new Tiers();
        tiers.setRaisonSociale("Stark Industry");
        tiers.setAdr1("1 Wall Street Avenue");
        tiers.setAdr2("2");
        tiers.setAdr3("3");
        tiers.setVille("New York");
        tiers.setCodePostal("04390");
        tiers.setEmail("tony.stark@stark-industry.com");

        facture.setDestinataire(tiers);
        facture.setTva(BigDecimal.valueOf(0.20));
        facture.setEcheanceDate(LocalDate.of(2020, 2, 1));

        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setIntitule("Vente alternateur");
        ligneFacture.setQuantite(1000);
        ligneFacture.setPrixHt(BigDecimal.valueOf(2700));
        facture.addLignes(ligneFacture);

        return facture;
    }

}
