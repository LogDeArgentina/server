package me.drpuc.lda.service.impl;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.entity.VerificationFile;
import me.drpuc.lda.repository.FileRepository;
import me.drpuc.lda.service.FileService;
import me.drpuc.lda.repository.FileContentStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileContentStore fileContentStore;
    private final Set<String> allowedMime = Set.of(
            "image/png",
            "image/jpeg",
            "application/pdf",
            "application/msword"
    );

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

    public List<VerificationFile> getAllOwnedBy(String userUuid) {
        return fileRepository.findAllByOwnerUuid(userUuid);
    }

    public ResourceContainer read(String uuid) {
        VerificationFile file = get(uuid);
        InputStreamResource inputStreamResource = new InputStreamResource(
                fileContentStore.getContent(file)
        );

        return new ResourceContainer(
                inputStreamResource,
                file.getMimeType()
        );
    }

    public List<String> saveAll(User user, MultipartFile[] files) {
        List<String> uuids = new LinkedList<>();

        if (files.length > 3) {
            throw new IllegalArgumentException("too many files");
        }

        for (MultipartFile multipartFile : files) {
            if (!allowedMime.contains(multipartFile.getContentType())) {
                throw new IllegalArgumentException("invalid file type");
            }
        }

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
