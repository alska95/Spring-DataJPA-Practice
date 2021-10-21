package com.example.datajpa.repository.projection;

import org.springframework.beans.factory.annotation.Value;

public interface NameOnly {

    @Value("#{target.name + ' ' + target.age}") //open projection --> dp에서 다 가져온다음 앱에서 매칭시킨다.
    String getName();//close projection db에서 딱 원하는 값만 가져올 수 있다.
}
