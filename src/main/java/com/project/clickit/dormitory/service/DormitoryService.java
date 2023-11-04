package com.project.clickit.dormitory.service;

import com.project.clickit.dormitory.domain.DormitoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DormitoryService {
    final DormitoryRepository dormitoryRepository;

}
