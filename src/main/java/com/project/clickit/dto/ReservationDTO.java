package com.project.clickit.dto;

import com.project.clickit.entity.MemberEntity;
import com.project.clickit.entity.ReservationEntity;
import com.project.clickit.entity.SeatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Integer num;
    private String seatId;
    private String memberId;
    private LocalDateTime timestamp;

    /**
     * <b>ReservationEntity로 변환</b>
     * @return ReservationEntity
     */
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
