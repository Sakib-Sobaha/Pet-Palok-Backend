package dev.sabri.securityjwt.scopes.community.communityPostComment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "_communityPostComments")
public class CommunityPostComment {
    @Id
    private String id;
    private String postId;
    private String parent; // parent null mane eta main comment, parent null na mane eita reply
    private String authorId;
    private String userType;
    private String text;
    private Date timestamp;
    private boolean anonymous;
}
