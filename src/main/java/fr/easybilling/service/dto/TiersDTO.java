package fr.easybilling.service.dto;


public class TiersDTO {

    private String raisonSociale;
    private String adr1;
    private String adr2;
    private String adr3;
    private String codePostale;
    private String ville;

    public TiersDTO(String raisonSociale, String adr1, String adr2, String adr3, String codePostale, String ville) {
        this.raisonSociale = raisonSociale;
        this.adr1 = adr1;
        this.adr2 = adr2;
        this.adr3 = adr3;
        this.codePostale = codePostale;
        this.ville = ville;
    }

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

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
