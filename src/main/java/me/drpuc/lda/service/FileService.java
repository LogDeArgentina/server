package me.drpuc.lda.service;

import me.drpuc.lda.entity.User;
import me.drpuc.lda.entity.VerificationFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileService {
    VerificationFile get(String uuid);
    List<VerificationFile> getAll();
    InputStream read(String uuid) throws IOException;
    List<String> saveAll(User user, MultipartFile[] files);
    boolean isOwner(User user, String uuid);
    void delete(String uuid);
}
