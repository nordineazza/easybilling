package fr.easybilling.web.rest;

public enum FactureStatusEnum {
    EN_COURS("En cours"),
    EMISE("Émise"),
    REGLE("Réglée"),
    RETARD_DE_PAIEMENT("Retard de paiement");

    private String status;

    FactureStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
