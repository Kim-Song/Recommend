package sejong.capstone.backjoonrecommend.service;

import static org.springframework.http.HttpMethod.POST;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.domain.BigO;

@Service
public class ChatGPTService {
    public BigO getBigO(String code) {
        String gptRequestURI = "https://api.openai.com/v1/chat/completions";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer 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");
        header.setContentType(MediaType.APPLICATION_JSON);

        GPTRequest gptRequest = new GPTRequest();
        Message message = new Message();
        message.setContent("100+200 = ?");
        message.setRole("user");
        gptRequest.setModel("gpt-3.5-turbo");
        gptRequest.setStream(false);
        gptRequest.setMessages(new ArrayList<>());
        gptRequest.getMessages().add(message);

        HttpEntity<GPTRequest> httpEntity = new HttpEntity<>(gptRequest, header);

        ResponseEntity<GPTResponse> exchange = restTemplate.exchange(gptRequestURI, POST, httpEntity,
                GPTResponse.class);

        String analysisResult = exchange.getBody().getChoices().get(0).getMessage().getContent();
        BigO bigO = new BigO();
        bigO.setContent(analysisResult);

        return bigO;
    }

    @Data
    static class GPTRequest {
        private String model;
        private Boolean stream;
        private List<Message> messages;
    }

    @Data
    static class Message {
        private String role;
        private String content;
    }

    @Data
    static class GPTResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<Choice> choices;
        private Usage usage;
    }

    @Data
    static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    @Data
    static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }
}
