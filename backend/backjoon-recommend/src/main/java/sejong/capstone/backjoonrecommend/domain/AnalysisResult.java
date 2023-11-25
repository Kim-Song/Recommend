package sejong.capstone.backjoonrecommend.domain;

import lombok.Data;

@Data
public class AnalysisResult {
    private String bigO;
    private String spaceComplexDegree;
    private String whatAlgo;
    private String compareOtherBigOCode;
    private String compareOtherBigO;
    private String compareOtherSpaceComplexDegreeCode;
    private String compareOtherSpaceComplexDegree;
}
