package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.Problem;
import sejong.capstone.backjoonrecommend.dto.client.ProblemClientDto;
import sejong.capstone.backjoonrecommend.repository.ProblemRecommendRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ProblemRecommendController {

    private final ProblemRecommendRepository problemRecommendRepository;

    public ProblemRecommendController(ProblemRecommendRepository problemRecommendRepository) {
        this.problemRecommendRepository = problemRecommendRepository;
    }

    @GetMapping("/problem")
    private ProblemClientDto getProblems(@RequestParam String id) {
        // id를 이용해서 problemNumbers를 가져온다. 즉 추천 문제들을 가져온다.
        List<Problem> list = new ArrayList<>();
        Problem info = problemRecommendRepository.findByProblemNumber(1010L);
        list.add(info);
        System.out.println(info);
        info = problemRecommendRepository.findByProblemNumber(1094L);
        list.add(info);
        System.out.println(info);
        info = problemRecommendRepository.findByProblemNumber(1181L);
        list.add(info);
        System.out.println(info);
        ProblemClientDto problemClientDto = new ProblemClientDto();
        problemClientDto.setData(list);
        return problemClientDto;
    }
}
