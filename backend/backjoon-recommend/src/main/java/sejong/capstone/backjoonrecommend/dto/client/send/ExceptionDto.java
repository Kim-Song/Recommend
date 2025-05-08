package sejong.capstone.backjoonrecommend.dto.client.send;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionDto {
    private Long statusCode;
    private String message;
}
