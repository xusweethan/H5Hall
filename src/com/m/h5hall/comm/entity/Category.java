
package com.m.h5hall.comm.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;


@Table(name = "t_game_category")  // 建议加上注解， 混淆后表名不受影响
public class Category {

	@Column(column = "_id")
    public int id;
	
	@Column(column = "CODE")
    public String code;
	
    @Column(column = "NAME")
    public String name;

}
