package dev.sabri.securityjwt.scopes.community.communityPostComment;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommunityPostCommentRepository extends MongoRepository<CommunityPostComment, String> {
    public List<CommunityPostComment> findByPostId(String postId);
    public List<CommunityPostComment> findByParent(String parentId);
}
