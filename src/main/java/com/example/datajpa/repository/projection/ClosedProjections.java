package com.example.datajpa.repository.projection;

public interface ClosedProjections {
    String getName();
    TeamInfo getTeam();

    interface TeamInfo{ //최적화는 안댐.left join 나가기 때문에 안정성은 보장.
        String getName();
    }
}
