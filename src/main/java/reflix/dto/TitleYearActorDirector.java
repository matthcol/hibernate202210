package reflix.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TitleYearActorDirector {
    private String title;
    private Short year;
    private String actorName;
    private String directorName;
}
