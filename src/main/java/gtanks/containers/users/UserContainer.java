package gtanks.containers.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_container")
@IdClass(UserContainer.UserContainerId.class)
public class UserContainer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "container_id")
    private Long containerId;

    @Column(name = "count")
    private Integer count;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserContainerId implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long userId;
        private Long containerId;
    }
}