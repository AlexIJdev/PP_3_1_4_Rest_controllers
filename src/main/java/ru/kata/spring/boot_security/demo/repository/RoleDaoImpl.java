package ru.kata.spring.boot_security.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRole(int id) {
        TypedQuery<Role> query = entityManager.createQuery("from Role p where p.id = ?1", Role.class);
        query.setParameter(1, id);
        return query.getSingleResult();
    }

    @Override
    public List<Role> showAllRoles() {
        return entityManager.createQuery("select p from Role p", Role.class).getResultList();
    }
}
