package sejong.capstone.backjoonrecommend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class User {
    private String handle;
    private String bio;
    private String badgeId;
    private String backgroundId;
    private String profileImageUrl;
    private Long solvedCount;
    private Long voteCount;
    private Long tier;
    private Long rating;
    private Long ratingByProblemsSum;
    private Long ratingByClass;
    private Long ratingBySolvedCount;
    private Long ratingByVoteCount;
    @JsonProperty("class")
    private Long oneClass;
    private String classDecoration;
    private Long rivalCount;
    private Long reverseRivalCount;
    private Long maxStreak;
    private Long coins;
    private Long stardusts;
    private LocalDateTime joinedAt;
    private LocalDateTime bannedUntil;
    private LocalDateTime proUntil;
    private Long rank;
    private Boolean isRival;
    private Boolean isReverseRival;
}
