package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.domain.Language;
import com.sapwd.wdquote.repository.LanguageRepository;
import com.sapwd.wdquote.service.LanguageService;
import com.sapwd.wdquote.service.dto.LanguageDTO;
import com.sapwd.wdquote.service.mapper.LanguageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Language}.
 */
@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {

    private final Logger log = LoggerFactory.getLogger(LanguageServiceImpl.class);

    private final LanguageRepository languageRepository;

    private final LanguageMapper languageMapper;

    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    @Override
    public LanguageDTO save(LanguageDTO languageDTO) {
        log.debug("Request to save Language : {}", languageDTO);
        Language language = languageMapper.toEntity(languageDTO);
        language = languageRepository.save(language);
        return languageMapper.toDto(language);
    }

    @Override
    public LanguageDTO update(LanguageDTO languageDTO) {
        log.debug("Request to update Language : {}", languageDTO);
        Language language = languageMapper.toEntity(languageDTO);
        language = languageRepository.save(language);
        return languageMapper.toDto(language);
    }

    @Override
    public Optional<LanguageDTO> partialUpdate(LanguageDTO languageDTO) {
        log.debug("Request to partially update Language : {}", languageDTO);

        return languageRepository
            .findById(languageDTO.getId())
            .map(existingLanguage -> {
                languageMapper.partialUpdate(existingLanguage, languageDTO);

                return existingLanguage;
            })
            .map(languageRepository::save)
            .map(languageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LanguageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Languages");
        return languageRepository.findAllByDeletedIsFalse(pageable).map(languageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LanguageDTO> findOne(Long id) {
        log.debug("Request to get Language : {}", id);
        return languageRepository.findById(id).map(languageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Language : {}", id);
        Optional<Language> languageOptional = languageRepository.findById(id);
        if (languageOptional.isPresent()) {
            Language language = languageOptional.orElseThrow();
            language.setDeleted(true);
            languageRepository.save(language);
        }
    }
}
