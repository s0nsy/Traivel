package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="memo_item")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy=InheritanceType.JOINED)
public class MemoItem extends RouteItem{
   @ManyToOne(cascade =CascadeType.ALL)
   @JoinColumn(name="memo_id")
   private Memo memo;

}
