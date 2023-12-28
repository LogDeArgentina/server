package me.drpuc.lda.repository;

import me.drpuc.lda.entity.VerificationFile;
import org.springframework.content.commons.store.ContentStore;
import org.springframework.stereotype.Service;

@Service
public interface FileContentStore extends ContentStore<VerificationFile, String> {

}