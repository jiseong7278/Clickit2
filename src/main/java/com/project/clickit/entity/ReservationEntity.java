package com.project.clickit.entity;

import com.project.clickit.dto.ReservationDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_num")
    private Integer num;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reservation_seat", insertable = false, updatable = false)
    private SeatEntity seatEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reservation_member", insertable = false, updatable = false)
    private MemberEntity memberEntity;

    @Column(name = "reservation_timestamp")
    private LocalDateTime timestamp;

    @Column(name = "reservation_status")
    private String status;

    /**
     * <b>ReservationDTO로 변환</b>
     * @return ReservationDTO
     */
    public ReservationDTO toDTO() {
        return ReservationDTO.builder()
                .num(this.num)
                .seatId(this.seatEntity.getId())
                .memberId(this.memberEntity.getId())
                .timestamp(this.timestamp)
                .status(this.status)
                .build();
    }
}
