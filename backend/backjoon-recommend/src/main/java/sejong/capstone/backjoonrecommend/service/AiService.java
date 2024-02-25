package sejong.capstone.backjoonrecommend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiProblemDTO;

@Service
public class AiService {

    @Value("${ai-recommend-problem-address}")
    private String problem_reco_addr;
    @Value("${ai-problem-information-address}")
    private String problem_info_addr;

    private RestTemplate restTemplate = new RestTemplate();

    public AiDTO getProblemList(String id) {
        String URL = problem_reco_addr + id;

        ResponseEntity<AiDTO> forEntity = restTemplate.getForEntity(URL, AiDTO.class);
        AiDTO body = forEntity.getBody();


        return body;
    }

    public AiProblemDTO getProblemInfo(Long id) {
        String URL = problem_info_addr + id;

        ResponseEntity<AiProblemDTO> forEntity = restTemplate.getForEntity(URL, AiProblemDTO.class);
        AiProblemDTO body = forEntity.getBody();

        return body;
    }
}