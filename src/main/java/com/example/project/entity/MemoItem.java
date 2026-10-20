package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "memo_item")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("MEMO")
public class MemoItem extends RouteItem {

   @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinColumn(name = "memo_id")
   private Memo memo;
}
