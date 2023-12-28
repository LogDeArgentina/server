package me.drpuc.lda.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;

@Entity
@Getter
@Setter
public class VerificationFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @ContentId
    private String contentId;

    @ContentLength
    private long contentLength;

    @MimeType
    private String mimeType;

    @ManyToOne
    private User owner;
}
