package com.chikacow.kohimana.model;


import com.chikacow.kohimana.util.enums.SalaryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tbl_salary")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Salary extends AbstractEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "salary_month")
    private Date salaryMonth;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SalaryStatus status = SalaryStatus.PENDING;

    @Column(name = "paid_date")
    private Date paidDate;

    @Column(name = "note")
    private String note;

}
