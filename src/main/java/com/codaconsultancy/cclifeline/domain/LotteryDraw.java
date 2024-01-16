package com.codaconsultancy.cclifeline.domain;

import com.codaconsultancy.cclifeline.view.LotteryDrawViewBean;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lottery_draws")
public class LotteryDraw {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "DRAW_DATE")
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date drawDate;

    @Column(name = "LOTTERY_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date lotteryDate;

    @Column(name = "NAME")
    private String name;

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

    public Date getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Date lotteryDate) {
        this.lotteryDate = lotteryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Transient
    public String showPrizesSummary() {
        StringBuilder summary = new StringBuilder();
        List<Prize> prizes = this.prizes;
        for (Prize prize : prizes) {
            Member winner = prize.getWinner();
            summary.append(prize.getPrize()).append(": ")
                    .append(winner.getForename()).append(" ").append(winner.getSurname());
            if (null != winner.getAddresses().get(0).getTown() && !winner.getAddresses().get(0).getTown().isEmpty()) {
                summary.append(", ").append(winner.getAddresses().get(0).getTown());
            }
            summary.append(" (").append(winner.getMembershipNumber()).append("), ");
        }
        summary.deleteCharAt(summary.length() - 1);
        summary.deleteCharAt(summary.length() - 1);
        return summary.toString();
    }

    public LotteryDrawViewBean toViewBean() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(this, LotteryDrawViewBean.class);
    }
}
