package me.drpuc.lda.service;

import me.drpuc.lda.entity.User;
import me.drpuc.lda.entity.VerificationFile;
import me.drpuc.lda.service.impl.ResourceContainer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    VerificationFile get(String uuid);
    List<VerificationFile> getAll();
    List<VerificationFile> getAllOwnedBy(String userUuid);
    ResourceContainer read(String uuid);
    List<String> saveAll(User user, MultipartFile[] files);
    boolean isOwner(User user, String uuid);
    void delete(String uuid);
}
