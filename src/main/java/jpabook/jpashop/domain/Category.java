package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    // ManyToMany는 실무에서는 쓰기 힘들다. why? 중간에 추가되는 부분이 많기 때문에
    @ManyToMany
    // 중간테이블 매핑을 위해서 조인을 해줘야 함.
    @JoinTable(name = "category_item",
            // 중간테이블(category_item) 에 있는 category_id
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    // 부모는 한개
    private Category parent;


    @OneToMany(mappedBy = "parent")
    // 자식은 여러 개
    private List<Category> child = new ArrayList<>();

    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}





