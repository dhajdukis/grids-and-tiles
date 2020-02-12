package hu.dhajdukis.gridsandtiles.entity;

import java.util.List;
import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class Grid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer width;
    private Integer height;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grid")
    private List<Tile> tiles;
}
