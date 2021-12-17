package fr.easybilling.service.impl;

import fr.easybilling.service.TiersService;
import fr.easybilling.domain.Tiers;
import fr.easybilling.repository.TiersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Tiers}.
 */
@Service
@Transactional
public class TiersServiceImpl implements TiersService {

    private final Logger log = LoggerFactory.getLogger(TiersServiceImpl.class);

    private final TiersRepository tiersRepository;

    public TiersServiceImpl(TiersRepository tiersRepository) {
        this.tiersRepository = tiersRepository;
    }

    @Override
    public Tiers save(Tiers tiers) {
        log.debug("Request to save Tiers : {}", tiers);
        return tiersRepository.save(tiers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tiers> findAll() {
        log.debug("Request to get all Tiers");
        return tiersRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Tiers> findOne(Long id) {
        log.debug("Request to get Tiers : {}", id);
        return tiersRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tiers : {}", id);
        tiersRepository.deleteById(id);
    }
}
