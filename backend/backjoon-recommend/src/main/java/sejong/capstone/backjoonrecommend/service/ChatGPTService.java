package sejong.capstone.backjoonrecommend.service;

import static org.springframework.http.HttpMethod.POST;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.domain.Analysis;
import sejong.capstone.backjoonrecommend.domain.Code;

@Service
public class ChatGPTService {

    @Value("${openai-admin-key}")
    private String openai_admin_key;
    public String getAnalysis(String code, Analysis analysis, Code bestCode) {
        String gptRequestURI = "https://api.openai.com/v1/chat/completions";
        String extraQuestion = "";
        if (analysis == Analysis.BIG_O_AND_SPACE_COMPLEX_AND_WHAT_ALGO) {
            extraQuestion =
                    " Just the Big O notation, Space Complexity notation, what algorithm it is without explanation.  example -> O(N^3),O(N),Greedy. "
                            + "Never give an answer longer than 8 characters for each 3 item and and separate them with commas like example";
        }

        if (analysis == Analysis.COMPARE_BIG_O) {
            if (bestCode == null) {
                return "기능 준비 중 입니다.";
            }
            extraQuestion = " Simply compare and analyze the time complexity of preceding code and following code in less than 30 characters. Please explain it in terms of items rather than lines in Korean. ";
        }
        if (analysis == Analysis.COMPARE_SPACE_COMPLEX) {
            if (bestCode == null) {
                return "기능 준비 중 입니다.";
            }
            extraQuestion = " Simply compare and analyze the space complexity of preceding code and following code in 30 characters or less and explain it in terms of items rather than lines in Korean. ";
        }

        if (analysis == Analysis.COMPARE_BIG_O || analysis == Analysis.COMPARE_SPACE_COMPLEX) {
            extraQuestion += bestCode.getCode();
            if (code.equals(bestCode.getCode())) {
                return "같은 코드 입니다. 당신의 코드가 지금까지 최선의 코드입니다.";
            }
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + openai_admin_key);
        header.setContentType(MediaType.APPLICATION_JSON);

        GPTRequest gptRequest = new GPTRequest();
        Message message = new Message();
        message.setContent(code + extraQuestion);
        System.out.println(code + extraQuestion);
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

    public String getAnalysisWrong(String problemContents, String code) {
        String gptRequestURI = "https://api.openai.com/v1/chat/completions";
        String extraQuestion = "\n\n I solved this problem like " + code + " , Please give me some hints to help me solve it in korean.";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer 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");
        header.setContentType(MediaType.APPLICATION_JSON);

        GPTRequest gptRequest = new GPTRequest();
        Message message = new Message();
        message.setContent(problemContents + extraQuestion);
        System.out.println(problemContents + extraQuestion);
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
