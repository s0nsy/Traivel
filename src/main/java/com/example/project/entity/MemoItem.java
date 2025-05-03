package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="memo_item")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("MEMO")
public class MemoItem extends RouteItem{
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(cascade =CascadeType.ALL)
   @JoinColumn(name="memo_id")
   private Memo memo;

   @ManyToOne(cascade =CascadeType.ALL)
   @JoinColumn(name="route_id")
   private Route route;

}
