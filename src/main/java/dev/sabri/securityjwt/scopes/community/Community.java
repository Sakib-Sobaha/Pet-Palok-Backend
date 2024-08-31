package dev.sabri.securityjwt.scopes.community;

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
@Document(collection = "_communities")
public class Community {
    @Id
    private String id;
    private String name;
    private String description;
    private String image;
    private List<String> members = new ArrayList<>();
    private Date dateCreated;
    private List<String> admins = new ArrayList<>();
    private List<String> topics = new ArrayList<>(); // like hashtag or something where the community topics are shown


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(name).append("\n").append("Description: ").append(description).append("\n");
        builder.append("Created on: ").append(dateCreated).append("\n");
        builder.append("Admins: ").append(admins.size()).append("\n");
        builder.append("Topics: ").append(topics).append("\n");

        return builder.toString();
    }
}
