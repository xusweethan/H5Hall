
package com.m.h5hall.comm.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;


@Table(name = "t_global_games")  // 建议加上注解， 混淆后表名不受影响
public class GlobalGame {

	@Column(column = "KEY")
    public String key;
	
    @Column(column = "RESURL")
    public String resurl;

    @Column(column = "ICON")
    public String icon;

    @Column(column = "HORIZONTAL")
    public Integer horizontal;

    @Column(column = "NAME")
    public String name;
    
    @Column(column = "BRIEF")
    public String brief;

    @Column(column = "VERSION")
    public Integer version;
    
    @Column(column = "SIZE")
    public Integer size;
    
    @Column(column = "TYPE")
    public Integer type;
}
