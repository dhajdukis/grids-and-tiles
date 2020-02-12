package hu.dhajdukis.gridsandtiles.entity;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
public class Tile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;
    private Integer xposition;
    private Integer yposition;
    @ManyToOne
    private Grid grid;
}
