package com.example.project.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ── 400 Bad Request ───────────────────────────────────────────────────────
    INVALID_INPUT(400,        "C001", "잘못된 입력값입니다."),
    INVALID_FILE_PATH(400,    "C002", "허용되지 않는 파일 경로입니다."),
    INVALID_FILE_TYPE(400,    "C003", "허용되지 않는 파일 형식입니다. (jpg, png, gif, webp만 가능)"),
    PASSWORD_MISMATCH(400,    "U003", "비밀번호가 일치하지 않습니다."),
    INVALID_INVITE_TOKEN(400, "I001", "유효하지 않은 초대 토큰입니다."),
    INVALID_ROUTE_ID(400,     "R003", "올바르지 않은 루트 ID 형식입니다."),
    INVALID_TOKEN(400,        "T001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(400,        "T002", "만료된 토큰입니다."),

    // ── 401 Unauthorized ──────────────────────────────────────────────────────
    UNAUTHORIZED(401, "A000", "인증이 필요합니다."),

    // ── 403 Forbidden ─────────────────────────────────────────────────────────
    ACCESS_DENIED(403,        "A001", "접근 권한이 없습니다."),
    NOT_ROUTE_OWNER(403,      "A002", "루트 소유자만 수행할 수 있는 작업입니다."),
    NOT_ACCESS_WRITER(403,    "A003", "해당 후기의 작성자가 아닙니다."),
    NOT_ROUTE_MEMBER(403,     "A004", "해당 루트의 멤버가 아닙니다."),
    NOT_ADMIN(403,            "A005", "관리자만 접근 가능합니다."),

    // ── 404 Not Found ─────────────────────────────────────────────────────────
    USER_NOT_FOUND(404,         "U001", "사용자를 찾을 수 없습니다."),
    ROUTE_NOT_FOUND(404,        "R001", "루트를 찾을 수 없습니다."),
    BLOCK_NOT_FOUND(404,        "R002", "블록을 찾을 수 없습니다."),
    ACCESS_POST_NOT_FOUND(404,  "P001", "후기를 찾을 수 없습니다."),
    PLACE_NOT_FOUND(404,        "L001", "장소를 찾을 수 없습니다."),
    MEMO_NOT_FOUND(404,         "L002", "메모를 찾을 수 없습니다."),

    // ── 409 Conflict ──────────────────────────────────────────────────────────
    DUPLICATE_USERNAME(409,    "U002", "이미 사용 중인 아이디입니다."),
    ROUTE_LOCK_TIMEOUT(409,         "R004", "다른 사용자가 편집 중입니다. 잠시 후 다시 시도하세요."),
    ROUTE_MEMBER_LIMIT_EXCEEDED(409,"R005", "루트 멤버는 최대 30명까지 참가할 수 있습니다."),

    // ── 500 Internal Server Error ─────────────────────────────────────────────
    IMAGE_UPLOAD_FAILED(500,        "S001", "이미지 업로드에 실패했습니다."),
    OPENAI_RESPONSE_INVALID(500,    "AI01", "AI 응답을 처리하는 데 실패했습니다."),
    INTERNAL_SERVER_ERROR(500,      "S999", "서버 내부 오류가 발생했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
