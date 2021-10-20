package com.example.datajpa.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> {
    @Id //generated value는 persist해서 jpa가 관리할때 생성된다.
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id){
        this.id = id;
    }

    @Override
    public String getId(){
        return id;
    }
    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
