package sejong.capstone.backjoonrecommend.service;

import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;

@Service
public class MockAiServiceImpl implements  AiService{
    public AiDTO getProblems(String id) {
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
        return body;

    }
}
