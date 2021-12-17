package fr.easybilling.service.impl;

import fr.easybilling.domain.Facture;
import fr.easybilling.service.dto.FactureDTO;
import fr.easybilling.repository.FactureRepository;
import fr.easybilling.service.FactureService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Facture}.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;

    public FactureServiceImpl(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Override
    public Facture save(Facture facture) {
        log.debug("Request to save Facture : {}", facture);
        return factureRepository.save(facture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Facture> findAll() {
        log.debug("Request to get all Factures");
        return factureRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
    }

    @Override
    public List<FactureDTO> findFacturesByEntreprise() {
        return factureRepository.findFacturesByEntreprise();
    }

    private ByteArrayOutputStream generateFactureWithJasper(Facture facture) {

        InputStream jasperInputStream = this.getClass().getResourceAsStream("/jasper/facture.jrxml");

        try {
            JasperDesign jasperDesign = JRXmlLoader.load(jasperInputStream);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), new JRBeanCollectionDataSource(Collections.singletonList(facture)));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return outputStream;
        } catch (JRException exception) {
            log.error("error while generating report", exception);
        }

        return new ByteArrayOutputStream(0);
    }

    @Override
    public ByteArrayOutputStream generateFactureWithJasper(long idFacture) {
        Optional<Facture> optFacture = findOne(idFacture);

        return optFacture
            .map(this::generateFactureWithJasper)
            .orElseThrow(() -> new NullPointerException("la facture n'existe pas"));
    }

}
