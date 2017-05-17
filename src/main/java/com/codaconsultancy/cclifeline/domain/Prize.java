package com.codaconsultancy.cclifeline.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRIZES")
public class Prize {

    @Id
    @GeneratedValue
    private Long id;

    private String prize;

    private Member winner;

    private boolean prizeCollected;

    private Date prizeCollectedDate;

    @ManyToOne
    @JoinColumn(name = "LOTTERY_DRAW_ID")
    private LotteryDraw lotteryDraw;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public Member getWinner() {
        return winner;
    }

    public void setWinner(Member winner) {
        this.winner = winner;
    }

    public boolean isPrizeCollected() {
        return prizeCollected;
    }

    public void setPrizeCollected(boolean prizeCollected) {
        this.prizeCollected = prizeCollected;
    }

    public Date getPrizeCollectedDate() {
        return prizeCollectedDate;
    }

    public void setPrizeCollectedDate(Date prizeCollectedDate) {
        this.prizeCollectedDate = prizeCollectedDate;
    }

    public LotteryDraw getLotteryDraw() {
        return lotteryDraw;
    }

    public void setLotteryDraw(LotteryDraw lotteryDraw) {
        this.lotteryDraw = lotteryDraw;
    }
}
