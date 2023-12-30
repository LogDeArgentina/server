package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.dto.request.radio.CreateQsoDto;
import me.drpuc.lda.dto.response.QsoResponse;
import me.drpuc.lda.dto.response.QsosResponse;
import me.drpuc.lda.dto.response.UuidResponse;
import me.drpuc.lda.entity.Qso;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.service.UserService;
import org.springframework.security.core.Authentication;
import me.drpuc.lda.service.QsoService;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qso")
public class QsoController {
    private final UserService userService;
    private final QsoService qsoService;

    @PostMapping("/create")
    public UuidResponse createQso(Authentication auth,
                                  @RequestBody CreateQsoDto qsoDto) {
        User user = userService.getUserViaAuthentication(auth);
        String uuid = qsoService.create(user, qsoDto);
        return new UuidResponse(uuid);
    }

    @GetMapping("/{uuid}")
    public QsoResponse getQso(Authentication auth,
                              @PathVariable String uuid) {
        User user = userService.getUserViaAuthentication(auth);
        Qso qso = qsoService.read(user, uuid);
        return new QsoResponse(qso);
    }

    @GetMapping("/all/{stationCallsign}")
    public QsosResponse getAllQsos(Authentication auth,
                                   @PathVariable String stationCallsign) {
        var user = userService.getUserViaAuthentication(auth);
        List<QsoResponse> qsoResponses = new LinkedList<>();
        qsoService.readAllFrom(user, stationCallsign).forEach(qso ->
                qsoResponses.add(new QsoResponse(qso)));
        return new QsosResponse(qsoResponses);
    }

    @DeleteMapping("/{uuid}")
    public void deleteQso(Authentication auth, @PathVariable String uuid) {
        var user = userService.getUserViaAuthentication(auth);
        qsoService.delete(user, uuid);
    }
}
