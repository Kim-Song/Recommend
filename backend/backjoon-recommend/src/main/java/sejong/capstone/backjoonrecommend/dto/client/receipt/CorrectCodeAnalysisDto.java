package sejong.capstone.backjoonrecommend.dto.client.receipt;

import lombok.Data;

@Data
public class CorrectCodeAnalysisDto {
    private Long number;
    private Long time;
    private Long memory;
    private String language;
    private String code;
}
