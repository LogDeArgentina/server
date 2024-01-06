package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.dto.response.FileResponse;
import me.drpuc.lda.dto.response.FilesResponse;
import me.drpuc.lda.dto.response.UuidResponse;
import me.drpuc.lda.dto.response.UuidsResponse;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.service.FileService;
import me.drpuc.lda.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    @PostMapping("")
    public UuidsResponse uploadFiles(Authentication auth,
                                     @RequestParam("files") MultipartFile[] files) {
        User user = userService.getUserViaAuthentication(auth);
        List<UuidResponse> uuidResponses = new LinkedList<>();
        fileService.saveAll(user, files).forEach(fileUuid ->
                uuidResponses.add(new UuidResponse(fileUuid)));
        return new UuidsResponse(uuidResponses);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<InputStreamResource> getFileContent(Authentication auth,
                                                              @PathVariable String uuid) {
        User user = userService.getUserViaAuthentication(auth);
        if (!fileService.isOwner(user, uuid) && !user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        var inputStreamContainer = fileService.read(uuid);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", inputStreamContainer.getMimeType());

        return new ResponseEntity<>(
                inputStreamContainer.getInputStreamResource(),
                headers,
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public FilesResponse getAllFiles() {
        List<FileResponse> fileResponses = new LinkedList<>();
        fileService.getAll().forEach(file ->
                fileResponses.add(new FileResponse(file)));
        return new FilesResponse(fileResponses);
    }

    @GetMapping("/all/{userUuid}")
    public FilesResponse getAllFilesFrom(Authentication auth,
                                         @PathVariable String userUuid) {
        User user = userService.getUserViaAuthentication(auth);
        if (!user.getUuid().equals(userUuid) && !user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        List<FileResponse> fileResponses = new LinkedList<>();
        fileService.getAllOwnedBy(userUuid).forEach(file ->
                fileResponses.add(new FileResponse(file)));

        return new FilesResponse(fileResponses);
    }

    @DeleteMapping("/all/{userUuid}")
    public void deleteAllFilesFrom(Authentication auth,
                                   @PathVariable String userUuid) {
        User user = userService.getUserViaAuthentication(auth);
        if (!user.getUuid().equals(userUuid) && !user.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }
        fileService.getAllOwnedBy(userUuid).forEach(file ->
                fileService.delete(file.getUuid()));
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
