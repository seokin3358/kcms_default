package com.kone.kitms.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

/**
 * KITMS CRLF 로그 변환기 클래스
 * 
 * 이 클래스는 KITMS 시스템의 로그 보안을 위한 CRLF(캐리지 리턴/라인 피드) 변환기입니다:
 * - 공격자가 CRLF 문자를 포함한 입력으로 로그 항목을 위조하는 것을 방지
 * - CRLF 문자를 빨간색 _ 문자로 대체
 * - 로그 인젝션 공격 방어
 * - 안전한 로거 목록 관리
 * - ANSI 색상 코드 지원
 * 
 * @see <a href="https://owasp.org/www-community/attacks/Log_Injection">Log Forging Description</a>
 * @see <a href="https://github.com/jhipster/generator-jhipster/issues/14949">JHipster issue</a>
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
public class CRLFLogConverter extends CompositeConverter<ILoggingEvent> {

    public static final Marker CRLF_SAFE_MARKER = MarkerFactory.getMarker("CRLF_SAFE");

    private static final String[] SAFE_LOGGERS = {
        "org.hibernate",
        "org.springframework.boot.autoconfigure",
        "org.springframework.boot.diagnostics",
    };
    private static final Map<String, AnsiElement> ELEMENTS;

    static {
        Map<String, AnsiElement> ansiElements = new HashMap<>();
        ansiElements.put("faint", AnsiStyle.FAINT);
        ansiElements.put("red", AnsiColor.RED);
        ansiElements.put("green", AnsiColor.GREEN);
        ansiElements.put("yellow", AnsiColor.YELLOW);
        ansiElements.put("blue", AnsiColor.BLUE);
        ansiElements.put("magenta", AnsiColor.MAGENTA);
        ansiElements.put("cyan", AnsiColor.CYAN);
        ELEMENTS = Collections.unmodifiableMap(ansiElements);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        AnsiElement element = ELEMENTS.get(getFirstOption());
        List<Marker> markers = event.getMarkerList();
        if ((markers != null && !markers.isEmpty() && markers.get(0).contains(CRLF_SAFE_MARKER)) || isLoggerSafe(event)) {
            return in;
        }
        String replacement = element == null ? "_" : toAnsiString("_", element);
        return in.replaceAll("[\n\r\t]", replacement);
    }

    protected boolean isLoggerSafe(ILoggingEvent event) {
        for (String safeLogger : SAFE_LOGGERS) {
            if (event.getLoggerName().startsWith(safeLogger)) {
                return true;
            }
        }
        return false;
    }

    protected String toAnsiString(String in, AnsiElement element) {
        return AnsiOutput.toString(element, in);
    }
}
