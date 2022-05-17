package domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Review {
	private Long rno;
	private String id;
	private Long sno;
	private String content;
	private Double likes;
	private Long groupId;
}
