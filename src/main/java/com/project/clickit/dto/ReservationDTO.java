package com.project.clickit.dto;

import com.project.clickit.entity.MemberEntity;
import com.project.clickit.entity.ReservationEntity;
import com.project.clickit.entity.SeatEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReservationDTO {
    private Integer num;
    private String seatId;
    private String memberId;
    private LocalDateTime timestamp;

    public ReservationDTO(Integer num, String seatId, String memberId) {
        this.num = num;
        this.seatId = seatId;
        this.memberId = memberId;
    }

    @Builder
    public ReservationDTO(Integer num, String seatId, String memberId, LocalDateTime timestamp) {
        this.num = num;
        this.seatId = seatId;
        this.memberId = memberId;
        this.timestamp = timestamp;
    }

    public ReservationEntity toEntity() {
        return ReservationEntity.builder()
                .num(this.num)
                .seatEntity(SeatEntity.builder()
                        .id(this.seatId)
                        .build())
                .memberEntity(MemberEntity.builder()
                        .id(this.memberId)
                        .build())
                .timestamp(this.timestamp)
                .build();
    }
}
