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
    private String owner;
    private List<String> userList = new ArrayList<>();
    private List<String> vetList = new ArrayList<>();
    private List<String> sellerList = new ArrayList<>();
    private Date timeStamp;
    private List<String> adminUsers = new ArrayList<>();
    private List<String> adminVets = new ArrayList<>();
    private List<String> adminSellers = new ArrayList<>();
    private List<String> topics = new ArrayList<>(); // like hashtag or something where the community topics are shown


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name: ").append(name).append("\n").append("Description: ").append(description).append("\n");
//        builder.append("Created on: ").append(timeStamp).append("\n");
        builder.append("Admins: ").append(adminSellers.size() + adminUsers.size() + adminVets.size()).append("\n");
        builder.append("Topics: ").append(topics).append("\n");

        return builder.toString();
    }
}
