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
import sejong.capstone.backjoonrecommend.domain.Analysis;

@Service
public class ChatGPTService {
    public String getAnalysis(String code, Analysis analysis, String otherCode) {
        String gptRequestURI = "https://api.openai.com/v1/chat/completions";
        String extraQuestion = "";
        if (analysis == Analysis.BIG_O) {
            extraQuestion = " Just the Big O notation without explanation, example -> O(N^3). Never give an answer longer than 8 characters ";
        }
        if (analysis == Analysis.SPACE_COMPLEX) {
            extraQuestion = " Just the Space Complexity notation without explanation, example -> O(N^3). Never give an answer longer than 8 characters ";
        }
        if (analysis == Analysis.WHAT_ALGO) {
            extraQuestion = " Analyze what algorithm it is. But just search for this person and say nothing. example -> Greedy. Never answer longer than 3 words. ";
        }
        if (analysis == Analysis.COMPARE_BIG_O) {
            extraQuestion = "이 코드랑 저 코드 시간 복잡도 간단히 비교 분석해줘 ";
        }
        if (analysis == Analysis.COMPARE_SPACE_COMPLEX) {
            extraQuestion = "이 코드랑 저 코드 공간 복잡도 간단히 비교 분석해줘 ";
        }

        if (analysis == Analysis.COMPARE_BIG_O || analysis == Analysis.COMPARE_SPACE_COMPLEX) {
            extraQuestion += otherCode;
            if (otherCode == null) {
                return "기능 준비 중 입니다.";
            }
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer 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");
        header.setContentType(MediaType.APPLICATION_JSON);

        GPTRequest gptRequest = new GPTRequest();
        Message message = new Message();
        message.setContent(code + extraQuestion);
        message.setRole("user");
        gptRequest.setModel("gpt-3.5-turbo");
        gptRequest.setStream(false);
        gptRequest.setMessages(new ArrayList<>());
        gptRequest.getMessages().add(message);

        HttpEntity<GPTRequest> httpEntity = new HttpEntity<>(gptRequest, header);

        ResponseEntity<GPTResponse> exchange = restTemplate.exchange(gptRequestURI, POST, httpEntity,
                GPTResponse.class);

        String analysisResult = exchange.getBody().getChoices().get(0).getMessage().getContent();

        return analysisResult;
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
