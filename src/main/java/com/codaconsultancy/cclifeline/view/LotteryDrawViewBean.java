package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Prize;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LotteryDrawViewBean {

    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date drawDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date lotteryDate;

    private String name;

    @NotNull
    private String drawMaster;

    private Integer numberOfPrizes;

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

    public Integer getNumberOfPrizes() {
        return numberOfPrizes;
    }

    public void setNumberOfPrizes(Integer numberOfPrizes) {
        this.numberOfPrizes = numberOfPrizes;
    }

    public String showPrizesSummary() {
        StringBuilder prizeSummary = new StringBuilder();
        List<Prize> prizes = this.prizes;
        for (Prize prize : prizes) {
            Member winner = prize.getWinner();
            prizeSummary.append(prize.getPrize()).append(": ").append(winner.getForename()).append(" ").append(winner.getSurname());
            if (null != winner.getAddresses().get(0).getTown() && !winner.getAddresses().get(0).getTown().isEmpty()) {
                prizeSummary.append(", ").append(winner.getAddresses().get(0).getTown());
            }
            prizeSummary.append(" (").append(winner.getMembershipNumber()).append("), ");
        }
        prizeSummary.deleteCharAt(prizeSummary.length() - 1);
        prizeSummary.deleteCharAt(prizeSummary.length() - 1);
        String summaryString = prizeSummary.toString();
        return summaryString;
    }

    public LotteryDraw toEntity() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(this, LotteryDraw.class);
    }
}

