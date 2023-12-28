package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.entity.VerificationFile;
import me.drpuc.lda.service.FileService;
import me.drpuc.lda.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("")
    public List<String> uploadFiles(Authentication auth,
                                    @RequestParam("files") MultipartFile[] files) {
        User user = userService.getUserViaAuthentication(auth);
        return fileService.saveAll(user, files);
    }

    @GetMapping("/{uuid}")
    public InputStreamResource getFile(Authentication auth, @PathVariable String uuid) throws IOException {
        User user = userService.getUserViaAuthentication(auth);
        if (!fileService.isOwner(user, uuid) && !user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        var content = fileService.read(uuid);
        return new InputStreamResource(content);
    }

    @GetMapping("/all")
    public List<VerificationFile> getAllFiles() {
        return fileService.getAll();
    }

    @GetMapping("/all/{userUuid}")
    public List<VerificationFile> getAllFilesFrom(Authentication auth,
                                                  @PathVariable String userUuid) {
        User user = userService.getUserViaAuthentication(auth);
        if (!user.getUuid().equals(userUuid) && !user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        return fileService.getAllOwnedBy(userUuid);
    }

    @DeleteMapping("/{uuid}")
    public void deleteFile(Authentication auth, @PathVariable String uuid) {
        User user = userService.getUserViaAuthentication(auth);
        if (!fileService.isOwner(user, uuid) && !user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        fileService.delete(uuid);
    }
}
