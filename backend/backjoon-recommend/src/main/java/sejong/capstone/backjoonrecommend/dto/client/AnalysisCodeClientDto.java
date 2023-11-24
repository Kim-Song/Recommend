package sejong.capstone.backjoonrecommend.dto.client;

import lombok.Data;

@Data
public class AnalysisCodeClientDto {
    private Long number;
    private Long time;
    private Long memory;
    private String language;
    private String code;
}
