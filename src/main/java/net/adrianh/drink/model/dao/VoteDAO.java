package net.adrianh.drink.model.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.Getter;
import net.adrianh.drink.model.entity.QVote;
import net.adrianh.drink.model.entity.Vote;

@Stateless
public class VoteDAO extends AbstractDAO<Vote> {
    @Getter @PersistenceContext(unitName = "drinkdb")
    private EntityManager entityManager;
    
    public VoteDAO() {
        super(Vote.class);
    }
    
    public int allDrinkVotes(long drinkID){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QVote vote = QVote.vote;
        List<Vote> votes = queryFactory.selectFrom(vote)
            .where(vote.drink.id.eq(drinkID))
            .fetch();
       
        return votes.size();
    }
    
    public Vote selectVote(long userID, long drinkID){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QVote vote = QVote.vote;
        List<Vote> votes = queryFactory.selectFrom(vote)
            .where(vote.user_id.id.eq(userID).and(vote.drink.id.eq(drinkID)))
            .fetch();
       
        return votes.get(0);
    }
    
    public boolean hasUserVotedDrink(long userID, long drinkID){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QVote vote = QVote.vote;
        List<Vote> votes = queryFactory.selectFrom(vote)
            .where(vote.user_id.id.eq(userID).and(vote.drink.id.eq(drinkID)))
            .fetch();
       
        return !votes.isEmpty();
    }
    
    public boolean hasUserUpvotedDrink(long userID, long drinkID){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QVote vote = QVote.vote;
        List<Vote> votes = queryFactory.selectFrom(vote)
            .where(vote.user_id.id.eq(userID).and(vote.drink.id.eq(drinkID).and(vote.val.eq(1))))
            .fetch();
       
        return !votes.isEmpty();
    }
    
    public boolean hasUserDownvotedDrink(long userID, long drinkID){
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QVote vote = QVote.vote;
        List<Vote> votes = queryFactory.selectFrom(vote)
            .where(vote.user_id.id.eq(userID).and(vote.drink.id.eq(drinkID).and(vote.val.eq((-1)))))
            .fetch();
       
        return !votes.isEmpty();
    }
    
}