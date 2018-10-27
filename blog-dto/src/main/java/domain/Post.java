package domain;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Post {

	private String id;

	private String title;

	private String content;

}
