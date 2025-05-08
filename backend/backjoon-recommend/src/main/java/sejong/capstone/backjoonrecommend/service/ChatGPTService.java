package sejong.capstone.backjoonrecommend.service;

import static org.springframework.http.HttpMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.domain.CodeAnalysis;
import sejong.capstone.backjoonrecommend.domain.entity.Code;

@Service
public class ChatGPTService {

    @Value("${openai-admin-key}")
    private String openai_admin_key;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> getAnalysis(String code, CodeAnalysis codeAnalysis, Code bestCode) {
        String gptRequestURI = "https://api.openai.com/v1/chat/completions";
        String extraQuestion = "";
        if (codeAnalysis == CodeAnalysis.BIG_O) {
            extraQuestion = "Please tell me the Big O notation for this code. Example -> O(N^3)\n"
                    + "As an example, just tell me without further explanation. Never allow the total length of your answer to exceed 10 characters.";
        }

        if (codeAnalysis == CodeAnalysis.SPACE_COMPLEX) {
            extraQuestion = "Please tell me the space complex for this code. Example -> O(N^3)\n"
                    + "As an example, just tell me without further explanation. Never allow the total length of your answer to exceed 10 characters.";
        }

        if (codeAnalysis == CodeAnalysis.WHAT_ALGO) {
            extraQuestion =
                    "Please tell me what algo it is for this code. Example -> Greedy\n"
                            + "As an example, just tell me without further explanation. "
                            + "Never allow the total length of your answer to exceed 10 characters.";
        }

        if (codeAnalysis == CodeAnalysis.COMPARE_BIG_O) {
            if (bestCode == null) {
                return CompletableFuture.completedFuture("기능 준비 중 입니다.");
            }
            extraQuestion = " Simply compare and analyze the time complexity of preceding code and following code in less than 30 characters. Please explain it in terms of items rather than lines in Korean. ";
        }
        if (codeAnalysis == CodeAnalysis.COMPARE_SPACE_COMPLEX) {
            if (bestCode == null) {
                return CompletableFuture.completedFuture("기능 준비 중 입니다.");
            }
            extraQuestion = " Simply compare and analyze the space complexity of preceding code and following code in 30 characters or less and explain it in terms of items rather than lines in Korean. ";
        }

        if (codeAnalysis == CodeAnalysis.COMPARE_BIG_O || codeAnalysis == CodeAnalysis.COMPARE_SPACE_COMPLEX) {
            extraQuestion += bestCode.getCode();
            if (code.equals(bestCode.getCode())) {
                return CompletableFuture.completedFuture("같은 코드 입니다. 당신의 코드가 지금까지 최선의 코드입니다.");
            }
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + openai_admin_key);
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

        return CompletableFuture.completedFuture(analysisResult);
    }
    public String getAnalysisWrong(String problemContents, String code) {
        String gptRequestURI = "https://api.openai.com/v1/chat/completions";
        String extraQuestion = "\n\n I solved this problem like " + code + " , Please give me some hints to help me solve it in korean.";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + openai_admin_key);
        header.setContentType(MediaType.APPLICATION_JSON);

        GPTRequest gptRequest = new GPTRequest();
        Message message = new Message();
        message.setContent(problemContents + extraQuestion);
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
