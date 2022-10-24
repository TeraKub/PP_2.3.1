package web.dao;

import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao{
    @PersistenceContext // Внедряет прокси, открывающий и закрывающий EntityManager автоматически
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        userCountStoredProcedure();
        return entityManager.createQuery("SELECT user FROM User user", User.class).getResultList();
    }

    @Override
    public void saveUser(User user) {
        entityManager.merge(user); // .saveOrUpdate(user)
    }

    @Override
    public User getUser(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(entityManager.find(User.class, id));
    }
    public void userCountStoredProcedure() {
        StoredProcedureQuery spq = entityManager
                .createStoredProcedureQuery("usersCount")
                .registerStoredProcedureParameter(1, Integer.class, ParameterMode.OUT);
        spq.execute();
        System.out.println(spq.getOutputParameterValue(1));
    }
}
