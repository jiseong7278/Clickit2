package com.project.clickit.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    DEV("CLICKIT_DEV", "개발자"),
    STAFF("CLICKIT_STAFF", "스태프"),
    STUDENT("CLICKIT_STUDENT", "학생");

    private final String name;
    private final String detail;
}
