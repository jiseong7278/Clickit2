package com.project.clickit.controller;

import com.project.clickit.dto.NoticeDTO;
import com.project.clickit.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${requestMapping.notice}", produces="application/json;charset=UTF-8")
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("${notice.create}")
    public ResponseEntity<Object> create(@RequestBody NoticeDTO noticeDTO){
        noticeService.createNotice(noticeDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("${notice.getAll}")
    public ResponseEntity<Page<NoticeDTO>> getAll(@PageableDefault(direction = Sort.Direction.DESC,
            sort = "num", size=10, page=0) Pageable pageable){
        return ResponseEntity.ok().body(noticeService.getAll(pageable));
    }

    @GetMapping("${notice.findByNoticeNum}")
    public ResponseEntity<NoticeDTO> findByNoticeNum(@RequestParam("num") Integer num){
        return ResponseEntity.ok().body(noticeService.findByNoticeNum(num));
    }

    @GetMapping("${notice.findByWriterId}")
    public ResponseEntity<Page<NoticeDTO>> findByWriterId(@RequestParam("writerId") String writerId,
                                                          @PageableDefault(direction = Sort.Direction.DESC,
                                                        sort = "num", size=10, page=0) Pageable pageable){
        return ResponseEntity.ok().body(noticeService.findNoticeByWriterId(writerId, pageable));
    }

    @PutMapping("${notice.update}")
    public ResponseEntity<Object> update(@RequestBody NoticeDTO noticeDTO){
        noticeService.updateNotice(noticeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("${notice.delete}")
    public ResponseEntity<Object> delete(@RequestParam("num") Integer num){
        noticeService.deleteNotice(num);
        return ResponseEntity.ok().build();
    }
}
