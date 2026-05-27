package com.example.project.repository;

import com.example.project.entity.MemoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemoItemRepository extends JpaRepository<MemoItem, Long> {

    @Query("SELECT mi FROM MemoItem mi WHERE mi.memo.id = :memoId")
    Optional<MemoItem> findByMemoId(@Param("memoId") Long memoId);
}
