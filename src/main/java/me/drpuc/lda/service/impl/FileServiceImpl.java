package me.drpuc.lda.service.impl;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.entity.VerificationFile;
import me.drpuc.lda.repository.FileRepository;
import me.drpuc.lda.service.FileService;
import me.drpuc.lda.repository.FileContentStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileContentStore fileContentStore;

    public VerificationFile get(String uuid) {
        return fileRepository.findById(uuid).orElseThrow(
                () -> new IllegalArgumentException("file not found via: " + uuid)
        );
    }

    public boolean isOwner(User user, String uuid) {
        VerificationFile file = get(uuid);
        return file.getOwner().equals(user);
    }

    public void delete(String uuid) {
        VerificationFile file = get(uuid);
        fileRepository.delete(file);
        fileContentStore.unsetContent(file);
    }

    public List<VerificationFile> getAll() {
        return fileRepository.findAll();
    }

    public InputStream read(String uuid) throws IOException {
        VerificationFile file = get(uuid);
        return fileContentStore.getResource(file).getInputStream();
    }

    public List<String> saveAll(User user, MultipartFile[] files) {
        List<String> uuids = new LinkedList<>();

        for (MultipartFile multipartFile : files) {
            VerificationFile file = new VerificationFile();
            file.setOwner(user);
            file = fileRepository.save(file);

            uuids.add(file.getUuid());
            file.setContentId(file.getUuid());
            file.setMimeType(multipartFile.getContentType());
            file.setContentLength(multipartFile.getSize());
            fileContentStore.setContent(file, multipartFile.getResource());
        }

        return uuids;
    }
}
