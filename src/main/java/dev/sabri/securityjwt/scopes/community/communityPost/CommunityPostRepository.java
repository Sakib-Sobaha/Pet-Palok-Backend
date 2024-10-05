package dev.sabri.securityjwt.scopes.community.communityPost;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommunityPostRepository extends MongoRepository<CommunityPost, String> {
    public List<CommunityPost> findByCommunityId(String communityId);
}
