package sejong.capstone.backjoonrecommend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;
import sejong.capstone.backjoonrecommend.exception.IsNotRecommendProblemException;
import sejong.capstone.backjoonrecommend.exception.IsNotSupportedUserException;
import sejong.capstone.backjoonrecommend.exception.IsUnknownServerError;

@Service
public class AiServiceImpl implements AiService{

    @Value("${ai-recommend-problem-address}")
    private String problemRecommendAddress;

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
    public AiDTO getProblems(String id, List<String> algorithms) {
        String URL = problemRecommendAddress + "?user_id=" + id;
        String algorithmsQuery = "";
        for (String algorithm : algorithms) {
            algorithmsQuery += algorithm;
            algorithmsQuery += ",";
        }

        if (algorithms.size() != 0) {
            algorithmsQuery = algorithmsQuery.substring(0, algorithmsQuery.length() - 1);
            URL += "&wanted_algorithm_list=" + algorithmsQuery;
        }

        System.out.println(URL);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(URL, String.class);
        String body = forEntity.getBody();
        if (body.contains("Not Exist Problem")) {
            throw new IsNotRecommendProblemException("추천 문제가 존재하지 않습니다.");
        }
        if (body.contains("존재하지 않는 유저입니다. 유저님의 정보가 모델에 반영되면 문제를 추천해드리겠습니다.")) {
            throw new IsNotSupportedUserException("아직 지원하지 않는 유저입니다.");
        }

        try {
            AiDTO aiDTO = objectMapper.readValue(body, AiDTO.class);
            return aiDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IsUnknownServerError("해결이 되지 않은 서버 에러가 발생하였습니다.");
        // 여기서 오류가 날 수 있는 상황으로
        // 1. 유저가 없는 경우가 있을 수 있음
        // 2. 추천 문제가 3개 미만인 경우가 있을 수 있음
    }

    @Override
    public AiDTO getVideos(String id) { // 이것도 구현해야되는군 ㅠㅠ
        String URL = problemRecommendAddress + "?user_id=" + id;

        ResponseEntity<String> forEntity = restTemplate.getForEntity(URL, String.class);
        String body = forEntity.getBody();
        if (body.contains("존재하지 않는 유저입니다. 유저님의 정보가 모델에 반영되면 문제를 추천해드리겠습니다.")) {
            throw new IsNotSupportedUserException("아직 지원하지 않는 유저입니다.");
        }

        try {
            AiDTO aiDTO = objectMapper.readValue(body, AiDTO.class);
            return aiDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IsUnknownServerError("해결이 되지 않은 서버 에러가 발생하였습니다.");
    }
}