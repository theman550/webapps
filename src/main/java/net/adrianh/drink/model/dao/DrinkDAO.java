package net.adrianh.drink.model.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.Drink;
import net.adrianh.drink.model.entity.QDrink;
import net.adrianh.drink.model.entity.QVote;
import net.adrianh.drink.model.entity.Vote;

@Stateless
public class DrinkDAO extends AbstractDAO<Drink> {

    @Getter
    @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;

    public DrinkDAO() {
        super(Drink.class);
    }

    public List<Drink> findDrinksMatchingName(String s) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        List<Drink> drinks = queryFactory.selectFrom(drink)
                .where(drink.name.eq(s))
                .fetch();
        return drinks;
    }

    public Drink findDrinkByID(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        List<Drink> drinks = queryFactory.selectFrom(drink)
                .where(drink.id.eq(id))
                .fetch();
        return drinks.get(0);
    }

    public List<Drink> findDrinksStartMatchingName(String s) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        List<Drink> drinks = queryFactory.selectFrom(drink)
                .where(drink.name.startsWithIgnoreCase(s))
                .limit(5)
                .orderBy(drink.voteCount.desc())
                .fetch();
        return drinks;
    }

    //find matching name from offset ordered by popularity
    public QueryResults<Drink> findDrinksMatchingNameFromOffset(String s, int offset, String user, boolean getUpvotedDrinks, boolean getCreatedDrinks) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        JPAQuery jpaQuery = queryFactory.selectFrom(drink)
                .where(drink.name.eq(s))
                .limit(20)
                .offset(offset)
                .orderBy(drink.voteCount.desc());
        checkUserAuthToFetchCreatedOrUpvotedDrinks(jpaQuery, drink, user, getUpvotedDrinks, getCreatedDrinks);
        QueryResults<Drink> drinks = (QueryResults<Drink>) jpaQuery.fetchResults();
        return drinks;
    }

    //find matching name from offset ordered by newest
    public QueryResults<Drink> findDrinksMatchingNameFromOffsetByNewest(String s, int offset, String user, boolean getUpvotedDrinks, boolean getCreatedDrinks) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        JPAQuery jpaQuery = queryFactory.selectFrom(drink)
                .where(drink.name.eq(s))
                .limit(20)
                .offset(offset)
                .orderBy(drink.createdAt.desc());
        checkUserAuthToFetchCreatedOrUpvotedDrinks(jpaQuery, drink, user, getUpvotedDrinks, getCreatedDrinks);
        QueryResults<Drink> drinks = (QueryResults<Drink>) jpaQuery.fetchResults();
        return drinks;
    }

    public QueryResults<Drink> findMostPopularFromOffset(int offset, String user, boolean getUpvotedDrinks, boolean getCreatedDrinks) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        JPAQuery jpaQuery = queryFactory.selectFrom(drink)
                .offset(offset)
                .limit(20)
                .orderBy(drink.voteCount.desc());
        // Add where clause if fetching a specific user's drinks
        checkUserAuthToFetchCreatedOrUpvotedDrinks(jpaQuery, drink, user, getUpvotedDrinks, getCreatedDrinks);
        QueryResults<Drink> drinks = (QueryResults<Drink>) jpaQuery.fetchResults();
        return drinks;
    }

    public QueryResults<Drink> findNewestFromOffset(int offset, String user, boolean getUpvotedDrinks, boolean getCreatedDrinks) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QDrink drink = QDrink.drink;
        JPAQuery jpaQuery = queryFactory.selectFrom(drink)
                .offset(offset)
                .limit(20)
                .orderBy(drink.createdAt.desc());
        checkUserAuthToFetchCreatedOrUpvotedDrinks(jpaQuery, drink, user, getUpvotedDrinks, getCreatedDrinks);
        QueryResults<Drink> drinks = (QueryResults<Drink>) jpaQuery.fetchResults();
        return drinks;
    }

    public void checkUserAuthToFetchCreatedOrUpvotedDrinks(JPAQuery jpaQuery, QDrink drink, String user, boolean getUpvotedDrinks, boolean getCreatedDrinks) {
        QVote vote = QVote.vote;
        if (user != null && getCreatedDrinks == true) {
            jpaQuery.where(drink.user.accountName.eq(user));
        }
        if (user != null && getUpvotedDrinks == true) {
            jpaQuery.innerJoin(vote).on(drink.id.eq(vote.drink.id)).where(vote.val.eq(1).and(vote.user_id.accountName.eq(user).and(vote.drink.eq(drink))));
            //jpaQuery.where(drink.votes.any().val.eq(1).and(drink.votes.any().user_id.accountName.eq(user)));
        }

    }

    public int findAllDrinkVotes(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QVote vote = QVote.vote;
        List<Vote> votes = queryFactory.selectFrom(vote)
                .where(vote.drink.id.eq(id))
                .fetch();

        int i = 0;
        for (Vote v : votes) {
            i += v.getVal();
        }

        return i;
    }
}
