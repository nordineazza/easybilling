package fr.easybilling.web.rest.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactureForm {
    private String raisonSociale;
    private String adr1;
    private String adr2;
    private String adr3;
    private String codePostal;
    private String ville;
    private String email;
    private BigDecimal tva;
    private LocalDate echeanceDate;

    private List<LigneFactureForm> lignesFacture;

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdr1() {
        return adr1;
    }

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public String getAdr2() {
        return adr2;
    }

    public void setAdr2(String adr2) {
        this.adr2 = adr2;
    }

    public String getAdr3() {
        return adr3;
    }

    public void setAdr3(String adr3) {
        this.adr3 = adr3;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public LocalDate getEcheanceDate() {
        return echeanceDate;
    }

    public void setEcheanceDate(LocalDate echeanceDate) {
        this.echeanceDate = echeanceDate;
    }

    public List<LigneFactureForm> getLignesFacture() {
        if (this.lignesFacture == null) {
            this.lignesFacture = new ArrayList<>();
        }
        return this.lignesFacture;
    }

    public void setLignesFacture(List<LigneFactureForm> lignesFacture) {
        this.lignesFacture = lignesFacture;
    }

    @Override
    public String toString() {
        return "FactureForm{" +
            " raisonSociale='" + raisonSociale + '\'' +
            ", adr1='" + adr1 + '\'' +
            ", adr2='" + adr2 + '\'' +
            ", adr3='" + adr3 + '\'' +
            ", codePostal='" + codePostal + '\'' +
            ", ville='" + ville + '\'' +
            ", email='" + email + '\'' +
            ", tva=" + tva +
            ", echeanceDate=" + echeanceDate +
            ", lignesFacture=" + lignesFacture +
            '}';
    }
}
