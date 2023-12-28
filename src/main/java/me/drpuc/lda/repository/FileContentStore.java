package me.drpuc.lda.repository;

import me.drpuc.lda.entity.ValidationFile;
import org.springframework.content.commons.store.ContentStore;
import org.springframework.stereotype.Service;

@Service
public interface FileContentStore extends ContentStore<ValidationFile, String> {

}