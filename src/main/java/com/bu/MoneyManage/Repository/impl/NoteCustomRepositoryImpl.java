package com.bu.MoneyManage.Repository.impl;

import com.bu.MoneyManage.Repository.INoteCustomRepository;
import com.bu.MoneyManage.entity.Notes;
import com.bu.MoneyManage.entity.bean.NotesFilterBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class NoteCustomRepositoryImpl implements INoteCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Notes> findNotesByFilter(NotesFilterBean filter, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Notes> cq = cb.createQuery(Notes.class);
        Root<Notes> root = cq.from(Notes.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getUserId() != null)
            predicates.add(cb.equal(root.get("user").get("userId"), filter.getUserId()));

        if (filter.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));

        if (filter.getFromDate() != null)
            predicates.add(cb.greaterThanOrEqualTo(root.get("recordDateTime"), filter.getFromDate()));

        if (filter.getToDate() != null)
            predicates.add(cb.lessThanOrEqualTo(root.get("recordDateTime"), filter.getToDate()));

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("createdAt")));

        TypedQuery<Notes> query = entityManager.createQuery(cq);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Notes> notes = query.getResultList();

        // COUNT QUERY
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Notes> countRoot = countQuery.from(Notes.class);

        List<Predicate> countPredicates = new ArrayList<>();

        if (filter.getUserId() != null)
            countPredicates.add(cb.equal(countRoot.get("user").get("userId"), filter.getUserId()));

        if (filter.getStatus() != null)
            countPredicates.add(cb.equal(countRoot.get("status"), filter.getStatus()));

        if (filter.getFromDate() != null)
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("recordDateTime"), filter.getFromDate()));

        if (filter.getToDate() != null)
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("recordDateTime"), filter.getToDate()));

        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(notes, pageable, total);
    }
}
