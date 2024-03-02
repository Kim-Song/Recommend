package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.entity.Problem;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;
import sejong.capstone.backjoonrecommend.dto.client.send.ExceptionDto;
import sejong.capstone.backjoonrecommend.dto.client.send.ProblemDto;
import sejong.capstone.backjoonrecommend.exception.IsNotRecommendProblemException;
import sejong.capstone.backjoonrecommend.exception.IsNotSupportedUserException;
import sejong.capstone.backjoonrecommend.repository.ProblemRepository;
import sejong.capstone.backjoonrecommend.service.AiService;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemRepository problemRepository;
    private final AiService aiService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IsNotRecommendProblemException.class)
    public ExceptionDto IsNotRecommendProblemException(IsNotRecommendProblemException e) {
        return new ExceptionDto(400L, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IsNotSupportedUserException.class)
    public ExceptionDto IsNotSupportedUserException(IsNotSupportedUserException e) {
        return new ExceptionDto(400L, e.getMessage());
    }

    @GetMapping("/problem")
    private ProblemDto getRecommendProblems(@RequestParam String id, @RequestParam(defaultValue = "") List<String> algorithms) {
        // id를 이용해서 problemNumbers를 가져온다. 즉 추천 문제들을 가져온다.
        AiDTO analysis = aiService.getProblems(id, algorithms);
        List<Long> recommendList = analysis.getRecommend_list();

        List<Problem> list = new ArrayList<>();
        for (Long number : recommendList) {
            Problem info = problemRepository.findByProblemNumber(number);
            list.add(info);
        }

        ProblemDto problemDto = new ProblemDto();
        problemDto.setData(list);
        return problemDto;
    }
}
