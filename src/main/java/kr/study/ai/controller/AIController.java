package kr.study.ai.controller;

import kr.study.ai.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Slf4j
public class AIController {
    private static final String TEMPLATE = """
                아래는 table의 column 목록입니다.
                사용자의 질의에 맞는 SQL 문에서 WHERE 절 이하 부분만 작성해 주세요.
                쿼리문은 반드시 table column information을 참조하여 작성해 주세요.
                날짜 조건이 포함된 경우, SQL 함수를 사용하지 말고 현재 날짜를 기준으로 한 실제 값을 작성해 주세요.
                            
                [additional information]
                `날짜 데이터`의 형태 : YYYY-MM-dd HH:mm:ss 형식의 text 타입
                `현재 시간` : {currentDateTime}
                `boolean 데이터`의 형태 : 'Y', 'N' 으로 구성
                `최종수정자`의 형태 : '이름(id)' 형태로 구성
                
                [table column information]
                테이블명
                테이블한글명
                DB명/스키마명
                외래키컬럼구성
                필수여부
                관계유형
                최종수정자
                최종수정일시
                            
                사용자 질의: {query}
                            
                ==> WHERE 절 이하 부분만 출력해 주세요. SQL 문 또는 줄바꿈 문자는 포함하지 마십시오.
                """;
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/grid-query")
    String completion(
            @RequestParam(value = "query") String query
    ) {
        log.info("prompt length: {}", TEMPLATE.length());
        PromptTemplate promptTemplate = new PromptTemplate(TEMPLATE);
        Prompt prompt = promptTemplate.create(
                Map.of(
                        "query", query,
                        "currentDateTime", DateUtil.formatLocalDateTime(LocalDateTime.now())
                )
        );
        log.info("prompt: {}", prompt);
        ChatResponse call = chatModel.call(prompt);
        return call.getResult().getOutput().getContent();
    }
}
