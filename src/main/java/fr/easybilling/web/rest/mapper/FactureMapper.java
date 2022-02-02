package fr.easybilling.web.rest.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.easybilling.domain.Facture;
import fr.easybilling.domain.LigneFacture;
import fr.easybilling.domain.Tiers;
import fr.easybilling.web.rest.form.FactureForm;
import fr.easybilling.web.rest.response.FactureUpdateResponse;
import fr.easybilling.web.rest.response.LigneFactureUpdateResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static fr.easybilling.web.rest.FactureStatusEnum.EN_COURS;

@Component
public class FactureMapper {

    private final ObjectMapper objectMapper;

    public FactureMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        // Settings
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public Facture mapFormToFactureForCreation(FactureForm form) {
        Facture facture = mapCommonElementForCreationAndUpdate(form);
        facture.setCreationDate(LocalDate.now());
        facture.setStatus(EN_COURS.name());
        facture.getDestinataire().setInscrit(false);
        facture.getDestinataire().setPays("France"); // TODO à retirer au futur pour l'intégrer comme feature
        return facture;
    }

    public Facture mapFormToFactureForUpdate(FactureForm form,
                                             Optional<Facture> optionalFactureDB) {
        Facture facture = mapCommonElementForCreationAndUpdate(form);
        if (optionalFactureDB.isPresent()) {
            Facture factureDB = optionalFactureDB.get();
            facture.setId(factureDB.getId());
            facture.getDestinataire().setId(factureDB.getDestinataire().getId());
            facture.getDestinataire().setPays(factureDB.getDestinataire().getPays());
            facture.getDestinataire().setInscrit(factureDB.getDestinataire().isInscrit());
            facture.setCreationDate(factureDB.getCreationDate());
            facture.setStatus(factureDB.getStatus());
            facture.setEntreprise(factureDB.getEntreprise());
        }
        return facture;
    }

    private Facture mapCommonElementForCreationAndUpdate(FactureForm form) {
        Facture facture = objectMapper.convertValue(form, Facture.class);
        Tiers tiers = objectMapper.convertValue(form, Tiers.class);
        facture.setDestinataire(tiers);

        form.getLignesFacture().forEach(ligne
            -> facture.addLignes(objectMapper.convertValue(ligne, LigneFacture.class))
        );

        return facture;
    }

    public FactureUpdateResponse mapFactureToResponse(Facture facture) {
        FactureUpdateResponse factureResponse = objectMapper.convertValue(facture.getDestinataire(), FactureUpdateResponse.class);

        factureResponse.setTva(facture.getTva());
        factureResponse.setEcheanceDate(facture.getEcheanceDate());

        facture.getLignes().forEach(ligne -> factureResponse.getLignesFacture().add(objectMapper.convertValue(ligne, LigneFactureUpdateResponse.class)));
        return factureResponse;
    }

}
