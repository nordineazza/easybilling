package fr.easybilling.service.dto;

import java.math.BigDecimal;

public class LigneFactureDTO {

    private String intitule;
    private Integer quantite;
    private BigDecimal prixHt;

    public LigneFactureDTO(String intitule, Integer quantite, BigDecimal prixHt) {
        this.intitule = intitule;
        this.quantite = quantite;
        this.prixHt = prixHt;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixHt() {
        return prixHt;
    }

    public void setPrixHt(BigDecimal prixHt) {
        this.prixHt = prixHt;
    }
}
