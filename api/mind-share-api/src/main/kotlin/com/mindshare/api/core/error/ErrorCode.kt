package com.mindshare.api.core.error

enum class ErrorCode(val description: String) {
    DEFAULT("HTTP 기본 에러입니다."),

    // A01 (계정)
    A01001("중복된 닉네임"),
    A01002("이미 가입된 아이디"),

    // A05(공통)
    A05001("요청한 데이터가 요구사항을 충족하지 않습니다.")
    ;

}