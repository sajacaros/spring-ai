package kr.study.ai.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ChatConfig {
    @Value(value = "${spring.ai.openai.api-key}")
    private String apiKey = "api-key";


    @Bean
    public OpenAiApi openAiApi() {
        return new OpenAiApi(apiKey);
    }

    @Bean
    public OpenAiChatOptions chatOptions() {
        return OpenAiChatOptions.builder()
                .withModel("gpt-4o")
                .withTemperature(0.0)
                .withMaxTokens(1000)
                .build();
    }

    @Bean
    public ChatModel chatModel(OpenAiApi openAiApi, OpenAiChatOptions chatOptions) {
        return new OpenAiChatModel(openAiApi, chatOptions);
    }
}
