package be.digitcom.course.management.repository;

import be.digitcom.course.management.model.IModel;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Transactional
public class AbstractGenericDAO<T extends IModel> implements IGenericDAO<T> {
    @PersistenceContext
    protected EntityManager em;

    protected Class<T> entityClass = (Class<T>) ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public T find(Long id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        final List<T> resultList = this.em.createQuery("SELECT v FROM " + this.entityClass.getCanonicalName() + " v").getResultList();

        return resultList;
    }

    @Override
    public void save(T entity) {
        em.persist(entity);
    }

    @Override
    public T update(T entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(final Long id) {
        em.remove(em.getReference(entityClass, id));
    }

    @Override
    public Session getSession() {
        return (Session) this.em.getDelegate();
    }
}
