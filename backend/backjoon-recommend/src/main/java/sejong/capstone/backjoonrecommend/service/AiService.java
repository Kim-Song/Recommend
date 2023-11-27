package sejong.capstone.backjoonrecommend.service;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.dto.ai.AiDTO;

@Service
public class AiService {
    static final String ipAddress = "172.16.1.68";
    static final String URL_FORMAT = "http://" + ipAddress + ":8000/get_list/";

    private RestTemplate restTemplate = new RestTemplate();

    public AiDTO getAnalysis(String id) {
        String URL = URL_FORMAT + id;

        ResponseEntity<AiDTO> forEntity = restTemplate.getForEntity(URL, AiDTO.class);
        AiDTO body = forEntity.getBody();


        return body;
    }
}