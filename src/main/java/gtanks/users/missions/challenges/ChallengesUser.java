package gtanks.users.missions.challenges;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "challenges_user")
@Data
public class ChallengesUser {
    @Id
    @Column(name = "userId")
    public Long userId;
    @Column(name = "stars")
    public Integer stars;
    @Column(name = "battlePass")
    public Boolean battlePass;
    @Column(name = "score")
    public Integer score;
}