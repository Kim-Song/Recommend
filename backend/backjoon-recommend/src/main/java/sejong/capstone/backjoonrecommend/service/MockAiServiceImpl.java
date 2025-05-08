package sejong.capstone.backjoonrecommend.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;
import sejong.capstone.backjoonrecommend.exception.IsNotRecommendProblemException;
import sejong.capstone.backjoonrecommend.exception.IsNotSupportedUserException;

public class MockAiServiceImpl implements AiService{
    public AiDTO getProblems(String id, List<String> algorithms) {
        if (id.equals("no_id")) {
            throw new IsNotSupportedUserException("아직 지원하지 않는 유저입니다.");
        }
        if (id.equals("no_recommend")) {
            throw new IsNotRecommendProblemException("추천 문제가 존재하지 않습니다.");
        }

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

    public AiDTO getVideos(String id) {
        if (id.equals("no_id")) {
            throw new IsNotSupportedUserException("아직 지원하지 않는 유저입니다.");
        }
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
