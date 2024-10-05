package dev.sabri.securityjwt.scopes.community.communityPost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "_communityPost")
public class CommunityPost {
    @Id
    private String id;
    private String communityId;
    private String title;
    private List<String> images = new ArrayList<>();
    private String text;
    private String author;
    private String userType;
    private List<String> reactList = new ArrayList<>();
    private Date timestamp;
    private List<String> topics;
    private boolean anonymous;
}
