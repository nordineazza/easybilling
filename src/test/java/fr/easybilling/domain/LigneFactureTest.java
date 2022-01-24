package fr.easybilling.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.easybilling.web.rest.TestUtil;

public class LigneFactureTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneFacture.class);
        LigneFacture ligneFacture1 = new LigneFacture();
        ligneFacture1.setId(1L);
        LigneFacture ligneFacture2 = new LigneFacture();
        ligneFacture2.setId(ligneFacture1.getId());
        assertThat(ligneFacture1).isEqualTo(ligneFacture2);
        ligneFacture2.setId(2L);
        assertThat(ligneFacture1).isNotEqualTo(ligneFacture2);
        ligneFacture1.setId(null);
        assertThat(ligneFacture1).isNotEqualTo(ligneFacture2);
    }
}
