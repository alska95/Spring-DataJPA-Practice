package com.example.datajpa.repository;

import com.example.datajpa.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item , String> {

}
