package com.project.clickit.dto;

import com.project.clickit.entity.NoticeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private Integer num;
    private String title;
    private String content;
    private LocalDateTime date;
    private String img;
    private String memberId;

    /**
     * <b>NoticeEntity로 변환</b>
     * @return NoticeEntity
     */
    public NoticeEntity toEntity() {
        return NoticeEntity.builder()
                .num(this.num)
                .title(this.title)
                .content(this.content)
                .date(this.date)
                .img(this.img)
                .memberEntity(MemberDTO.builder().id(this.memberId).build().toEntity())
                .build();
    }
}
