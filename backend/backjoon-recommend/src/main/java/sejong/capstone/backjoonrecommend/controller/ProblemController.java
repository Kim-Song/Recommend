package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.dto.RecommendDto;

@RestController
public class ProblemController {

    @GetMapping("/recommend")
    List<RecommendDto> getRecommendDto() {
        ArrayList<RecommendDto> recommendDtos = new ArrayList<>();
        recommendDtos.add(new RecommendDto(1000L, "구구단 문제"));
        recommendDtos.add(new RecommendDto(1001L, "다익스트라 일까요"));
        recommendDtos.add(new RecommendDto(1002L, "문제문제문제"));
        recommendDtos.add(new RecommendDto(1003L, "재밌는 문제"));
        recommendDtos.add(new RecommendDto(1004L, "어려운 문제"));
        recommendDtos.add(new RecommendDto(1005L, "신기한 문제"));
        return recommendDtos;
    }
}
