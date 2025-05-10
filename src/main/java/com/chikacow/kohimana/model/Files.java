package com.chikacow.kohimana.model;

import com.chikacow.kohimana.util.enums.FileType;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "tbl_file")
@Slf4j
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Files extends AbstractEntity<Long> {
    @Column(name = "local_url", nullable = false)
    private String localUrl;

    @Column(name = "cloud_url", columnDefinition = "TEXT")
    private String cloudUrl;

    @Column(name = "type")
//    @Enumerated(EnumType.STRING)
    private String fileType;
    

}
