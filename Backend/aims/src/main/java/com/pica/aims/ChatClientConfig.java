package com.pica.aims;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ChatClientConfig {

    @Bean
    @Scope("prototype")
    public ChatClient.Builder openAiChatClientBuilder(ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                                      @Qualifier("openAiChatModel") ChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        return chatClientBuilderConfigurer.configure(builder);
    }

    @Bean
    @Scope("prototype")
    ChatClient.Builder ollamaChatClientBuilder(ChatClientBuilderConfigurer chatClientBuilderConfigurer,
                                               @Qualifier("ollamaChatModel") ChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        return chatClientBuilderConfigurer.configure(builder);
    }
}
