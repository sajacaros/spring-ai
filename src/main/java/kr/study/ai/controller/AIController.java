package kr.study.ai.controller;

import kr.study.ai.utils.DateUtil;
import kr.study.ai.vo.UserQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Slf4j
public class AIController {
    @Value("${grid.template}")
    private String gridTemplate;
    @Autowired
    private ChatModel chatModel;

    BeanOutputConverter<UserQuery> beanOutputConverter =
        new BeanOutputConverter<>(UserQuery.class);

    @GetMapping(value = "/grid-query", produces = "application/json")
    public UserQuery completion(
            @RequestParam(value = "query") String query
    ) {
        log.info("prompt length: {}", gridTemplate.length());
        PromptTemplate promptTemplate = new PromptTemplate(gridTemplate);
        Prompt prompt = promptTemplate.create(
                Map.of(
                        "query", query,
                        "currentDateTime", DateUtil.formatLocalDateTime(LocalDateTime.now()),
                        "format", beanOutputConverter.getFormat()
                )
        );
        log.info("prompt: {}", prompt);
        ChatResponse call = chatModel.call(prompt);
        return beanOutputConverter.convert(call.getResult().getOutput().getContent());
    }
}
