package fr.easybilling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "echeance_date")
    private LocalDate echeanceDate;

    @Column(name = "tva", precision = 21, scale = 2)
    private BigDecimal tva;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "facture", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<LigneFacture> lignes = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "factures", allowSetters = true)
    private Tiers destinataire;

    @ManyToOne
    @JsonIgnoreProperties(value = "factures", allowSetters = true)
    private Entreprise entreprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Facture creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getEcheanceDate() {
        return echeanceDate;
    }

    public Facture echeanceDate(LocalDate echeanceDate) {
        this.echeanceDate = echeanceDate;
        return this;
    }

    public void setEcheanceDate(LocalDate echeanceDate) {
        this.echeanceDate = echeanceDate;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public Facture tva(BigDecimal tva) {
        this.tva = tva;
        return this;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public String getStatus() {
        return status;
    }

    public Facture status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<LigneFacture> getLignes() {
        return lignes;
    }

    public Facture lignes(Set<LigneFacture> ligneFactures) {
        this.lignes = ligneFactures;
        return this;
    }

    public Facture addLignes(LigneFacture ligneFacture) {
        this.lignes.add(ligneFacture);
        ligneFacture.setFacture(this);
        return this;
    }

    public Facture removeLignes(LigneFacture ligneFacture) {
        this.lignes.remove(ligneFacture);
        ligneFacture.setFacture(null);
        return this;
    }

    public void setLignes(Set<LigneFacture> ligneFactures) {
        this.lignes = ligneFactures;
    }

    public Tiers getDestinataire() {
        return destinataire;
    }

    public Facture destinataire(Tiers tiers) {
        this.destinataire = tiers;
        return this;
    }

    public void setDestinataire(Tiers tiers) {
        this.destinataire = tiers;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public Facture entreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
        return this;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public BigDecimal getPrixTotalHT() {
        return this.getLignes().stream().map(lF ->
                        lF.getPrixHt().multiply(BigDecimal.valueOf(lF.getQuantite())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getPrixTotalTTC() {
        return getPrixTotalHT().add(getPrixTotalHT().multiply(this.tva));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return id != null && id.equals(((Facture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", echeanceDate='" + getEcheanceDate() + "'" +
            ", tva=" + getTva() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
