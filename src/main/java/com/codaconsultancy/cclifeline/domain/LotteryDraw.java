package com.codaconsultancy.cclifeline.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "LOTTERY_DRAWS")
public class LotteryDraw {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "DRAW_DATE")
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date drawDate;

    @Column(name = "DRAW_MASTER")
    @NotNull
    private String drawMaster;

    @OneToMany(mappedBy = "lotteryDraw")
    private List<Prize> prizes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Date drawDate) {
        this.drawDate = drawDate;
    }

    public String getDrawMaster() {
        return drawMaster;
    }

    public void setDrawMaster(String drawMaster) {
        this.drawMaster = drawMaster;
    }

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }
}
