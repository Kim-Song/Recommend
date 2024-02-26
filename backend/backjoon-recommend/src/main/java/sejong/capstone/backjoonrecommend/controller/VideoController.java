package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.entity.Video;
import sejong.capstone.backjoonrecommend.dto.VideoLink;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;
import sejong.capstone.backjoonrecommend.dto.client.send.VideoLinkDto;
import sejong.capstone.backjoonrecommend.repository.VideoRepository;
import sejong.capstone.backjoonrecommend.service.AiService;
import sejong.capstone.backjoonrecommend.service.AiServiceImpl;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoRepository videoRepository;
    private final AiService AiServiceImpl;

    @GetMapping("/video")
    private VideoLinkDto getVideo(@RequestParam String id) {
        // id를 가지고 취약 알고리즘을 알아낸다.
        AiDTO analysis = AiServiceImpl.getProblems(id);
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
