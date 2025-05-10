package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Files, Long> {
    public Files findByLocalUrl(String localUrl);

    void deleteByCloudUrl(String cloutUrl);
}
