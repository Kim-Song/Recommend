package sejong.capstone.backjoonrecommend.dto;

import lombok.Data;

@Data
public class CorrectCodeAnalysisResult {
    private String bigO;
    private String spaceComplexDegree;
    private String whatAlgo;
    private String compareOtherBigOCode;
    private String compareOtherBigO;
    private String compareOtherSpaceComplexDegreeCode;
    private String compareOtherSpaceComplexDegree;
}
