package com.chikacow.kohimana.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor

public class StatisticalResponse {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class AgeAndGender {
        private double womenAverageAge;
        private double menAverageAge;

        private long womenNumUser;
        private long menNumUser;

    }
}
