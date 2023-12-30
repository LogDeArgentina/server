package me.drpuc.lda.service.impl;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
public class ResourceContainer {
    private final InputStreamResource inputStreamResource;
    private final String mimeType;

    public ResourceContainer(InputStreamResource inputStreamResource, String mimeType) {
        this.inputStreamResource = inputStreamResource;
        this.mimeType = mimeType;
    }
}
