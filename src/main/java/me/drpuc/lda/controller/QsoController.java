package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.dto.radio.CreateQsoDto;
import me.drpuc.lda.service.UserService;
import org.springframework.security.core.Authentication;
import me.drpuc.lda.service.QsoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qso")
public class QsoController {
    private final UserService userService;
    private final QsoService qsoService;

    @PostMapping("/upload")
    public String uploadQso(Authentication auth,
                            @RequestBody CreateQsoDto qsoDto) {
        var user = userService.getUserByAuthentication(auth);
        return qsoService.create(user, qsoDto);
    }
}
