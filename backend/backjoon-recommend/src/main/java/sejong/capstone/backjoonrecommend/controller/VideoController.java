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
import sejong.capstone.backjoonrecommend.domain.entity.Video;
import sejong.capstone.backjoonrecommend.dto.VideoLink;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;
import sejong.capstone.backjoonrecommend.dto.client.send.ExceptionDto;
import sejong.capstone.backjoonrecommend.dto.client.send.VideoLinkDto;
import sejong.capstone.backjoonrecommend.exception.IsNotSupportedUserException;
import sejong.capstone.backjoonrecommend.repository.VideoRepository;
import sejong.capstone.backjoonrecommend.service.AiService;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoRepository videoRepository;
    private final AiService aiService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IsNotSupportedUserException.class)
    public ExceptionDto IsNotSupportedUserException(IsNotSupportedUserException e) {
        return new ExceptionDto(400L, e.getMessage());
    }

    @GetMapping("/video")
    private VideoLinkDto getVideo(@RequestParam String id) {
        // id를 가지고 취약 알고리즘을 알아낸다.
        AiDTO analysis = aiService.getVideos(id);
        List<String> userWeakAlgorithms = analysis.getUser_weak_algorithm();

        ArrayList<VideoLink> videoLinks = new ArrayList<>();

        for (String algo : userWeakAlgorithms) {
            List<Video> videos = videoRepository.findByAlgorithm(algo);

            for (Video video : videos) {
                VideoLink videoLink = new VideoLink();
                String algorithm = video.getAlgorithm();
                String url = video.getUrl();
                videoLink.setWeakAlgorithm(algorithm);
                videoLink.setLink(url);
                videoLinks.add(videoLink);
            }
        }

        VideoLinkDto videoLinkDto = new VideoLinkDto();
        videoLinkDto.setData(videoLinks);
        return videoLinkDto;
    }
}
