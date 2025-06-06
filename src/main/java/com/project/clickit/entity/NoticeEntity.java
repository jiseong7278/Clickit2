package com.project.clickit.entity;

import com.project.clickit.dto.NoticeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Table(name = "notice")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_num")
    private Integer num;

    @Column(name = "notice_title")
    private String title;

    @Column(name = "notice_content")
    private String content;

    @Column(name = "notice_date")
    private LocalDateTime date;

    @Column(name = "notice_img")
    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_member")
    private MemberEntity memberEntity;

    /**
     * <b>NoticeDTO로 변환</b>
     * @return NoticeDTO
     */
    public NoticeDTO toDTO() {
        return NoticeDTO.builder()
                .num(this.num)
                .title(this.title)
                .content(this.content)
                .date(this.date)
                .img(this.img)
                .memberId(this.memberEntity.getId())
                .build();
    }
}
