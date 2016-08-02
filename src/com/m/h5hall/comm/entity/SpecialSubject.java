
package com.m.h5hall.comm.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;


@Table(name = "special_Topic_table")  // 建议加上注解， 混淆后表名不受影响
public class SpecialSubject {

	@Column(column = "ID")
    public String id;
	
    @Column(column = "BANNER_URL")
    public String banner;

    @Column(column = "ICON_URL")
    public String icon;

    @Column(column = "SUBJECT_NAME")
    public Integer name;

    @Column(column = "INTRODUCE")
    public String intro;
    
}
