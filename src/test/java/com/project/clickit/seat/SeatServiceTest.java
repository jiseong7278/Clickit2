package com.project.clickit.seat;

import com.project.clickit.repository.SeatRepository;
import com.project.clickit.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@DisplayName("SeatService 테스트")
@ExtendWith({MockitoExtension.class})
public class SeatServiceTest {
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

}
