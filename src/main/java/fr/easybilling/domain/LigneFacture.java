package fr.easybilling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A LigneFacture.
 */
@Entity
@Table(name = "ligne_facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LigneFacture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "intitule")
    private String intitule;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "prix_ht", precision = 21, scale = 2)
    private BigDecimal prixHt;

    @ManyToOne
    @JsonIgnoreProperties(value = "lignes", allowSetters = true)
    private Facture facture;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public LigneFacture intitule(String intitule) {
        this.intitule = intitule;
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public LigneFacture quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixHt() {
        return prixHt;
    }

    public LigneFacture prixHt(BigDecimal prixHt) {
        this.prixHt = prixHt;
        return this;
    }

    public void setPrixHt(BigDecimal prixHt) {
        this.prixHt = prixHt;
    }

    public Facture getFacture() {
        return facture;
    }

    public LigneFacture facture(Facture facture) {
        this.facture = facture;
        return this;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneFacture)) {
            return false;
        }
        return id != null && id.equals(((LigneFacture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneFacture{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", quantite=" + getQuantite() +
            ", prixHt=" + getPrixHt() +
            "}";
    }
}
