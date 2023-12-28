package me.drpuc.lda.repository;

import me.drpuc.lda.entity.VerificationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<VerificationFile, String> {

}