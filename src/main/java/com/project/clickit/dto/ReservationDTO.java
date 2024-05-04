package com.project.clickit.dto;

import com.project.clickit.entity.ReservationEntity;
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
    private String status;

    /**
     * <b>ReservationEntity로 변환</b>
     * @return ReservationEntity
     */
    public ReservationEntity toEntity() {
        return ReservationEntity.builder()
                .num(this.num)
                .seatEntity(SeatDTO.builder().id(seatId).build().toEntity())
                .memberEntity(MemberDTO.builder().id(memberId).build().toEntity())
                .timestamp(this.timestamp)
                .status(this.status)
                .build();
    }
}
