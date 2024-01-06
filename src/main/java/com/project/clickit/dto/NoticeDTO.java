package com.project.clickit.dto;

import com.project.clickit.entity.NoticeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeDTO {
    private Integer num;
    private String title;
    private String content;
    private LocalDateTime date;
    private String img;
    private String memberId;

    @Builder
    public NoticeDTO(Integer num, String title, String content, LocalDateTime date, String img, String memberId) {
        this.num = num;
        this.title = title;
        this.content = content;
        this.date = date;
        this.img = img;
        this.memberId = memberId;
    }

    public NoticeEntity toEntity() {
        return NoticeEntity.builder()
                .num(this.num)
                .title(this.title)
                .content(this.content)
                .date(this.date)
                .img(this.img)
                .build();
    }
}
