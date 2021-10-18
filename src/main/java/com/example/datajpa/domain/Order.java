package com.example.datajpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter@Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") //foreign key를 가지고있는 쪽을 연관관계 주인으로 설정하는것이 좋다.
    private Member member;
    //프록시 기술 사용을 위해 byteBuddyInterceptor객체가 들어가 있다.
    //Order 입장에선 얘를 어떻게 할 수가 없다.

    @OneToOne(fetch = LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    private OrderStatus status; //주문 상태 [ORDER / CANCEL]

    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
