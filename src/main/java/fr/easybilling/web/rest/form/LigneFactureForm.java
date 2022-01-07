package fr.easybilling.web.rest.form;

import java.math.BigDecimal;

public class LigneFactureForm {
    private int quantite;
    private BigDecimal prixHt;
    private String intitule;

    public LigneFactureForm() {
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixHt() {
        return prixHt;
    }

    public void setPrixHt(BigDecimal prixHt) {
        this.prixHt = prixHt;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    @Override
    public String toString() {
        return "LigneFactureForm{" +
            "quantite=" + quantite +
            ", montantHT=" + prixHt +
            ", detail='" + intitule + '\'' +
            '}';
    }
}
