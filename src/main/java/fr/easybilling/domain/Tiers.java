package fr.easybilling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Tiers.
 */
@Entity
@Table(name = "tiers")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tiers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "raison_sociale")
    private String raisonSociale;

    @Column(name = "adr_1")
    private String adr1;

    @Column(name = "adr_2")
    private String adr2;

    @Column(name = "adr_3")
    private String adr3;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "ville")
    private String ville;

    @Column(name = "pays")
    private String pays;

    @Column(name = "email")
    private String email;

    @Column(name = "inscrit")
    private Boolean inscrit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public Tiers raisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
        return this;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdr1() {
        return adr1;
    }

    public Tiers adr1(String adr1) {
        this.adr1 = adr1;
        return this;
    }

    public void setAdr1(String adr1) {
        this.adr1 = adr1;
    }

    public String getAdr2() {
        return adr2;
    }

    public Tiers adr2(String adr2) {
        this.adr2 = adr2;
        return this;
    }

    public void setAdr2(String adr2) {
        this.adr2 = adr2;
    }

    public String getAdr3() {
        return adr3;
    }

    public Tiers adr3(String adr3) {
        this.adr3 = adr3;
        return this;
    }

    public void setAdr3(String adr3) {
        this.adr3 = adr3;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public Tiers codePostal(String codePostal) {
        this.codePostal = codePostal;
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public Tiers ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public Tiers pays(String pays) {
        this.pays = pays;
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getEmail() {
        return email;
    }

    public Tiers email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isInscrit() {
        return inscrit;
    }

    public Tiers inscrit(Boolean inscrit) {
        this.inscrit = inscrit;
        return this;
    }

    public void setInscrit(Boolean inscrit) {
        this.inscrit = inscrit;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tiers)) {
            return false;
        }
        return id != null && id.equals(((Tiers) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tiers{" +
            "id=" + getId() +
            ", raisonSociale='" + getRaisonSociale() + "'" +
            ", adr1='" + getAdr1() + "'" +
            ", adr2='" + getAdr2() + "'" +
            ", adr3='" + getAdr3() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", ville='" + getVille() + "'" +
            ", pays='" + getPays() + "'" +
            ", email='" + getEmail() + "'" +
            ", inscrit='" + isInscrit() + "'" +
            "}";
    }
}
