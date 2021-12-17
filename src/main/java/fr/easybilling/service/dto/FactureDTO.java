package fr.easybilling.service.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class FactureDTO {

    private long id;
    private LocalDate creationDate;
    private LocalDate echeanceDate;
    private BigDecimal montantHT;

    private BigDecimal montantTTC;
    private String status;

    private String raisonSocialeDest;
    private String coordonneeDest;

    public FactureDTO(long id, LocalDate creationDate, LocalDate echeanceDate, BigDecimal tva, BigDecimal montantHT, String status,
                      String raisonSocialeEnt, String adr1Ent, String adr2Ent, String adr3Ent, String codePostaleEnt, String villeEnt,
                      String raisonSocialeDest, String adr1Dest, String adr2Dest, String adr3Dest, String codePostaleDest, String villeDest) {
        this.creationDate = creationDate;
        this.echeanceDate = echeanceDate;
        this.montantHT = montantHT;
        this.montantTTC = montantHT.add(montantHT.multiply(tva)).setScale(2, RoundingMode.CEILING); // Ajout de la TVA pour avoir le montant TTC
        this.status = status;
        this.raisonSocialeDest = raisonSocialeDest;
        this.coordonneeDest = adr1Dest + " - " + codePostaleDest + " " + villeDest;
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getEcheanceDate() {
        return echeanceDate;
    }

    public void setEcheanceDate(LocalDate echeanceDate) {
        this.echeanceDate = echeanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getRaisonSocialeDest() {
        return raisonSocialeDest;
    }

    public void setRaisonSocialeDest(String raisonSocialeDest) {
        this.raisonSocialeDest = raisonSocialeDest;
    }

    public String getCoordonneeDest() {
        return coordonneeDest;
    }

    public void setCoordonneeDest(String coordonneeDest) {
        this.coordonneeDest = coordonneeDest;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
