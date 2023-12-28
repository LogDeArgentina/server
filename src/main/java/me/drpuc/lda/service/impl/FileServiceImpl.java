package me.drpuc.lda.service.impl;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.entity.ValidationFile;
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

    public ValidationFile get(String uuid) {
        return fileRepository.findById(uuid).orElseThrow(
                () -> new IllegalArgumentException("file not found via: " + uuid)
        );
    }

    public boolean isOwner(User user, String uuid) {
        ValidationFile file = get(uuid);
        return file.getOwner().equals(user);
    }

    public List<ValidationFile> getAll() {
        return fileRepository.findAll();
    }

    public InputStream read(String uuid) throws IOException {
        ValidationFile file = get(uuid);
        return fileContentStore.getResource(file).getInputStream();
    }

    public List<String> saveAll(User user, MultipartFile[] files) {
        List<String> uuids = new LinkedList<>();

        for (MultipartFile multipartFile : files) {
            ValidationFile file = new ValidationFile();
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
