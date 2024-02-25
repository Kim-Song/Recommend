package sejong.capstone.backjoonrecommend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;

@Service
public class AiService {

    @Value("${ai-recommend-problem-address}")
    private String problemRecommendAddress;

    private RestTemplate restTemplate = new RestTemplate();
    public AiDTO getProblems(String id) {
        String URL = problemRecommendAddress + id;

        ResponseEntity<AiDTO> forEntity = restTemplate.getForEntity(URL, AiDTO.class);
        AiDTO body = forEntity.getBody();

        return body;
    }
}