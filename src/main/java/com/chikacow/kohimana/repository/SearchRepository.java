package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SearchRepository {
    private static final String LIKE_FORMAT = "%%%s%%";

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Yet to implement sort functions
     * Bugs with total page
     * @param pageNo
     * @param pageSize
     * @param search
     * @return
     */
    public PageResponse<?> getAllUsersWithSearch(int pageNo, int pageSize, String search) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT new com.chikacow.kohimana.dto.response.UserResponseDTO(" +
                        "u.firstName, u.lastName, u.email, u.phoneNumber, " +
                        "u.dateOfBirth, u.gender, u.username, u.password, u.createdAt) " +
                        "FROM User u " +
                        "WHERE 1=1 "
        );

        Query selectQuery = null;
        if (search != null && !search.isEmpty()) {

            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");

            selectQuery = entityManager.createQuery(sqlQuery.toString());
            selectQuery.setParameter("firstName", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("lastName", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("email", String.format(LIKE_FORMAT, search));

        }

        if (selectQuery == null) {
            selectQuery = entityManager.createQuery(sqlQuery.toString());
        }
        selectQuery.setFirstResult((pageNo - 1) * pageSize);
        selectQuery.setMaxResults(pageSize);

        List<UserResponseDTO> users = selectQuery.getResultList();

        System.out.println(users);


        // Count users
        StringBuilder sqlCountQuery = new StringBuilder("SELECT COUNT(*) FROM User u");
        Query countQuery = null;
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" WHERE lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" OR lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" OR lower(u.email) like lower(?3)");

            countQuery = entityManager.createQuery(sqlCountQuery.toString());

            countQuery.setParameter(1, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(2, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(3, String.format(LIKE_FORMAT, search));
            //countQuery.getSingleResult();
        }
        if (countQuery == null) {
            countQuery = entityManager.createQuery(sqlCountQuery.toString());
        }

        Long totalElements = (Long) countQuery.getSingleResult();

        log.info("totalElements={}", totalElements);

        List<Sort.Order> orders = new ArrayList<>();



        //sort ko thuc su sap xep trong pageable, no chi hoat dong khi tuong tac voi db qua findAll()
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        Page<?> page = new PageImpl<>(users, pageable, totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(users)
                .build();


    }
}
