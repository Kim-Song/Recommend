package sejong.capstone.backjoonrecommend.controller;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.CodeAnalysis;
import sejong.capstone.backjoonrecommend.domain.entity.Problem;
import sejong.capstone.backjoonrecommend.dto.CorrectCodeAnalysisResult;
import sejong.capstone.backjoonrecommend.dto.WrongCodeAnalysis;
import sejong.capstone.backjoonrecommend.domain.entity.Code;
import sejong.capstone.backjoonrecommend.dto.client.send.AnalysisDto;
import sejong.capstone.backjoonrecommend.dto.client.receipt.CorrectCodeAnalysisDto;
import sejong.capstone.backjoonrecommend.dto.client.send.WrongCodeAnalysisDto;
import sejong.capstone.backjoonrecommend.repository.CodeRepository;
import sejong.capstone.backjoonrecommend.repository.ProblemRepository;
import sejong.capstone.backjoonrecommend.service.ChatGPTService;

@RestController
@RequiredArgsConstructor
public class CodeAnalysisController {
    private final ChatGPTService chatGPTService;
    private final CodeRepository codeRepository;
    private final ProblemRepository problemRepository;

    @PostMapping("/analysis")
    private AnalysisDto getCorrectCodeAnalysis(@RequestBody CorrectCodeAnalysisDto codeDto) {
        Long number = codeDto.getNumber();
        Long time = codeDto.getTime();
        Long memory = codeDto.getMemory();
        String language = codeDto.getLanguage();
        String code = codeDto.getCode();

        Code inputCode = new Code();
        inputCode.setNumber(number);
        inputCode.setTime(time);
        inputCode.setMemory(memory);
        inputCode.setLanguage(language);
        inputCode.setCode(code);

        codeRepository.save(inputCode);
        Code bestCode1 = codeRepository.getBestCodeByBigO(number, language);
        Code bestCode2 = codeRepository.getBestCodeBySpaceComplexity(number, language);

        CompletableFuture<String> asyncBigO = chatGPTService.getAnalysis(code, CodeAnalysis.BIG_O, null);
        CompletableFuture<String> asyncSpaceComplex = chatGPTService.getAnalysis(code, CodeAnalysis.SPACE_COMPLEX, null);
        CompletableFuture<String> asyncWhatAlgo = chatGPTService.getAnalysis(code, CodeAnalysis.WHAT_ALGO, null);
        CompletableFuture<String> asyncCompareBigO = chatGPTService.getAnalysis(code, CodeAnalysis.COMPARE_BIG_O, bestCode1);
        CompletableFuture<String> asyncCompareSpaceComplex = chatGPTService.getAnalysis(code, CodeAnalysis.COMPARE_SPACE_COMPLEX, bestCode2);

        String bigO = "";
        String spaceComplex = "";
        String whatAlgo = "";
        String compareBigO = "";
        String compareSpaceComplex = "";

        try {
            bigO = asyncBigO.get();
            spaceComplex = asyncSpaceComplex.get();
            whatAlgo = asyncWhatAlgo.get();
            compareBigO = asyncCompareBigO.get();
            compareSpaceComplex = asyncCompareSpaceComplex.get();
        } catch (Exception e) {
            bigO = "내일";
            spaceComplex = "이용이";
            whatAlgo = "가능합니다";
            compareBigO = "내일 이용이 가능합니다.";
            compareSpaceComplex = "내일 이용이 가능합니다";
        }

        AnalysisDto analysisDto = new AnalysisDto();
        CorrectCodeAnalysisResult correctCodeAnalysisResult = new CorrectCodeAnalysisResult();

        correctCodeAnalysisResult.setBigO(bigO);
        correctCodeAnalysisResult.setSpaceComplexDegree(spaceComplex);
        correctCodeAnalysisResult.setWhatAlgo(whatAlgo);

        correctCodeAnalysisResult.setCompareOtherBigOCode(bestCode1.getCode());
        correctCodeAnalysisResult.setCompareOtherBigO(compareBigO);
        correctCodeAnalysisResult.setCompareOtherSpaceComplexDegreeCode(bestCode2.getCode());
        correctCodeAnalysisResult.setCompareOtherSpaceComplexDegree(compareSpaceComplex);

        analysisDto.setData(correctCodeAnalysisResult);
        return analysisDto;
    }

    @PostMapping("/analysis2")
    private WrongCodeAnalysisDto getWrongCodeAnalysis(@RequestBody sejong.capstone.backjoonrecommend.dto.client.receipt.WrongCodeAnalysisDto clientDto) {
        Long number = clientDto.getNumber();
        String userCode = clientDto.getCode();

        // 문제를 가져온다.
        Problem problem = problemRepository.findByProblemNumber(number);
        String contents = problem.getDescription();
        contents += " 입력 : " + problem.getInput();
        contents += " 출력 : " + problem.getOutput();
        // 문제를 가져온다.

        String analysisWrong = chatGPTService.getAnalysisWrong(contents, userCode);

        WrongCodeAnalysisDto wrongCodeAnalysisDto = new WrongCodeAnalysisDto();
        WrongCodeAnalysis wrongCodeAnalysis = new WrongCodeAnalysis();
        wrongCodeAnalysis.setContents(analysisWrong);
        wrongCodeAnalysisDto.setData(wrongCodeAnalysis);
        return wrongCodeAnalysisDto;
    }
}
