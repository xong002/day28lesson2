package practice.day28lesson2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    private Integer id;
    private String name;
    private String img;
}
