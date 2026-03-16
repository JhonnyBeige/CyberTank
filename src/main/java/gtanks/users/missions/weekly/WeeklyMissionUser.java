package gtanks.users.missions.weekly;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "weekly_mission_user")
@Data
public class WeeklyMissionUser {
    @Id
    @Column(name = "userId")
    public Long userId;
    @Column(name = "changePrice")
    public Integer changePrice;
    @Column(name = "missionId1")
    public String missionId1;
    @Column(name = "missionProgr1")
    public Integer missionProgr1;
    @Column(name = "missionId2")
    public String missionId2;
    @Column(name = "missionProgr2")
    public Integer missionProgr2;
    @Column(name = "missionId3")
    public String missionId3;
    @Column(name = "missionProgr3")
    public Integer missionProgr3;
}