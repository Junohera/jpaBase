package com.juno.jpabase.repository;

import com.juno.jpabase.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect() {
        // DB에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("================================");

        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2() {
        // DB에 존재하는 mno
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("================================");

        System.out.println(memo);
    }

    @Test
    public void testUpdate() {
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {
        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("------------------------------------------------------");

        // 총 몇 페이지
        System.out.println("Total Pages: " + result.getTotalPages());

        // 총 개수
        System.out.println("Total Count: " + result.getTotalElements());

        // 현재 페이지 번호 0부터 시작
        System.out.println("Page Number : " + result.getNumber());

        // 페이지당 데이터 갯수
        System.out.println("Page Size : " + result.getSize());

        // 다음 페이지 존재 여부
        System.out.println("has nexst Page ? : " + result.hasNext());

        // 시작 페이지(0) 여부
        System.out.println("first Page ? : " + result.isFirst());

        System.out.println("------------------------------------------------------");

        for (Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test
    public void testSort() {
        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testSort2() {
        Sort sort1 = Sort.by("memoText").ascending();
        Sort sort2 = Sort.by("mno").descending();
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethods() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo: list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodsWithPagable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(m -> System.out.println(m));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testgetListDesc() {
        List<Memo> list = memoRepository.getListDesc();
        list.forEach(m -> System.out.println(m));
    }

    @Test
    public void testUpdateMemoText() {
        int result = memoRepository.updateMemoText(10L, "update by query");
        System.out.println(result);
    }

    @Test
    public void testUpdateMemoText2() {
        Memo memo = Memo.builder()
                .mno(10L)
                .memoText("update by query2")
                .build();
        int result = memoRepository.updateMemoText(memo);
        System.out.println(result);
    }

    @Test
    public void testGetListWithQuery() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.getListWithQuery(55L, pageable);

        result.get().forEach(m -> System.out.println(m));
    }
}
