package com.example.datajpa.repository;

import com.example.datajpa.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save(){
//        Item item = new Item();
        //key값(GeneratedValue) 가 널이다. --> persist되는 순간 생성된다.
        //entityInformation.isNew(item) 메소드로 널임을 확인하고, 새로운 entity임을 확인한다.
        Item item = new Item("A");
        //pk에 뭔가가 들어있다?? 그러면 isNew가 false나옴 --> merge가 동작하게 된다. --> "A" 가 이미 있다고 가정!
        //이런 경우에는 그냥 save쓰지 말고 persistable사용할것.
        itemRepository.save(item);

    }
}