package sejong.capstone.backjoonrecommend.service;

import java.util.ArrayList;
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

        // ResponseEntity<AiDTO> forEntity = restTemplate.getForEntity(URL, AiDTO.class);
        // AiDTO body = forEntity.getBody();
        // 위의 주석 진짜 쓸때 풀어줘야함

        // 지금은 더미데이터임 삭제해 줘야함
        AiDTO body = new AiDTO();

        ArrayList arrayList = new ArrayList<>();
        arrayList.add(9206L);
        arrayList.add(27740L);
        arrayList.add(1166L);
        body.setRecommend_list(arrayList);

        ArrayList arrayList2 = new ArrayList<>();
        arrayList2.add("수학");
        arrayList2.add("자료 구조");
        arrayList2.add("정렬");
        arrayList2.add("그래프 이론");
        arrayList2.add("다이나믹 프로그래밍");
        body.setUser_weak_algorithm(arrayList2);
        // 지금은 더미데이터임 삭제해 줘야함
        return body;
    }
}
